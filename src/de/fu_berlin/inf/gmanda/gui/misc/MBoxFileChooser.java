package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.util.Configuration;

public class MBoxFileChooser extends ConfigurationAwareFileChooser<ExtensionDescriptor> {
	
	public MBoxFileChooser(Configuration configuration){
		super(configuration, "MboxImportLocation", new ExtensionDescription().append(
				"MBox file *.mbox *.eml", ".mbox", ".eml"));
	}
}
