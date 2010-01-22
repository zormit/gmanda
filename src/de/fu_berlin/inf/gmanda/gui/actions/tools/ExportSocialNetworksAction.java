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
					
					
					String prefix = FilenameUtils.getBaseName(dotFile.getName());
					String extension = FilenameUtils.getExtension(dotFile.getName());
					File directory = dotFile.getParentFile();

					SNAFileType type = dotFileChooser.getSelectedFileType();

					if (type == null) {
						throw new NullPointerException(
								"Must choose a file type for SNA Export");
					}
					
					
//					IProgress subProgress = progress.getSub(70, ProgressStyle.ROTATING);
//					subProgress.setScale(120);
//					for (int periphery : new int[]{1,2,3}){
//						for (int core : new int[]{4,5,6,7,8,9,10,11,12}){
//							for (PrimaryDocument pd : project.getPrimaryDocuments()){
//								socialNetworkModule.createNetwork(pd, type, type.getDefaultSettings().setClusterBuilder(new MonthClusterBuilder(periphery,core)), 
//										new File(directory, prefix + periphery + "-" + core + "-" + pd.getShortListGuess() + "." + extension), subProgress.getSub(1));
//							}
//						}
//					}
					
					for (PrimaryDocument pd : project.getPrimaryDocuments()){
						socialNetworkModule.createNetwork(pd, type, type.getDefaultSettings().setClusterBuilder(new MonthClusterBuilder(2, 8)), 
								new File(directory, prefix + "2-8-" + pd.getShortListGuess() + "." + extension), progress.getSub(70));
					}
					
				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}

			}
		}, "Error in computing social networks");
	}
	

}
