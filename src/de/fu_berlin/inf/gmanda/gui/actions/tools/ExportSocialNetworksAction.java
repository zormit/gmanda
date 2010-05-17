package de.fu_berlin.inf.gmanda.gui.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule.SNAFileType;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.AbstractCodedString;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.gui.EnableComponentBridge;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Action to export all Social Networks in a Single Patch
 */
public class ExportSocialNetworksAction extends AbstractAction {

	@Inject
	CommonService commonService;

	@Inject
	VelocitySupport velocity;

	@Inject
	SocialNetworkModule socialNetworkModule;

	@Inject
	DotFileFileChooser dotFileChooser;

	ProjectProxy proxy;

	public ExportSocialNetworksAction(ProjectProxy proxy) {
		super("Export Social Networks Batch Operation");

		this.proxy = proxy;

		EnableComponentBridge.connectNonNull(this, proxy);

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	}

	public class ToString<T> {

		protected T t;

		protected String toString;

		public ToString(T t, String toString) {
			this.t = t;
			this.toString = toString;
		}

		public T getPayload() {
			return t;
		}

		public String toString() {
			return toString;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		commonService.run(new Runnable() {

			@SuppressWarnings("unchecked")
			public void run() {

				IProgress progress = commonService
						.getProgressBar("Calculate Project Statistics");
				try {
					Project project = proxy.getVariable();

					if (project == null)
						return;

					LinkedList<ToString<Code>> possibilities = new LinkedList<ToString<Code>>();
					possibilities.add(new ToString<Code>(CodedStringFactory
								.parseOne("exportSNA"), "Default Settings"));
					
					for (Entry<PrimaryDocument, Code> entry : project
							.getCodeModel().getAllCodesDeep("exportSNA")
							.entries()) {

						PrimaryDocument pd = entry.getKey();
						Code c = entry.getValue();

						possibilities.add(new ToString<Code>(c,
								AbstractCodedString.getFirstPropertyValueClean(
										c, "title", "untitled export setting")
										+ " defined in PD " + pd.getName()));
					}

					Code exportDefinition;
					if (possibilities.size() == 1) {
						exportDefinition = possibilities.getFirst().getPayload();
					} else {
						ToString<Code> choice = (ToString<Code>) JOptionPane
								.showInputDialog(commonService
										.getForegroundWindowOrNull(),
										"Which setting to use for export",
										"Social Network Scripted Export",
										JOptionPane.QUESTION_MESSAGE, null,
										possibilities.toArray(), possibilities
												.getFirst());

						if (choice == null)
							return;

						exportDefinition = choice.getPayload();
					}

					File dotFile = dotFileChooser.getSaveFile();
					progress.setScale(70);
					progress.start();

					if (dotFile == null)
						return;

					String prefix = FilenameUtils
							.getBaseName(dotFile.getName());
					String extension = FilenameUtils.getExtension(dotFile
							.getName());
					File directory = dotFile.getParentFile();

					SNAFileType type = dotFileChooser.getSelectedFileType();

					if (type == null) {
						throw new NullPointerException(
								"Must choose a file type for SNA Export");
					}

					type.exportSNA(socialNetworkModule, project, directory,
							prefix, extension, exportDefinition, progress);

				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing social networks");
	}
}
