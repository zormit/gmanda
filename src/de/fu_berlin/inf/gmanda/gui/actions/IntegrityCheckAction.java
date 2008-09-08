/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class IntegrityCheckAction extends AbstractAction {

	ProjectProxy projectProxy;

	CommonService common;

	public IntegrityCheckAction(ProjectProxy projectProxy, CommonService common) {
		super("Integrity Check...");

		this.projectProxy = projectProxy;
		this.common = common;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
	}

	JFileChooser fc;

	public void actionPerformed(ActionEvent arg0) {

		// Run in Background...
		common.run(new Runnable() {
			public void run() {
				integrityCheck();
			}
		}, "Could not execute trail");
	}

	public void integrityCheck() {

		Project p = projectProxy.getVariable();
		if (p == null)
			return;


		IProgress progressbar = common.getProgressBar("Integrity Check...");
		progressbar.setScale(100);
		progressbar.start();

		try {
			// Check that all Documents have a filename
			for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(p.getPrimaryDocuments())) {
				if (pd.getFilename() == null)
					System.out.println("Missing filename: " + pd);
			}
			progressbar.work(5);
			if (progressbar.isCanceled())
				return;
		} finally {
			progressbar.done();
		}

	}
}
