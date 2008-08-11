package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.CodeBox;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class InsertSessionLogTemplateAction extends AbstractAction {

	SelectionProxy selectionProxy;

	CodeBox box;

	public InsertSessionLogTemplateAction(SelectionProxy selectionProxy, CodeBox box) {
		super("Insert session log template");
		this.selectionProxy = selectionProxy;
		this.box = box;

		selectionProxy.add(new VariableProxyListener<Object>() {
			public void setVariable(Object newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke
			.getKeyStroke(KeyEvent.VK_F5, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
	}

	public void actionPerformed(ActionEvent arg0) {
		box.insertSessionLogTemplate();
	}
}
