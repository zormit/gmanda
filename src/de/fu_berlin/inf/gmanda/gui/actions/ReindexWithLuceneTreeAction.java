/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.search.LuceneFacade;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class ReindexWithLuceneTreeAction extends AbstractAction {

	PrimaryDocumentTree tree;

	CommonService commonService;

	LuceneFacade lucene;

	public ReindexWithLuceneTreeAction(ProjectProxy project,
			ForegroundWindowProxy windowProxy, PrimaryDocumentTree tree,
			CommonService progress, LuceneFacade lucene) {
		super("Reindex via Lucene...");
		
		this.tree = tree;
		this.commonService = progress;
		this.lucene = lucene;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});
	}

	public void actionPerformed(ActionEvent arg0) {

		commonService.run(new Runnable() {
			public void run() {
				TreePath path = tree.getSelectionPath();

				if (path == null)
					return;

				Object o = path.getLastPathComponent();

				if (o == null || !(o instanceof PrimaryDocument))
					return;

				lucene.reindex(PrimaryDocument.getTreeWalker((PrimaryDocument) o));
			}
		}, "Reindexing with Lucene failed");
	}
}
