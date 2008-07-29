package de.fu_berlin.inf.gmanda.util.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * Connect a Boolean Proxy and the enabled state of a component for
 * unidirectional updates.
 */
public class SelectedComponentBridge {

	public static void connect(final JButton component, final VariableProxy<Boolean> booleanProxy) {
		booleanProxy.add(new VariableProxyListener<Boolean>() {
			public synchronized void setVariable(Boolean newValue) {
				component.setSelected(newValue);
			}
		});
	}
	
	public static void connect(final VariableProxy<Boolean> booleanProxy, final JButton component){
		component.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				booleanProxy.setVariable(component.isSelected());
			}
		});
	}

	public static <T> void connect(final JButton component, final VariableProxy<T> proxy,
		final Converter<T, Boolean> converter) {
		proxy.add(new VariableProxyListener<T>() {
			public synchronized void setVariable(T newValue) {
				component.setSelected(converter.convert(newValue));
			}
		});
	}
}
