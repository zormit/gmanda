package de.fu_berlin.inf.gmanda.gui.misc;

import java.util.Collection;

public interface ExtensionDescriptor {

	public String getDescription();
	
	public Collection<String> getSupportedExtensions(); 
	
}
