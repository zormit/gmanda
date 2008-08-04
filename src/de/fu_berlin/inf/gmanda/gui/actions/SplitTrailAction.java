package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.trail.TrailManager;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class SplitTrailAction extends AbstractAction {

	ProjectProxy projectProxy;

	CommonService common;
	
	TrailManager trailManager;

	public SplitTrailAction(TrailManager trailManager, ProjectProxy projectProxy, CommonService common) {
		super("Start new Trail...");

		this.projectProxy = projectProxy;
		this.common = common;
		this.trailManager = trailManager;

		this.projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}

	public void actionPerformed(ActionEvent arg0) {

		common.runSync(
			new Runnable() {
				public void run() {
					trailManager.split();
				}
			}, "Could not split trail");
	}

}
