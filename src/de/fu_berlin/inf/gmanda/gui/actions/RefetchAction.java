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
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class RefetchAction extends AbstractAction {

	PrimaryDocumentTree tree;

	ForegroundWindowProxy windowProxy;

	CommonService commonService;
	
	GmaneMboxFetcher fetcher;

	public RefetchAction(ProjectProxy project, ForegroundWindowProxy windowProxy,
		PrimaryDocumentTree tree, CommonService progress, GmaneMboxFetcher fetcher) {
		super("Refetch Message...");
		this.tree = tree;
		this.windowProxy = windowProxy;
		this.commonService = progress;
		this.fetcher = fetcher;
		
		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
	}

	public void actionPerformed(ActionEvent arg0) {

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

		IProgress progress = commonService.getProgressBar("Refetching messages", 1);

		try {
			pd.refetch(fetcher);
		} finally {
			progress.done();
		}
	}

};
