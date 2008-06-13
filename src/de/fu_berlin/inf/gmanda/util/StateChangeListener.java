package de.fu_berlin.inf.gmanda.util;

/**
 * The notifier complements the Listener construct.
 * 
 * 
 */
public interface StateChangeListener<T> {
    
	public void stateChangedNotification(T t);
    
}
