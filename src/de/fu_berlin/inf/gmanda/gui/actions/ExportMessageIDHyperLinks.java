/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class ExportMessageIDHyperLinks extends AbstractAction {

	ProjectProxy project;

	PrimaryDocumentTree tree;

	public ExportMessageIDHyperLinks(ProjectProxy project,
			PrimaryDocumentTree tree) {
		super("Export MessageID LaTeX Code...");
		this.project = project;
		this.tree = tree;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});
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

				for (PrimaryDocument child : PrimaryDocument.getTreeWalker(pd)) {

					String sid = child.getMetaData("id");
					if (sid == null)
						continue;

					int id;
					try {
						id = Integer.parseInt(sid);
					} catch (NumberFormatException e) {
						continue;
					}

					String mid = child.getMetaData("mid");
					String list = child.getListGuess();
					if (mid == null || list == null)
						continue;

					System.out.println("\\@namedef{" + list + "_" + id+"}{"+mid.replace("<", "").replace(">", "").replace("%", "\\%").replace("@", "\\%40")+"}");
				}
			}
		}).start();
	}
};
