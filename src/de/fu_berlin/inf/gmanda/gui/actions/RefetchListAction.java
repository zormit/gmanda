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
import de.fu_berlin.inf.gmanda.gui.misc.StorageFileChooser;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class RefetchListAction extends AbstractAction {

	PrimaryDocumentTree tree;

	ForegroundWindowProxy windowProxy;
	
	ProjectProxy projectProxy;

	CommonService commonService;
	
	StorageFileChooser storage;
	
	GmaneMboxFetcher fetcher;
	
	GmaneImporter importer;

	public RefetchListAction(ProjectProxy project, ForegroundWindowProxy windowProxy,
		PrimaryDocumentTree tree, CommonService progress,
		StorageFileChooser storage, GmaneMboxFetcher fetcher, GmaneImporter importer) {
		super("Refetch List...");
		this.tree = tree;
		this.windowProxy = windowProxy;
		this.commonService = progress;
		this.projectProxy = project;
		this.storage = storage;
		this.fetcher = fetcher;
		this.importer = importer;
		

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
				
				while (pd.getParent() != null)
					pd = pd.getParent();
				
				if (pd.getMetaData().get("list") == null) {
					String list = JOptionPane.showInputDialog(windowProxy.getVariable(),
						"Enter List Name to Fetch", pd.getListGuess());
					if (list == null)
						return;

					pd.getMetaData().put("list", list);
				}
				
				projectProxy.getVariable().refetch(pd, commonService.getProgressBar("Refetching list"), fetcher, importer);
			}
		}).start();
	}

};
