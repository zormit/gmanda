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

import de.fu_berlin.inf.gmanda.proxies.FilterProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class ResetFilterAction extends AbstractAction {

	FilterProxy filterProxy;

	public ResetFilterAction(FilterProxy filterProxy) {
		super("Reset Filter");
		this.filterProxy = filterProxy;

		filterProxy.add(new VariableProxyListener<Object>() {
			public void setVariable(Object newValue) {
				setEnabled(newValue != null);
			}
		});

		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
	}

	public void actionPerformed(ActionEvent arg0) {
		filterProxy.setVariable(null);
	}
}
