package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;

public class LoadAction extends AbstractAction {

	UndoManagement undoManager;
	
	CommonService commonService;
	
	public LoadAction(UndoManagement undoManager, CommonService commonService) {
		super("Open...");

		this.undoManager = undoManager;
		this.commonService = commonService;

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
	}

	public void actionPerformed(ActionEvent arg0) {
		commonService.run(new Runnable() {
			public void run() {
					undoManager.load();
			}
		}, "Could not load project");
	}
}
