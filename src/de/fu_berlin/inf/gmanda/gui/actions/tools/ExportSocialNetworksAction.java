package de.fu_berlin.inf.gmanda.gui.actions.tools;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.io.FilenameUtils;
import org.colorbrewer.ColorBrewerPalettes;
import org.picocontainer.annotations.Inject;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;
import de.fu_berlin.inf.gmanda.gui.graph.Coloration;
import de.fu_berlin.inf.gmanda.gui.graph.DefaultClusterBuilder;
import de.fu_berlin.inf.gmanda.gui.graph.ExportSettings;
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

					computeSNAs(progress, project, prefix, extension,
							directory, type);

				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing social networks");
	}

	protected void computeSNAprogression(IProgress progress, Project project,
			String prefix, String extension, File directory, SNAFileType type) {
		IProgress subProgress = progress.getSub(70, ProgressStyle.ROTATING);
		subProgress.setScale(120);

		for (PrimaryDocument pd : project.getPrimaryDocuments()) {

			if (pd.getShortListGuess() == null)
				continue;

			ExportSettings settings = SNAFileType.DOT.getDefaultSettings();

			// 1 - Show plain graph, bidirectional and without node size
			File targetFile = buildFile(directory, prefix, pd, extension, "-",
					"-1");
			Predicate<Node> alwaysTrue = Predicates.alwaysTrue();
			
			settings = settings.setCluster(false).setCollapseEdges(false)
					.setUndirected(false).setScaleNodes(false).setScaleEdges(
							false).setColorPalette(new String[] { "#ffffff" })
					.setFontColorPalette(new String[] { "#000000" })
					.setClusterBuilder(new DefaultClusterBuilder(alwaysTrue, "everybody", "#000000"));
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));

			// 2 - Scale nodes
			targetFile = buildFile(directory, prefix, pd, extension, "-", "-2");
			settings = settings.setScaleNodes(true);
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));

			// 3 - Make vertices unidirectional and add width
			targetFile = buildFile(directory, prefix, pd, extension, "-", "-3");
			settings = settings.setUndirected(true).setScaleEdges(true);
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));

			// 4 - Color nodes
			targetFile = buildFile(directory, prefix, pd, extension, "-", "-4");
			settings = settings.setColoration(Coloration.MONTH)
					.setColorPalette(ColorBrewerPalettes.ylorrd6)
					.setFontColorPalette(ColorBrewerPalettes.ylorrd6Font);
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));

			// 5 - Layout core separately
			targetFile = buildFile(directory, prefix, pd, extension, "-", "-5");
			settings = settings.setCluster(true).setClusterBuilder(new MonthClusterBuilder(2,8));
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));

			// 6 - Collapse nodes
			targetFile = buildFile(directory, prefix, pd, extension, "-", "-6");
			settings = settings.setCollapseEdges(true);
			socialNetworkModule.createNetwork(pd, type, settings, targetFile,
					subProgress.getSub(1));
		}
	}

	protected void computeSNAs(IProgress progress, Project project,
			String prefix, String extension, File directory, SNAFileType type) {
		IProgress subProgress = progress.getSub(70, ProgressStyle.ROTATING);
		subProgress.setScale(120);

		for (int periphery : new int[] { 2 }) {
			for (int core : new int[] { 8 }) {

				for (PrimaryDocument pd : project.getPrimaryDocuments()) {
					if (pd.getShortListGuess() == null)
						continue;

					File targetFile = buildFile(directory, prefix, pd,
							extension, periphery + "-" + core + "-", "");

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

	protected File buildFile(File directory, String prefix, PrimaryDocument pd,
			String extension, String preID, String postID) {
		File targetFile = new File(directory, prefix + preID
				+ pd.getShortListGuess().replaceAll("\\.", "-") + postID + "."
				+ extension);
		return targetFile;
	}

}
