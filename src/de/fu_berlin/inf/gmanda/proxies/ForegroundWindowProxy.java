package de.fu_berlin.inf.gmanda.proxies;

import java.awt.Component;
import java.awt.Frame;

import de.fu_berlin.inf.gmanda.util.VariableProxy;

/**
 * Proxy which holds a reference to the top most visible component
 */
public class ForegroundWindowProxy extends VariableProxy<Component> {

	public ForegroundWindowProxy() {
		super(null);
	}
	
	public Frame getAsFrameOrNull(){
		if (getVariable() instanceof Frame)
			return (Frame)getVariable();
		return null;
	}

}
