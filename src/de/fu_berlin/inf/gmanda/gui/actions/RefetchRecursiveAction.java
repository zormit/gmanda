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
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class RefetchRecursiveAction extends AbstractAction {

	PrimaryDocumentTree tree;

	ForegroundWindowProxy windowProxy;

	CommonService commonService;
	
	GmaneMboxFetcher fetcher;

	public RefetchRecursiveAction(ProjectProxy project, ForegroundWindowProxy windowProxy,
		PrimaryDocumentTree tree, CommonService progress, GmaneMboxFetcher fetcher) {
		super("Refetch Recursivly...");
		this.tree = tree;
		this.windowProxy = windowProxy;
		this.commonService = progress;
		this.fetcher = fetcher;

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
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

				if (pd.getMetaData().get("list") == null) {
					String list = JOptionPane.showInputDialog(windowProxy.getVariable(),
						"Enter List Name to Fetch", pd.getListGuess());
					if (list == null)
						return;

					pd.getMetaData().put("list", list);
				}

				pd.refetchRecursive(commonService.getProgressBar("Refetching messages"), fetcher);
			}
		}).start();
	}

};
