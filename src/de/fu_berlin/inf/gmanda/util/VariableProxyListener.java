/*
 * Created on 20.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.util;

/**
 * A VariableProxyListener is an object that is interested in changes to a
 * variable.
 * 
 * A VariableProxy allows you to add yourself as a listener for such changes to
 * it.
 */
public interface VariableProxyListener<T> {

	public void setVariable(T newValue);

}
