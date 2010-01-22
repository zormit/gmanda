package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.gui.graph.SocialNetworkModule.SNAFileType;
import de.fu_berlin.inf.gmanda.util.Configuration;

/**
 * File chooser for storing DotFiles
 */
public class DotFileFileChooser extends ConfigurationAwareFileChooser<SNAFileType> {

	public DotFileFileChooser(Configuration configuration) {
		super(configuration, "DotFileLocation", SNAFileType.values());
	}

}
