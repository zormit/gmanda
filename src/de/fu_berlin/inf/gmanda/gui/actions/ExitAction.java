/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.GmandaMain;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.VetoableVariableProxy;

/**
 * Stops the application
 * 
 * @author oezbek
 * 
 */
public class ExitAction extends AbstractAction {

	VetoableVariableProxy<Project> projectProxy;

	UndoManagement undoManager;

	Configuration configuration;
	
	GmandaMain application;

	public ExitAction(
		ProjectProxy projectProxy,
		UndoManagement manager, 
		Configuration configuration,
		GmandaMain application) {
		super("Exit");

		this.projectProxy = projectProxy;
		this.undoManager = manager;
		this.configuration = configuration;
		this.application = application;

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
	}

	public void actionPerformed(ActionEvent arg0) {
		exit();
	}

	public void exit() {
		Project p = projectProxy.getVariable();

		if (p != null) {
			// Try to close the current project
			if (!projectProxy.setVariable(null)) {
				return;
			}

			if (p.getSaveFile() != null) {
				configuration.setProperty("lastProject", p.getSaveFile().getAbsolutePath());
			}
		}

		application.stopApplication();
	}
}
