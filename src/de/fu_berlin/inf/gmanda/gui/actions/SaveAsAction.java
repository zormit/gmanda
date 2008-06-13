package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.gui.EnableComponentBridge;
import de.fu_berlin.inf.gmanda.util.gui.NotNullConverter;

public class SaveAsAction extends AbstractAction {

	ProjectProxy projectProxy;

	UndoManagement undoManager; 
	
	CommonService commonService;

	public SaveAsAction(UndoManagement manager, ProjectProxy projectProxy, CommonService commonService) {
		super("Save As...");

		this.projectProxy = projectProxy;
		this.undoManager = manager;
		this.commonService = commonService;

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK
			| KeyEvent.SHIFT_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_V));

		EnableComponentBridge.connect(this, (VariableProxy<Project>)projectProxy, new NotNullConverter<Project>());
	}

	public void actionPerformed(ActionEvent arg0) {

		commonService.run(new Runnable() {
			public void run() {
					undoManager.saveAs();
			}
		}, "Could not save project");

	}
}
