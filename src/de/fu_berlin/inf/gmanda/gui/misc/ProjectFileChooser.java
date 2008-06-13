package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.util.Configuration;

public class ProjectFileChooser extends ConfigurationAwareFileChooser {
	
	public ProjectFileChooser(Configuration configuration){
		super(configuration, "ProjectLocation", ".gmp", "Gmanda project files *.gmp");
	}
	
}
