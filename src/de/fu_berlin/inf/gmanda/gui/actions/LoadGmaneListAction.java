/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.dialogs.ImportFromGmaneDialog;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter;
import de.fu_berlin.inf.gmanda.imports.GmaneMboxFetcher;
import de.fu_berlin.inf.gmanda.imports.GmaneImporter.ImportSettings;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class LoadGmaneListAction extends AbstractAction {

	ProjectProxy projectProxy;

	GmaneImporter importer;

	GmaneMboxFetcher fetcher;

	CommonService cs;

	public LoadGmaneListAction(ProjectProxy projectProxy, GmaneImporter importer, CommonService cs,
		GmaneMboxFetcher fetcher) {
		super("Add Emails from a Gmane Mailing List...");

		this.projectProxy = projectProxy;
		this.importer = importer;
		this.cs = cs;
		this.fetcher = fetcher;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_G));
	}

	ImportFromGmaneDialog dialog;

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

		Project p = projectProxy.getVariable();

		if (p == null)
			return;

		if (dialog == null) {
			dialog = new ImportFromGmaneDialog(cs.getForegroundWindowOrNull(), true);
		}

		dialog.setVisible(true);

		if (dialog.getReturnStatus() == ImportFromGmaneDialog.RET_OK) {
			String input = dialog.getListName();

			if (input == null || input.trim().length() == 0)
				return;

			if (!input.startsWith("gmane."))
				input = "gmane." + input;

			if (input.endsWith(".mbox"))
				input = input.substring(0, input.length() - ".mbox".length());

			NestableProgressMonitor pm = new NestableProgressMonitor(
				cs.getForegroundWindowOrNull(), "Fetching '" + input + "' from Gmane...");

			pm.setScale(100);
			pm.start();

			try {
				// Fetch from Gmane to temporary mbox file
				ImportSettings settings = new ImportSettings();
				settings.listName = input;
				settings.mboxFile = null; // Fetch to temporary file

				switch (dialog.getFetchType()) {
				case BYDATE: {
					settings.startDate = dialog.getStartDate();
					settings.endDate = dialog.getEndDate();
					
					settings.rangeStart = fetcher.getEstimatedStartEmail(pm.getSub(5), input,
						dialog.getStartDate());
					settings.rangeEnd = fetcher.getEstimatedEndEmail(pm.getSub(5), input,
						settings.rangeStart, dialog.getEndDate());
					break;
				}
				case BYID: {
					settings.rangeStart = dialog.getStartId();
					settings.rangeEnd = dialog.getEndId();
					break;
				}
				default:
					settings.rangeStart = -1;
					settings.rangeEnd = -1;
				}

				fetcher.fetch(pm.getSub(45), settings);

				// Read mbox file
				List<PrimaryDocumentData> imported = importer.importPrimaryDocuments(pm.getSub(45),
					settings);

				p.addRootPDDs(imported);

				settings.mboxFile.delete();

			} catch (Exception e) {
				throw new ReportToUserException(e);
			} finally {
				pm.done();
			}
		}
	}
}
