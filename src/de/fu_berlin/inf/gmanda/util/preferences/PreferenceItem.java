package de.fu_berlin.inf.gmanda.util.preferences;

import de.fu_berlin.inf.gmanda.util.VariableProxyListener;


public interface PreferenceItem<T> {

	public T getValue();

	public void setValue(T t);
	
	public void addListener(VariableProxyListener<T> listener);
	
	public void removeListener(VariableProxyListener<T> listener);
		
}
