package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.SVGScreenshotTaker;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class ApplicationScreenshotAction extends AbstractAction {

	@Inject
	CommonService commonService;
	
	@Inject
	SVGScreenshotTaker screenshotTaker;
	
	public ApplicationScreenshotAction(ProjectProxy project) {
		super("Take SVG-Screenshot (experimental)");

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
				JFrame f = commonService.getForegroundWindowOrNull();
				
				if (f != null)
					screenshotTaker.screenshotToFile(f.getRootPane());
			}
		}, "Error in taking screenshot");
	}
}
