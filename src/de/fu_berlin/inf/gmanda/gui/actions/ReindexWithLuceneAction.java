/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.gui.search.LuceneFacade;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class ReindexWithLuceneAction extends AbstractAction {

	ProjectProxy project;

	LuceneFacade lucene;

	public ReindexWithLuceneAction(ProjectProxy project, ForegroundWindowProxy windowProxy,
		PrimaryDocumentTree tree, LuceneFacade lucene) {
		super("Update Lucene search index...");

		this.project = project;
		this.lucene = lucene;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	}

	public void actionPerformed(ActionEvent arg0) {
		new Thread(new Runnable() {
			public void run() {
				lucene.reindex(project.getVariable());
			}
		}).start();
	}

};
