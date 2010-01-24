package de.fu_berlin.inf.gmanda.gui.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.io.FilenameUtils;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.graph.MonthClusterBuilder;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule;
import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule.SNAFileType;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.DotFileFileChooser;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.gui.EnableComponentBridge;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.IProgress.ProgressStyle;

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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		commonService.run(new Runnable() {

			public void run() {

				IProgress progress = commonService
						.getProgressBar("Calculate Project Statistics");
				try {
					Project project = proxy.getVariable();

					if (project == null)
						return;

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

					computerSNAs(progress, project, prefix, extension,
							directory, type);

				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing social networks");
	}

	protected void computerSNAs(IProgress progress, Project project,
			String prefix, String extension, File directory, SNAFileType type) {
		IProgress subProgress = progress.getSub(70, ProgressStyle.ROTATING);
		subProgress.setScale(120);

		for (int periphery : new int[] { 2 }) {
			for (int core : new int[] { 8 }) {

				for (PrimaryDocument pd : project.getPrimaryDocuments()) {
					if (pd.getShortListGuess() == null)
						continue;

					File targetFile = new File(directory, prefix + periphery
							+ "-" + core + "-"
							+ pd.getShortListGuess().replaceAll("\\.", "-")
							+ "." + extension);

					socialNetworkModule.createNetwork(pd, type, type
							.getDefaultSettings().setClusterBuilder(
									new MonthClusterBuilder(periphery, core)),
							targetFile, subProgress.getSub(1));

					// // Max Edge NO Aggregate
					// socialNetworkModule.createNetwork(pd, type, type
					// .getDefaultSettings().setClusterBuilder(
					// new MaxEdgePeripheryBuilder(periphery, core)),
					// targetFile, subProgress.getSub(1));

					// // Max Edge Aggregate!
					// socialNetworkModule.createNetwork(pd, type, type
					// .getDefaultSettings().setClusterBuilder(
					// new MaxEdgeAggregatePeripheryBuilder(periphery, core)),
					// targetFile, subProgress.getSub(1));
				}
			}
		}
	}

}
