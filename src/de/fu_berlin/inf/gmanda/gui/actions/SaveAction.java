package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.manager.UndoManagement;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class SaveAction extends AbstractAction {

	ProjectProxy projectProxy;

	UndoManagement undoManager;

	public SaveAction(UndoManagement manager, ProjectProxy projectProxy) {
		super("Save...");

		this.projectProxy = projectProxy;
		this.undoManager = manager;

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				update();
			}
		});

		manager.getModifiedNotifier().add(new StateChangeListener<Boolean>() {
			public void stateChangedNotification(Boolean t) {
				update();
			}
		});

		update();
	}

	public void update() {
		setEnabled(projectProxy.getVariable() != null && undoManager.isModified());
	}

	public void actionPerformed(ActionEvent arg0) {

		new Thread(new Runnable() {
			public void run() {
				try {
					undoManager.save();
				} catch (DoNotShowToUserException e) {
					// TODO maybe put into log files...
				} catch (ReportToUserException e) {
					JOptionPane.showMessageDialog(null, "Could not save project:\n" + e.getCause()
						+ "\n" + e.getCause().getMessage());
				}
			}
		}).start();
	}
}
