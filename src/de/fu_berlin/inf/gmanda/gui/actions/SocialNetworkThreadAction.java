package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FileUtils;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Author;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.ExtensionDescriptor;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.AutoHashMap;
import de.fu_berlin.inf.gmanda.util.CMultimap;
import de.fu_berlin.inf.gmanda.util.CUtils;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Good fdp graph:
 * 
 * fdp -x -Tsvg -Gepsilon=0.0000001 -Gmaxiter=100000 -Gstart=5 dot2.dot >
 * diagram.svg
 */
public class SocialNetworkThreadAction extends AbstractAction {

	@Inject
	PrimaryDocumentTree tree;

	@Inject
	CommonService commonService;

	@Inject
	GmaneFacade facade;

	@Inject
	DotFileFileChooser dotFileChooser;

	@Inject
	VelocitySupport velocitySupport;

	public SocialNetworkThreadAction(ProjectProxy project) {
		super("Print Social Network to Dot-File...");

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
	}

	public static class ExportSettings {

		public ExportSettings(boolean cluster, boolean undirected,
				boolean onlyGiantComponent, boolean selfLoops) {
			this.cluster = cluster;
			this.undirected = undirected;
			this.onlyGiantComponent = onlyGiantComponent;
			this.selfLoops = selfLoops;
		}

		protected boolean selfLoops;

		protected boolean cluster;

		protected boolean undirected;

		protected boolean onlyGiantComponent;

		public boolean isUndirected() {
			return undirected;
		}

		public boolean isCluster() {
			return cluster;
		}

		public boolean isOnlyGiantComponent() {
			return onlyGiantComponent;
		}

		public boolean includeSelfLoops() {
			return selfLoops;
		}

	}

	public enum SNAFileType implements ExtensionDescriptor {

		DOT("graphDOT", "Graphviz Dot *.dot", ".dot") {
			ExportSettings defaultSettings = new ExportSettings(true, true,
					false, true);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		COMMUNITYDOT("graphDOTcommunity", "Graphviz Dot Community Assignment *.dot", ".dot") {
			ExportSettings defaultSettings = new ExportSettings(false, true, false, false);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		GRAPHML("graphML", "GraphML *.graphml", ".graphml") {
			ExportSettings defaultSettings = new ExportSettings(false, true,
					false, true);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		EDGE("edge", "Edge format *.edge", ".edge") {
			ExportSettings defaultSettings = new ExportSettings(false, true,
					false, false);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		PREFUSE("prefuse", "Prefuse Graph Format *.xml", ".xml") {
			ExportSettings defaultSettings = new ExportSettings(false, true,
					false, true);

			@Override
			public ExportSettings getDefaultSettings() {
				return defaultSettings;
			}
		},
		TAB("tab", "Tabbed format *.tab", ".tab") {
			ExportSettings defaultSettings = new ExportSettings(false, true,
					true, false);

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

	public void actionPerformed(ActionEvent arg0) {

		commonService.run(new Runnable() {
			public void run() {

				IProgress progress = commonService
						.getProgressBar("Calculate Project Statistics");

				try {
					TreePath path = tree.getSelectionPath();

					if (path == null)
						return;

					Object o = path.getLastPathComponent();

					if (o == null || !(o instanceof PrimaryDocument))
						return;

					PrimaryDocument pd = (PrimaryDocument) o;

					File dotFile = dotFileChooser.getSaveFile();

					if (dotFile == null)
						return;

					SNAFileType type = dotFileChooser.getSelectedFileType();

					if (type == null) {
						throw new NullPointerException(
								"Must choose a file type for SNA Export");
					}

					Map<String, Object> data = computeSocialNetwork(pd, type
							.getDefaultSettings(), progress);

					// Run template engine
					String result = velocitySupport.run(data, type
							.getVelocityFileName());

					try {
						FileUtils.writeStringToFile(dotFile, result, "latin1");
					} catch (IOException e) {
						throw new ReportToUserException(e);
					}
				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing thread statistics");

	}

	public class ProjectToPlot {

		String name;

		List<Author> members;

		int clusterID;

		public String getName() {
			return name;
		}

		public List<Author> getMembers() {
			return members;
		}

		public int getClusterID() {
			return clusterID;
		}
	}

	public Map<String, Object> computeSocialNetwork(PrimaryDocument root,
			ExportSettings settings, IProgress pm) {

		pm.setScale(100);
		pm.start();

		try {

			Map<Pair<Author, Author>, List<PrimaryDocument>> conversations = AutoHashMap
					.getListHashMap();

			HashMap<String, Author> authors = new HashMap<String, Author>();

			int numberOfPds = PrimaryDocument.getTreeWalker(root).size();
			pm.work(5);

			System.out.println("# of PDs: " + numberOfPds);

			Graph g = new Graph();

			{ // Build author map
				IProgress pToFetch = pm.getSub(75);
				pToFetch.setScale(numberOfPds);
				pToFetch.start();

				for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(root)) {

					Author author = getAuthor(pd, authors, g);
					if (author == null)
						continue;

					author.add(pd);

					PrimaryDocument parent = pd.getParent();
					if (parent == null)
						continue;

					Author parentAuthor = getAuthor(parent, authors, g);
					if (parentAuthor == null)
						continue;

					if (!settings.includeSelfLoops()
							&& author.equals(parentAuthor))
						continue;

					if (settings.isUndirected()
							&& parentAuthor.getName().compareTo(
									author.getName()) < 0) {
						conversations.get(
								new Pair<Author, Author>(author, parentAuthor))
								.add(pd);
					} else {
						conversations.get(
								new Pair<Author, Author>(parentAuthor, author))
								.add(pd);
					}

					pToFetch.work(1);
				}
				pToFetch.done();
			}

			List<Author> plainAuthors = new ArrayList<Author>(authors.values());
			Collections.sort(plainAuthors);
			Collections.reverse(plainAuthors);

			List<Author> projectMembers = getProjectMembersFromMonthActivity(
					plainAuthors, 0.666);
			/*
			 * List<Author> projectMembers = getProjectMembersAsClique(
			 * conversations, plainAuthors);
			 */
			System.out.println("# of Authors: " + plainAuthors.size());
			System.out.println("# of Edges: " + conversations.size());

			List<Edge> edges = new ArrayList<Edge>(conversations.size());

			{ // Convert
				IProgress pToFetch = pm.getSub(10);
				pToFetch.setScale(conversations.size());
				pToFetch.start();

				for (Entry<Pair<Author, Author>, List<PrimaryDocument>> entry : conversations
						.entrySet()) {

					edges.add(g.new Edge(entry.getKey().p, entry.getKey().v,
							entry.getValue().size()));
					pToFetch.work(1);
				}
				pToFetch.done();
			}

			Collection<List<Author>> communityAssignment; 
			try {
				communityAssignment = getCommunitiesFromFile(plainAuthors, new File("C:\\Documents and Settings\\oezbek\\Desktop\\Temp\\2009-12\\argo-fc_second.groups"));
			} catch (FileNotFoundException e3) {
				throw new ReportToUserException(e3);
			}
			
			// cluster(edges, authors);

			Author cluster0 = g.new Author("cluster0");
			if (settings.isCluster()) {

				List<Edge> collapsedEdges = new ArrayList<Edge>(edges.size());
				HashMap<Author, Edge> collapsing = new HashMap<Author, Edge>();

				for (Edge e : edges) {

					Author one = e.author;
					Author two = e.reply;

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
				Author mostActiveAuthor = plainAuthors.get(0);

				LinkedList<Author> todo = new LinkedList<Author>();
				Set<Author> result = new HashSet<Author>();
				todo.add(mostActiveAuthor);
				result.add(mostActiveAuthor);

				while (todo.size() > 0) {
					Author next = todo.removeFirst();
					for (Edge e : edges) {
						Author otherEnd = e.getOtherEnd(next);
						if (otherEnd != null && !result.contains(otherEnd)) {
							todo.add(otherEnd);
							result.add(otherEnd);
						}
					}
				}

				List<Edge> newEdges = new ArrayList<Edge>();
				for (Edge e : edges) {
					if (result.contains(e.getFrom())) {
						newEdges.add(e);
					}
				}

				edges = newEdges;
				plainAuthors = new ArrayList<Author>(result);
			}

			ProjectToPlot project = new ProjectToPlot();
			project.name = root.getMetaData("list");
			if ("gmane.comp.db.axion.devel".equals(project.name)) {
				project.name = "gmane.comp.lang.uml.argouml.devel";
			}
			project.members = projectMembers;

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

	private Author getAuthor(PrimaryDocument pd,
			HashMap<String, Author> authors, Graph g) {

		String authorsName = pd.getMetaData("from");

		if (authorsName == null)
			return null;

		authorsName = CUtils.cleanAuthor(authorsName);
		String authorsNameLookup = authorsName.replaceAll("\\s", "")
				.toLowerCase();

		Author author = authors.get(authorsNameLookup);
		if (author == null) {
			author = g.new Author(authorsName);
			authors.put(authorsNameLookup, author);
		}
		return author;
	}

	public List<Author> getProjectMembersAsClique(
			CMultimap<Pair<String, String>, PrimaryDocument> conversations,
			List<Author> plainAuthors) {

		List<Author> projectMembers = new ArrayList<Author>();

		a: for (Author a : plainAuthors) {
			for (Author b : projectMembers) {
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

	public Collection<List<Author>> getCommunitiesFromFile(List<Author> plainAuthors,
			File file) throws FileNotFoundException {

		HashMap<Integer, Integer> communities = getCommunityAssignment(file);

		AutoHashMap<Integer, List<Author>> map = AutoHashMap.getListHashMap();

		for (Author author : plainAuthors) {
			
			Integer community = communities.get((Integer)author.getId());
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
	public List<Author> getProjectMembersFromWeekActivity(
			List<Author> plainAuthors, double threshold) {

		List<Author> projectMembers = new ArrayList<Author>();

		for (Author a : plainAuthors) {
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
	public List<Author> getProjectMembersFromMonthActivity(
			List<Author> plainAuthors, double threshold) {

		List<Author> projectMembers = new ArrayList<Author>();

		for (Author a : plainAuthors) {
			if (a.months.getNonZeroBins() / 13.0 > threshold) {
				projectMembers.add(a);
				a.project = true;
			}
		}
		return projectMembers;
	}

	// private void cluster(List<Edge> edges, HashMap<String, Author> authors) {
	//		
	// int n = authors.size();
	//		
	//		
	//		
	//		
	// int[][] a = new int[n][n];
	//		
	//		
	// double q = 0.0;
	//		
	// EdgeWeightedGraph graph, bestGraph;
	//		
	// while (true){
	//			
	// CMultimap<Double, Edge> betweenness = graph.edgeBetweenness();
	//			
	// // Stop if no more edges in the graph
	// if (betweenness.size() == 0)
	// break;
	//			
	// // Remove the edges from the graph
	// for (Edge e : betweenness.lastEntry().getValue()){
	// graph = graph.remove(e);
	// }
	//
	// // Find best modularity
	// double newQ = graph.getModularity();
	// if (newQ > q){
	// bestGraph = graph;
	// q = newQ;
	// }
	// }
	//		
	//		
	//		
	//		
	//		
	//		
	//		
	//		
	//		
	// }

}
