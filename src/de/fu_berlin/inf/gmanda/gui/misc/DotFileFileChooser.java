package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.util.Configuration;

/**
 * File chooser for storing DotFiles
 */
public class DotFileFileChooser extends ConfigurationAwareFileChooser {

	public DotFileFileChooser(Configuration configuration) {
		super(configuration, "DotFileLocation", new ExtensionDescription()
				.append("Graphviz Dot *.dot", ".dot").append(
						"GraphML *.graphml", ".graphml"));
	}

}
