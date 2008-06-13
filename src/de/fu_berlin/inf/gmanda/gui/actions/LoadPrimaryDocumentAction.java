/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.preferences.PrimaryDocumentDirectoryProperty;
import de.fu_berlin.inf.gmanda.imports.PlainTextImporter;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class LoadPrimaryDocumentAction extends AbstractAction {

	ProjectProxy projectProxy;

	PlainTextImporter importer;

	PrimaryDocumentDirectoryProperty configuration;

	public LoadPrimaryDocumentAction(ProjectProxy projectProxy, PlainTextImporter importer,
		PrimaryDocumentDirectoryProperty configuration) {
		super("Add a text file...");

		this.projectProxy = projectProxy;
		this.importer = importer;
		this.configuration = configuration;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
	}

	public void actionPerformed(ActionEvent arg0) {

		Project p = projectProxy.getVariable();

		if (p == null)
			return;

		JFileChooser fc = new JFileChooser(configuration.getValue());

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			File f = fc.getSelectedFile();

			configuration.setValue(f.getAbsoluteFile().getParentFile());

			p.addRootPDDs(importer.importPrimaryDocuments(f.getAbsolutePath(),
				new NestableProgressMonitor(null, "Loading Primary Document")));
		}
	}
}
