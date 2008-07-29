package de.fu_berlin.inf.gmanda.util.preferences;

import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * This Preference item is not backed by any other storage than memory.
 * 
 * @param <T>
 */
public class MemoryPreferenceItem<T> implements PreferenceItem<T> {

	VariableProxy<T> value = new VariableProxy<T>(null);
	
	public MemoryPreferenceItem(T value) {
		setValue(value);
	}

	public T getValue() {
		return value.getVariable();
	}

	public void setValue(T t) {
		this.value.setVariable(t);
	}

	public void addListener(VariableProxyListener<T> listener) {
		value.add(listener);
	}

	public void removeListener(VariableProxyListener<T> listener) {
		value.remove(listener);
	}
}
