package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.util.Configuration;

/**
 * Filechooser for storing Screenshots
 */
public class ScreenshotFileChooser extends ConfigurationAwareFileChooser<ExtensionDescriptor> {
	
	public ScreenshotFileChooser(Configuration configuration){
		super(configuration, "ScreenshotLocation", ".svg", "Scalable Vector Graphics *.svg");
	}
	
}
