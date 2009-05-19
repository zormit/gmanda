package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.util.Configuration;

/**
 * Filechooser for storing DotFiles
 */
public class DotFileFileChooser extends ConfigurationAwareFileChooser {
	
	public DotFileFileChooser(Configuration configuration){
		super(configuration, "DotFileLocation", ".dot", "Graphviz Dot *.dot");
	}
	
}
