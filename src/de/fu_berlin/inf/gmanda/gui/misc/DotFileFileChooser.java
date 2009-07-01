package de.fu_berlin.inf.gmanda.gui.misc;

import java.util.Arrays;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.Pair;

/**
 * File chooser for storing DotFiles
 */
public class DotFileFileChooser extends ConfigurationAwareFileChooser {

	@SuppressWarnings("unchecked")
	public DotFileFileChooser(Configuration configuration) {
		super(configuration, "DotFileLocation", Arrays.asList(
				new Pair<String, String>(".dot", "Graphviz Dot *.dot"),
				new Pair<String, String>(".graphml", "GraphML *.graphml")));
	}

}
