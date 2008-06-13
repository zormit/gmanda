package de.fu_berlin.inf.gmanda.gui.preferences;

/**
 * This Preference item is not backed by any other storage than memory.
 * 
 * @param <T>
 */
public class MemoryPreferenceItem<T> implements PreferenceItem<T> {

	T value;

	public MemoryPreferenceItem(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T t) {
		this.value = t;
	}
}
