package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule.SNAFileType;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
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
	SocialNetworkModule socialNetworkModule;

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

					SNAFileType type = dotFileChooser.getSelectedFileType();

					if (type == null) {
						throw new NullPointerException(
								"Must choose a file type for SNA Export");
					}

					socialNetworkModule.createNetwork(PrimaryDocument
							.getTreeItems(pd), type, null, dotFile, progress);

				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing thread statistics");

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
