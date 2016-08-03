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
import javax.swing.KeyStroke;

import org.apache.commons.io.FilenameUtils;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.dialogs.ImportFromGmaneDialog;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.MBoxFileChooser;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter.ImportSettings;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class ImportFromMboxAction extends AbstractAction {

	ProjectProxy projectProxy;

	GmaneImporter importer;

	CommonService cs;
	
	MBoxFileChooser fileChooser;
	
	public ImportFromMboxAction(
			ProjectProxy projectProxy, 
			GmaneImporter importer, 
			CommonService cs,
			MBoxFileChooser fileChooser) {
		super("Import Emails from an MBox archive");

		this.projectProxy = projectProxy;
		this.importer = importer;
		this.cs = cs;
		this.fileChooser = fileChooser;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
	}

	ImportFromGmaneDialog importDialog;

	JFileChooser fc;

	public void actionPerformed(ActionEvent arg0) {

		// Run in Background...
		cs.run(new Runnable() {
			public void run() {
				loadEmails();
			}
		}, "Error while Fetching from Gmane");
	}

	public void loadEmails() {

		Project project = projectProxy.getVariable();

		if (project == null)
			return;
		
		File mboxFile = fileChooser.getOpenFile();
		if (mboxFile == null)
			return;

		if (importDialog == null) {
			importDialog = new ImportFromGmaneDialog(cs.getForegroundWindowOrNull(), true);
		}
		importDialog.setListName(FilenameUtils.getBaseName(mboxFile.getName()));

		importDialog.setVisible(true);

		if (importDialog.getReturnStatus() == ImportFromGmaneDialog.RET_OK) {
			String input = importDialog.getListName();

			if (input == null || input.trim().length() == 0)
				return;

			NestableProgressMonitor pm = new NestableProgressMonitor(
				cs.getForegroundWindowOrNull(), "Importing from mbox file '" + mboxFile.getName() + "...");

			pm.setScale(100);
			pm.start();

			try {
				// Fetch from Gmane to temporary mbox file
				ImportSettings settings = new ImportSettings();
				settings.listName = input;
				settings.mboxFile = mboxFile;

				switch (importDialog.getFetchType()) {
				case BYDATE: {
					settings.startDate = importDialog.getStartDate();
					settings.endDate = importDialog.getEndDate();
					break;
				}
				case BYID: {
					settings.rangeStart = importDialog.getStartId();
					settings.rangeEnd = importDialog.getEndId();
					break;
				}
				default:
					settings.rangeStart = -1;
					settings.rangeEnd = -1;
				}
				settings.useIDsFromEmails = importDialog.getUseIDsFromEmails();
				
				// Read mbox file
				List<PrimaryDocumentData> imported = importer.importPrimaryDocuments(pm, settings);

				project.addRootPDDs(imported);

			} catch (Exception e) {
				throw new ReportToUserException(e);
			} finally {
				pm.done();
			}
		}
	}
}
