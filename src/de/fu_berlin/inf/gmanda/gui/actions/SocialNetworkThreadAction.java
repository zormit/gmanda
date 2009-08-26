package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FileUtils;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
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

					Map<String, Object> data = computeSocialNetwork(pd,
							progress);

					// Run template engine
					String dotString = velocitySupport.run(data, dotFile.getName()
							.endsWith(".dot") ? "graphDOT" : "graphML");

					try {
						FileUtils.writeStringToFile(dotFile, dotString,
								"latin1");
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

	

	

	public static class Author implements Comparable<Author> {
		int emailsWritten;
		String name;

		boolean project = false;

		public Author(String name) {
			this.setName(name);
		}

		public void setEmailsWritten(int emailsWritten) {
			this.emailsWritten = emailsWritten;
		}

		public int getEmailsWritten() {
			return emailsWritten;
		}

		public String getMultiLineName() {
			return getName().replaceAll("\\s+", "\\\\n");
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public double getWeight() {
			return Math.sqrt(emailsWritten) / 2.0;
		}

		public String getColor() {
			return "blue";
		}

		@Override
		public int compareTo(Author arg0) {
			return this.emailsWritten - arg0.emailsWritten;
		}
	}

	public static class Edge {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((author == null) ? 0 : author.hashCode());
			result = prime * result + ((reply == null) ? 0 : reply.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Edge))
				return false;
			Edge other = (Edge) obj;
			if (author == null) {
				if (other.author != null)
					return false;
			} else if (!author.equals(other.author))
				return false;
			if (reply == null) {
				if (other.reply != null)
					return false;
			} else if (!reply.equals(other.reply))
				return false;
			return true;
		}

		public Edge(String p, String v, int size) {
			this.author = p;
			this.reply = v;
			this.weight = size;
		}

		String author;

		String reply;

		int weight;

		public String getAuthor() {
			return author;
		}

		public String getReply() {
			return reply;
		}

		public int getWeight() {
			return weight;
		}
	}

	public class ProjectToPlot {

		public String name;

		List<Author> members;

		public String getName() {
			return name;
		}

		public List<Author> getMembers() {
			return members;
		}
	}

	public Map<String, Object> computeSocialNetwork(PrimaryDocument root,
			IProgress pm) {

		pm.setScale(100);
		pm.start();

		try {

			Comparator<Pair<String, String>> pComp = Pair.pvCompare();

			CMultimap<Pair<String, String>, PrimaryDocument> conversations = new CMultimap<Pair<String, String>, PrimaryDocument>(
					pComp);

			HashMap<String, Author> authors = new HashMap<String, Author>();

			int numberOfPds = PrimaryDocument.getTreeWalker(root).size();
			pm.work(5);

			System.out.println("# of PDs: " + numberOfPds);

			{ // Build author map
				IProgress pToFetch = pm.getSub(75);
				pToFetch.setScale(numberOfPds);
				pToFetch.start();

				for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(root)) {
					String authorsName = pd.getMetaData("from");

					if (authorsName == null) {
						continue;
					}

					authorsName = CUtils.cleanAuthor(authorsName);

					Author author = authors.get(authorsName);
					if (author == null) {
						author = new Author(authorsName);
						authors.put(authorsName, author);
					}
					author.emailsWritten++;

					PrimaryDocument parent = pd.getParent();
					String parentAuthor;
					if (parent == null) {
						continue;
					} else {
						parentAuthor = parent.getMetaData("from");

						if (parentAuthor == null) {
							continue;
						}
						parentAuthor = CUtils.cleanAuthor(parentAuthor);
					}

					// if (parentAuthor.compareTo(authorsName) < 0){
					// String s = authorsName;
					// authorsName = parentAuthor;
					// parentAuthor = s;
					// }

					conversations.put(new Pair<String, String>(parentAuthor,
							authorsName), pd);
					pToFetch.work(1);
				}
				pToFetch.done();
			}

			List<Author> plainAuthors = new ArrayList<Author>(authors.values());
			Collections.sort(plainAuthors);
			Collections.reverse(plainAuthors);
			List<Author> projectMembers = new ArrayList<Author>();

			a: for (Author a : plainAuthors) {
				for (Author b : projectMembers) {
					if (!(conversations.containsKey(new Pair<String, String>(
							a.name, b.name)) && conversations
							.containsKey(new Pair<String, String>(b.name,
									a.name)))) {
						continue a;
					}
				}
				projectMembers.add(a);
				a.project = true;
			}

			System.out.println("# of Authors: " + conversations.size());

			List<Edge> edges = new ArrayList<Edge>(conversations.size());

			{ // Convert
				IProgress pToFetch = pm.getSub(10);
				pToFetch.setScale(conversations.size());
				pToFetch.start();

				for (Entry<Pair<String, String>, Collection<PrimaryDocument>> entry : conversations
						.entrySet()) {

					edges.add(new Edge(entry.getKey().p, entry.getKey().v,
							entry.getValue().size()));
					pToFetch.work(1);
				}
				pToFetch.done();
			}

			boolean collapseEdges = false;
			if (collapseEdges == true) {

				List<Edge> collapsedEdges = new ArrayList<Edge>(edges.size());
				HashMap<Author, Edge> collapsing = new HashMap<Author, Edge>();

				for (Edge e : edges) {

					Author one = authors.get(e.author);
					Author two = authors.get(e.reply);

					if (one.project) {
						if (two.project) {
							collapsedEdges.add(e);
						} else {
							Edge e2 = collapsing.get(two);
							if (e2 == null) {
								e2 = new Edge("cluster0", two.name, 0);
								collapsing.put(two, e2);
								collapsedEdges.add(e2);
							}
							e2.weight += e.weight;
						}
					} else {
						if (two.project) {
							Edge e2 = collapsing.get(one);
							if (e2 == null) {
								e2 = new Edge(one.name, "cluster0", 0);
								collapsing.put(one, e2);
								collapsedEdges.add(e2);
							}
							e2.weight += e.weight;
						} else {
							collapsedEdges.add(e);
						}
					}
				}
				edges = collapsedEdges;
			}

			ProjectToPlot project = new ProjectToPlot();
			project.name = root.getMetaData("list");
			project.members = projectMembers;

			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("edges", edges);
			result.put("authors", plainAuthors);
			result.put("project", project);
			return result;

		} finally {
			pm.done();
		}
	}

}
