package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.Codeable;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class FormatCodeAction extends AbstractAction {

	SelectionProxy selectionProxy;

	public FormatCodeAction(SelectionProxy selectionProxy) {
		super("Format");
		this.selectionProxy = selectionProxy;

		selectionProxy.add(new VariableProxyListener<Object>() {
			public void setVariable(Object newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
	}

	public void actionPerformed(ActionEvent arg0) {
		Object o = selectionProxy.getVariable();

		if (!(o instanceof Codeable))
			return;

		Codeable c = (Codeable) o;

		String code = c.getCodeAsString();

		if (code == null)
			return;

		CodedString cs = CodedStringFactory.parse(code);
		
		c.setCode(cs.format());
	
	
	}

};




