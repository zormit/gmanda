package de.fu_berlin.inf.gmanda.gui.preferences;


public interface PreferenceItem<T> {

	public T getValue();

	public void setValue(T t);
		
}
