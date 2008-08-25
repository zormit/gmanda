/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class DeletePrimaryDocumentAction extends AbstractAction {

	ProjectProxy project;
	
	PrimaryDocumentTree tree;

	ForegroundWindowProxy windowProxy;

	public DeletePrimaryDocumentAction(ProjectProxy project, ForegroundWindowProxy windowProxy,
		PrimaryDocumentTree tree) {
		super("Delete...");
		this.project = project;
		this.tree = tree;
		this.windowProxy = windowProxy;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_D));
	}

	public void actionPerformed(ActionEvent arg0) {

		new Thread(new Runnable() {
			public void run() {

				TreePath path = tree.getSelectionPath();

				if (path == null)
					return;

				Object o = path.getLastPathComponent();

				if (o == null || !(o instanceof PrimaryDocument))
					return;

				PrimaryDocument pd = (PrimaryDocument) o;
				
				int total = 0;
				int codes = 0;
				for (PrimaryDocument child : PrimaryDocument.getTreeWalker(pd)){
					total++;
					if (child.getCodeAsString() != null)
						codes++;
				}
				
				if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
					windowProxy.getAsFrameOrNull(), 
					String.format("Are you sure about deleting %d documents, %d of which have attached codes?", total, codes))){
					project.getVariable().removePD(pd);
				}
			}
		}).start();
	}

};
