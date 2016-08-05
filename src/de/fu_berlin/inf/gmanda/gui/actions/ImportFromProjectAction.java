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

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.ProjectFileChooser;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.MyXStream;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.ProjectData;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class ImportFromProjectAction extends AbstractAction {

	ProjectProxy projectProxy;

	GmaneImporter importer;

	CommonService cs;
	
	ProjectFileChooser fileChooser;

	MyXStream stream;
	
	public ImportFromProjectAction(
			ProjectProxy projectProxy, 
			GmaneImporter importer, 
			CommonService cs,
			ProjectFileChooser fileChooser,
			MyXStream stream) {
		super("Import Codes from another project file");

		this.projectProxy = projectProxy;
		this.importer = importer;
		this.cs = cs;
		this.fileChooser = fileChooser;
		this.stream = stream;

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

		// Run in Background...
		cs.run(new Runnable() {
			public void run() {
				loadCodes();
			}
		}, "Error while loading Codes");
	}

	public void loadCodes() {

		Project project = projectProxy.getVariable();

		if (project == null)
			return;
		
		File projectFile = fileChooser.getOpenFile();
		if (projectFile == null)
			return;
		
		projectFile.getName();

		NestableProgressMonitor pm = new NestableProgressMonitor(
			cs.getForegroundWindowOrNull(), "Importing from project file '" + projectFile.getName() + "...");

		pm.setScale(100);
		pm.start();

		try {
			ProjectData data = stream.fromXML(projectFile, pm);

			project.mergeDataToExistingPDs(data.rootDocuments);

		} catch (Exception e) {
			throw new ReportToUserException(e);
		} finally {
			pm.done();
		}
	}
}
