/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class LoadGmaneListAction extends AbstractAction {

	ProjectProxy projectProxy;

	GmaneImporter importer;

	ForegroundWindowProxy windowProxy;
	
	GmaneMboxFetcher fetcher;
	
	public LoadGmaneListAction(ProjectProxy projectProxy, GmaneImporter importer,
		ForegroundWindowProxy windowProxy,
		GmaneMboxFetcher fetcher) {
		super("Add Emails from a Gmane Mailing List...");

		this.projectProxy = projectProxy;
		this.importer = importer;
		this.windowProxy = windowProxy;
		this.fetcher = fetcher;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
	}

	JFileChooser fc;

	public void actionPerformed(ActionEvent arg0) {

		final Project p = projectProxy.getVariable();

		if (p == null)
			return;

		// Run in Background...
		new Thread(new Runnable() {
			public void run() {

				// TODO: Show better dialog that allows partial fetch
				String input = JOptionPane.showInputDialog(windowProxy.getVariable(),
					"Enter List Name to Fetch");

				if (input == null || input.trim().length() == 0)
					return;

				if (!input.startsWith("gmane."))
					input = "gmane." + input;
				
				if (input.endsWith(".mbox"))
					input = input.substring(0, input.length() - ".mbox".length());

				NestableProgressMonitor pm = new NestableProgressMonitor(windowProxy
					.getAsFrameOrNull(), "Fetching '" + input + "' from Gmane...");
				pm.setScale(100);
				pm.start();

				try {
					// Fetch from Gmane to temporary mbox file
					File target = fetcher.fetchToTemp(pm.getSub(45), input, -1, -1);

					// Read mbox file
					List<PrimaryDocumentData> imported = importer.importPrimaryDocuments(input, 1,
						target, pm.getSub(45), false);

					p.addRootPDDs(imported);
					
					target.delete();

				} catch (Exception e) {
					JOptionPane.showMessageDialog(windowProxy.getVariable(),
						"Error fetching Mailing list:\n" + e.getMessage(),
						"Error while Fetching from Gmane", JOptionPane.ERROR_MESSAGE);
				} finally {
					pm.done();
				}
			}
		}).start();
	}
}
