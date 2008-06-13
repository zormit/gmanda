/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.fu_berlin.inf.gmanda.gui.preferences.PrimaryDocumentDirectoryProperty;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

/**
 * Action to manually download an mbox file from Gmane
 */
public class FetchGmaneListToFileAction extends AbstractAction {

	ProjectProxy projectProxy;

	ForegroundWindowProxy windowProxy;

	GmaneImporter importer;

	Configuration configuration;
	
	GmaneMboxFetcher fetcher;
	
	PrimaryDocumentDirectoryProperty primaryDocumentStorage;

	public FetchGmaneListToFileAction(ProjectProxy projectProxy, GmaneImporter importer,
		Configuration configuration, ForegroundWindowProxy windowProxy,
		GmaneMboxFetcher fetcher, PrimaryDocumentDirectoryProperty pds) {
		super("Fetch from Gmane to mbox-File...");

		this.projectProxy = projectProxy;
		this.windowProxy = windowProxy;
		this.importer = importer;
		this.configuration = configuration;
		this.fetcher = fetcher;
		this.primaryDocumentStorage = pds;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
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

				if (fc == null) {
					File location = primaryDocumentStorage.getValue();
					fc = new JFileChooser(location);
				}

				fc.setSelectedFile(new File(input + ".mbox"));
				int returnVal = fc.showSaveDialog(windowProxy.getVariable());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f;
					if (null != (f = fc.getSelectedFile())) {

						if (f.exists())
							if (JOptionPane.showConfirmDialog(windowProxy.getVariable(),
								"File exists. Do you want to overwrite?") != JOptionPane.YES_OPTION)
								return;

						try {
							fetcher.fetch(new NestableProgressMonitor(windowProxy.getAsFrameOrNull(),
								"Fetching '" + input + "' from Gmane..."), input, f, -1,
								-1);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(windowProxy.getVariable(),
								"Error fetching mailinglist from Gmane " + e.getMessage(),
								"Gmane Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}).start();
	}
}
