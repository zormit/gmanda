package de.fu_berlin.inf.gmanda.proxies;

import java.awt.Component;

import javax.swing.JFrame;

import de.fu_berlin.inf.gmanda.util.VariableProxy;

/**
 * Proxy which holds a reference to the top most visible component
 */
public class ForegroundWindowProxy extends VariableProxy<Component> {

	public ForegroundWindowProxy() {
		super(null);
	}
	
	public JFrame getAsFrameOrNull(){
		if (getVariable() instanceof JFrame)
			return (JFrame)getVariable();
		return null;
	}

}
