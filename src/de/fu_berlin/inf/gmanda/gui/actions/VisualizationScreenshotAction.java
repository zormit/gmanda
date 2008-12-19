package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.SVGScreenshotTaker;
import de.fu_berlin.inf.gmanda.gui.visualisation.VisualizationCanvas;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class VisualizationScreenshotAction extends AbstractAction {

	@Inject
	CommonService commonService;
	
	@Inject
	VisualizationCanvas canvas;

	@Inject
	SVGScreenshotTaker screenshotTaker;

	public VisualizationScreenshotAction(ProjectProxy project) {
		super("Screenshot");

		project.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}

	public void actionPerformed(ActionEvent arg0) {
		commonService.run(new Runnable() {
			public void run() {
				screenshotTaker.screenshotToFile(canvas.getCanvas());
			}
		}, "Error in taking screenshot");
	}
}
