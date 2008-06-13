package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class BasicStatisticalReportAction extends AbstractAction {
	CommonService commonService;
	GmaneFacade facade;
	de.fu_berlin.inf.gmanda.proxies.ProjectProxy proxy;
	
	public BasicStatisticalReportAction(ProjectProxy proxy, GmaneFacade facade, CommonService commonService) {
		super("Calculate project statistics");

		this.proxy = proxy;
		this.commonService = commonService;
		this.facade = facade;
		
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}

	public void actionPerformed(ActionEvent arg0) {
		commonService.run(new Runnable() {
			public void run() {
				IProgress progress = commonService.getProgressBar("Calculate Project Statistics");	
				facade.printStatistics(proxy.getVariable(), progress);
			}
		}, "Error creating statistics");
	}
}
