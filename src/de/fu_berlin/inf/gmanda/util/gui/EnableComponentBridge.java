package de.fu_berlin.inf.gmanda.util.gui;

import java.awt.Component;

import javax.swing.Action;

import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * Connect a Boolean Proxy and the enabled state of a component for
 * unidirectional updates.
 */
public class EnableComponentBridge {

	public static void connect(final Component component, final VariableProxy<Boolean> booleanProxy) {
		booleanProxy.add(new VariableProxyListener<Boolean>() {
			public synchronized void setVariable(Boolean newValue) {
				component.setEnabled(newValue);
			}
		});
	}

	public static <T> void connect(final Action action, final VariableProxy<T> proxy,
		final Converter<T, Boolean> converter) {
		proxy.add(new VariableProxyListener<T>() {
			public synchronized void setVariable(T newValue) {
				action.setEnabled(converter.convert(newValue));
			}
		});
	}

	public static <T> void connect(final Component component, final VariableProxy<T> proxy,
		final Converter<T, Boolean> converter) {
		proxy.add(new VariableProxyListener<T>() {
			public synchronized void setVariable(T newValue) {
				component.setEnabled(converter.convert(newValue));
			}
		});
	}

	public static <T> void connectNonNull(Action action, VariableProxy<T> proxy) {
		connect(action, proxy, new NotNullConverter<T>());
	}
}
