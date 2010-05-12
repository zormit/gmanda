package de.fu_berlin.inf.gmanda.gui.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.picocontainer.annotations.Inject;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Ordering;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;
import de.fu_berlin.inf.gmanda.gui.graph.ExportSettings.ExportSetting;
import de.fu_berlin.inf.gmanda.gui.misc.ExtensionDescriptor;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.util.AutoHashMap;
import de.fu_berlin.inf.gmanda.util.CMultimap;
import de.fu_berlin.inf.gmanda.util.CUtils;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.StringJoiner;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class SocialNetworkModule {

	@Inject
	VelocitySupport velocitySupport;

	public enum SNAFileType implements ExtensionDescriptor {

		DOT("graphDOT", "Graphviz Dot with Core Cluster *.dot", ".dot") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.SELFLOOPS, ExportSetting.CLUSTER,
					ExportSetting.UNDIRECTED);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		DOTNOCLUSTER("graphDOTnocluster",
				"Graphviz Dot without Core Cluster *.dot", ".dot") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.SELFLOOPS, ExportSetting.UNDIRECTED);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		COMMUNITYDOT("graphDOTcommunity",
				"Graphviz Dot Community Assignment *.dot", ".dot") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.UNDIRECTED, ExportSetting.ONLYGIANTCOMPONENT);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		GRAPHML("graphML", "GraphML *.graphml", ".graphml") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.UNDIRECTED);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		EDGE("edge", "Edge format *.edge", ".edge") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.UNDIRECTED, ExportSetting.ONLYGIANTCOMPONENT);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		PREFUSE("prefuse", "Prefuse Graph Format *.xml", ".xml") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.UNDIRECTED);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		TAB("tab", "Tabbed format *.tab", ".tab") {
			ExportSettings defaultSettings = new DefaultExportSettings(
					ExportSetting.UNDIRECTED, ExportSetting.ONLYGIANTCOMPONENT);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		ICC("icc", "GraphViz Dot Inter Cluster Graph *.dot", ".dot") {
			ExportSettings defaultSettings = new ExportSettings(EnumSet.of(
					ExportSetting.SELFLOOPS, ExportSetting.INTERCLUSTERGRAPH,
					ExportSetting.CLUSTER), Coloration.FIXED, null, null,
					new MonthClusterBuilder(1, 9));

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		};

		protected String description;

		protected Collection<String> extensions;

		protected String velocityFileName;

		SNAFileType(String velocityFileName, String description,
				String... extensions) {
			this.velocityFileName = velocityFileName;
			this.description = description;
			this.extensions = Arrays.asList(extensions);
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public Collection<String> getSupportedExtensions() {
			return extensions;
		}

		public abstract ExportSettings getDefaultSettings();

		public String getVelocityFileName() {
			return velocityFileName;
		}
	}

	public class ProjectToPlot {

		String name;

		Collection<Node> members;

		int clusterID;

		public String getName() {
			return name;
		}

		public Collection<Node> getMembers() {
			return members;
		}
		
		public boolean isPrintMembers(){
			return members != null && members.size() > 0;
		}

		public int getClusterID() {
			return clusterID;
		}
	}

	public Map<String, Object> computeSocialNetwork(PrimaryDocument root,
			ExportSettings settings, File targetFile, IProgress pm) {

		pm.setScale(100);
		pm.start();

		try {

			Map<Pair<Node, Node>, List<PrimaryDocument>> conversations = AutoHashMap
					.getListHashMap();

			HashMap<String, Node> authors = new HashMap<String, Node>();

			int numberOfPds = PrimaryDocument.getTreeWalker(root).size();
			pm.work(5);

			System.out.println("# of PDs: " + numberOfPds);

			Graph g = new Graph(settings);

			{ // Build author map
				IProgress pToFetch = pm.getSub(75);
				pToFetch.setScale(numberOfPds);
				pToFetch.start();

				for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(root)) {

					if (pd.isMailingList())
						continue;

					Node author = getAuthor(pd, authors, g);
					if (author == null)
						continue;

					author.add(pd);

					if (pd.isThreadStart()) {
						author.threadsStarted++;
						continue;
					}

					PrimaryDocument parent = pd.getParent();
					if (parent == null) {
						// Should warn!
						continue;
					}

					boolean countOnlyFirstAnswer = false;
					if (countOnlyFirstAnswer) {
						if (!parent.isThreadStart()
								|| parent.getChildren().indexOf(pd) != 0)
							continue; // Only count first answers
					}

					Node parentAuthor = getAuthor(parent, authors, g);
					if (parentAuthor == null)
						continue;

					if (!settings.isIncludeSelfLoops()
							&& author.equals(parentAuthor))
						continue;

					if (settings.isUndirected()
							&& parentAuthor.getName().compareTo(
									author.getName()) < 0) {
						conversations.get(
								new Pair<Node, Node>(parentAuthor, author))
								.add(pd);
					} else {
						conversations.get(
								new Pair<Node, Node>(author, parentAuthor))
								.add(pd);
					}

					pToFetch.work(1);
				}
				pToFetch.done();
			}

			List<Node> plainAuthors = new ArrayList<Node>(authors.values());
			Collections.sort(plainAuthors);
			Collections.reverse(plainAuthors);

			System.out.println("# of Authors: " + plainAuthors.size());
			System.out.println("# of Edges: " + conversations.size());

			List<Edge> edges = new ArrayList<Edge>(conversations.size());

			{ // Convert
				IProgress pToFetch = pm.getSub(10);
				pToFetch.setScale(conversations.size());
				pToFetch.start();

				for (Entry<Pair<Node, Node>, List<PrimaryDocument>> entry : conversations
						.entrySet()) {

					edges.add(g.new Edge(entry.getKey().p, entry.getKey().v,
							entry.getValue().size()));
					pToFetch.work(1);
				}
				pToFetch.done();
			}

			// Check whether the number add up!
			if (!settings.isUndirected() && settings.isIncludeSelfLoops()) {

				// for (Node node : plainAuthors){
				// int result = 0;
				// for (Edge e : edges){
				// if (e.from == node)
				// result += e.weight;
				// }
				// if (node.emailsWritten - node.threadsStarted != result)
				// throw new IllegalArgumentException("Something is wrong!");
				// }
			}

			// Build Clusters
			List<Cluster> clusters = settings.getClusterBuilder().getClusters(
					g, plainAuthors, edges);

			Collection<List<Node>> communityAssignment;
			try {
				communityAssignment = getCommunitiesFromFile(
						plainAuthors,
						new File(
								"C:\\Documents and Settings\\oezbek\\Desktop\\Temp\\2009-12\\argo-fc_second.groups"));
			} catch (FileNotFoundException e3) {
				throw new ReportToUserException(e3);
			}

			HashMap<String, Object> icc = printInterClusterCommunication(
					settings, root, plainAuthors, edges, targetFile);
			if (settings.isInterClusterGraph()) {
				return icc;
			}

			// cluster(edges, authors);

			Node cluster0 = g.new Node("cluster0");
			cluster0.cluster = clusters.get(0);
			if (settings.isCluster() && settings.isCollapseEdges()) {

				List<Edge> collapsedEdges = new ArrayList<Edge>(edges.size());
				HashMap<Node, Edge> collapsing = new HashMap<Node, Edge>();

				for (Edge e : edges) {

					Node one = e.from;
					Node two = e.to;

					if (one.project) {
						if (two.project) {
							collapsedEdges.add(e);
						} else {
							Edge e2 = collapsing.get(two);
							if (e2 == null) {
								e2 = g.new Edge(cluster0, two, 0);
								collapsing.put(two, e2);
								collapsedEdges.add(e2);
							}
							e2.weight += e.weight;

							Edge e1 = collapsing.get(one);
							if (e1 == null) {
								e1 = g.new Edge(one, cluster0, 0);
								collapsing.put(one, e1);
								collapsedEdges.add(e1);
							}
							e1.weight += e.weight;
						}
					} else {
						if (two.project) {
							Edge e2 = collapsing.get(one);
							if (e2 == null) {
								e2 = g.new Edge(one, cluster0, 0);
								collapsing.put(one, e2);
								collapsedEdges.add(e2);
							}
							e2.weight += e.weight;

							Edge e1 = collapsing.get(two);
							if (e1 == null) {
								e1 = g.new Edge(cluster0, two, 0);
								collapsing.put(two, e1);
								collapsedEdges.add(e1);
							}
							e1.weight += e.weight;
						} else {
							collapsedEdges.add(e);
						}
					}
				}
				edges = collapsedEdges;
			}

			if (settings.isOnlyGiantComponent() && plainAuthors.size() > 0) {
				Node mostActiveAuthor = plainAuthors.get(0);

				LinkedList<Node> todo = new LinkedList<Node>();
				Set<Node> result = new HashSet<Node>();
				todo.add(mostActiveAuthor);
				result.add(mostActiveAuthor);

				while (todo.size() > 0) {
					Node next = todo.removeFirst();
					for (Edge e : edges) {
						Node otherEnd = e.getOtherEnd(next);
						if (otherEnd != null && !result.contains(otherEnd)) {
							todo.add(otherEnd);
							result.add(otherEnd);
						}
					}
				}

				List<Edge> newEdges = new ArrayList<Edge>();
				for (Edge e : edges) {
					if (result.contains(e.getFrom())) {
						assert result.contains(e.getTo()); // invariant of the
						// giant component!
						newEdges.add(e);
					}
				}

				edges = newEdges;
				plainAuthors = new ArrayList<Node>(result);
			}

			ProjectToPlot project = new ProjectToPlot();
			project.name = root.getMetaData("list");
			if ("gmane.comp.db.axion.devel".equals(project.name)) {
				project.name = "gmane.comp.lang.uml.argouml.devel";
			}
			if (clusters.size() == 0 || !settings.isCluster()) {
				project.members = new ArrayList<Node>();
			} else {
				project.members = clusters.get(0).getAuthors();
			}

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("edges", edges);
			result.put("authors", plainAuthors);
			result.put("project", project);
			result.put("communities", communityAssignment);
			return result;

		} finally {
			pm.done();
		}
	}

	protected HashMap<String, Object> printInterClusterCommunication(
			ExportSettings settings, PrimaryDocument root,
			List<Node> plainAuthors, List<Edge> edges, File targetFile) {

		final AutoHashMap<Cluster, AutoHashMap<Cluster, Integer>> interClusterCommunication = new AutoHashMap<Cluster, AutoHashMap<Cluster, Integer>>(
				new Supplier<AutoHashMap<Cluster, Integer>>() {
					@Override
					public AutoHashMap<Cluster, Integer> get() {
						return new AutoHashMap<Cluster, Integer>(0);
					}
				});

		Set<Cluster> clusters = new TreeSet<Cluster>(Ordering.usingToString());

		for (Edge e : edges) {

			Cluster clusterOne = e.from.getCluster();
			clusters.add(clusterOne);
			Cluster clusterTwo = e.to.getCluster();
			clusters.add(clusterTwo);

			interClusterCommunication.get(clusterOne).put(
					clusterTwo,
					interClusterCommunication.get(clusterOne).get(clusterTwo)
							+ e.weight);
		}

		List<Function<Cluster, String>> columns = new ArrayList<Function<Cluster, String>>();
		List<String> columnTitles = new ArrayList<String>();

		for (final Cluster cluster : clusters) {
			columnTitles.add(cluster.getName());
			columns.add(new Function<Cluster, String>() {
				@Override
				public String apply(Cluster from) {
					return interClusterCommunication.get(cluster).get(from)
							.toString();
				}
			});
		}
		;

		columnTitles.add("threadsStarted");
		columns.add(new Function<Cluster, String>() {
			@Override
			public String apply(Cluster from) {
				return String.valueOf(from.getThreadsStarted());
			}
		});

		columnTitles.add("size");
		columns.add(new Function<Cluster, String>() {
			@Override
			public String apply(Cluster from) {
				return String.valueOf(from.getAuthors().size());
			}
		});

		columnTitles.add("emailsTotal");
		columns.add(new Function<Cluster, String>() {
			@Override
			public String apply(Cluster from) {
				int result = 0;
				for (Node author : from.getAuthors()) {
					result += author.emailsWritten;
				}
				return String.valueOf(result);
			}
		});

		columnTitles.add("repliesTotal");
		columns.add(new Function<Cluster, String>() {
			@Override
			public String apply(Cluster from) {
				int result = 0;
				for (Node author : from.getAuthors()) {
					result += author.emailsWritten - author.threadsStarted;
				}
				return String.valueOf(result);
			}
		});

		// Print ICC to Console
		StringJoiner joiner = new StringJoiner("\t");
		joiner.append(root.getShortListGuess());
		for (String two : columnTitles) {
			joiner.append(two);
		}
		joiner.appendNoJoin("\n");

		for (Cluster one : clusters) {
			joiner.appendNoJoin(StringUtils.rightPad(one.getName(), 15));
			for (Function<Cluster, String> column : columns) {
				joiner.append(column.apply(one));
			}
			joiner.appendNoJoin("\n");
		}
		System.out.println(joiner.toString());

		if (settings.isInterClusterGraph())
			try {
				FileUtils.writeStringToFile(new File(FilenameUtils
						.removeExtension(targetFile.getPath())
						+ ".txt"), joiner.toString());
			} catch (IOException e) {
				throw new ReportToUserException(e);
			}

		// Build Graph in case we create ICC Dot Graphs
		Graph iccGraph = new Graph(settings);
		List<Edge> iccEdges = new ArrayList<Edge>();
		List<Node> iccNodes = new ArrayList<Node>();

		Node threadStartNode = iccGraph.new Node("Threads Started");
		iccNodes.add(threadStartNode);

		Map<Cluster, Node> authors = new HashMap<Cluster, Node>();

		for (Cluster one : clusters) {
			Node author = iccGraph.new Node(one.getName());
			iccNodes.add(author);
			authors.put(one, author);

			int threadStarts = one.getThreadsStarted();
			author.emailsWritten += threadStarts;

			// Add virtually!
			threadStartNode.emailsWritten += threadStarts;

			iccEdges.add(iccGraph.new Edge(author, threadStartNode,
					threadStarts));
		}

		for (Cluster one : clusters) {
			for (Cluster two : clusters) {

				int emailsWritten = interClusterCommunication.get(one).get(two);
				if (emailsWritten == 0)
					continue;
				Node intermediateNode = iccGraph.new Node("");
				intermediateNode.emailsWritten = emailsWritten / 96;
				iccNodes.add(intermediateNode);
				authors.get(one).emailsWritten += emailsWritten;
				iccEdges.add(iccGraph.new Edge(authors.get(one),
						intermediateNode, emailsWritten));
				iccEdges.add(iccGraph.new Edge(intermediateNode, authors
						.get(two), emailsWritten));
			}
		}

		// for (Node a : iccNodes){
		// a.emailsWritten /= 50;
		// }
		//		
		// for (Edge e : iccEdges){
		// e.weight /= 5;
		// }

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("edges", iccEdges);
		result.put("nodes", iccNodes);
		return result;
	}

	protected Node getAuthor(PrimaryDocument pd, HashMap<String, Node> authors,
			Graph g) {

		String authorsName = pd.getMetaData("from");

		if (authorsName == null)
			return null;

		authorsName = CUtils.cleanAuthor(authorsName);
		// Normalize to lower case and remove initials
		String[] nameParts = authorsName.toLowerCase().replaceAll(
				"(^|\\s)\\w\\.?(\\s|$)", " ").split("\\s");
		// Sort
		Arrays.sort(nameParts);
		String authorsNameLookup = StringUtils.join(nameParts);
		if (authorsNameLookup.length() <= 1)
			authorsNameLookup = authorsName;

		Node author = authors.get(authorsNameLookup);
		if (author == null) {
			author = g.new Node(authorsName);
			authors.put(authorsNameLookup, author);
		}
		author.addName(authorsName);
		return author;
	}

	public List<Node> getProjectMembersAsClique(
			CMultimap<Pair<String, String>, PrimaryDocument> conversations,
			List<Node> plainAuthors) {

		List<Node> projectMembers = new ArrayList<Node>();

		a: for (Node a : plainAuthors) {
			for (Node b : projectMembers) {
				if (!(conversations.containsKey(new Pair<String, String>(
						a.name, b.name)) && conversations
						.containsKey(new Pair<String, String>(b.name, a.name)))) {
					continue a;
				}
			}
			projectMembers.add(a);
			a.project = true;
		}
		return projectMembers;
	}

	/**
	 * Given a file containing group information in the format
	 * 
	 * GROUP.... 3 4 GROUP 1 2
	 * 
	 * Will return a Map of node-ID to community number (which starts at one)
	 * 
	 */
	public HashMap<Integer, Integer> getCommunityAssignment(File file)
			throws FileNotFoundException {

		Scanner sc = new Scanner(file);

		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

		int group = 0;
		while (sc.hasNext()) {
			String s = sc.nextLine();
			if (s.startsWith("GROUP")) {
				group++;
			} else {
				int i = Integer.parseInt(s);

				result.put(i, group);
			}
		}

		return result;
	}

	public Collection<List<Node>> getCommunitiesFromFile(
			List<Node> plainAuthors, File file) throws FileNotFoundException {

		HashMap<Integer, Integer> communities = getCommunityAssignment(file);

		AutoHashMap<Integer, List<Node>> map = AutoHashMap.getListHashMap();

		for (Node author : plainAuthors) {

			Integer community = communities.get((Integer) author.getId());
			if (community == null)
				community = 0;

			map.get(community).add(author);
		}

		return map.values();
	}

	/**
	 * Extracts project members based on a threshold of the percentage of weeks
	 * that member was active posting in the project.
	 * 
	 * @param threshold
	 *            0.0 - 1.0
	 */
	public List<Node> getProjectMembersFromWeekActivity(
			List<Node> plainAuthors, double threshold) {

		List<Node> projectMembers = new ArrayList<Node>();

		for (Node a : plainAuthors) {
			if (a.weeks.getNonZeroBins() / 53.0 > threshold) {
				projectMembers.add(a);
				a.project = true;
			}
		}
		return projectMembers;
	}

	/**
	 * Extracts project members based on a threshold of the percentage of months
	 * that member was active posting in the project.
	 * 
	 * @param threshold
	 *            0.0 - 1.0
	 */
	public List<Node> getProjectMembersFromMonthActivity(
			List<Node> plainAuthors, double threshold) {

		List<Node> projectMembers = new ArrayList<Node>();

		for (Node a : plainAuthors) {
			if (a.months.getNonZeroBins() / (double) a.months.getNumberOfBins() > threshold) {
				projectMembers.add(a);
				a.project = true;
			}
		}
		return projectMembers;
	}

	public void createNetwork(PrimaryDocument pd, SNAFileType type,
			ExportSettings settings, File targetFile, IProgress progress) {

		if (settings == null)
			settings = type.getDefaultSettings();

		Map<String, Object> data = computeSocialNetwork(pd, settings,
				targetFile, progress);

		// Run template engine
		String result = velocitySupport.run(data, type.getVelocityFileName());

		try {
			FileUtils.writeStringToFile(targetFile, result, "utf8");
		} catch (IOException e) {
			throw new ReportToUserException(e);
		}
	}
}
