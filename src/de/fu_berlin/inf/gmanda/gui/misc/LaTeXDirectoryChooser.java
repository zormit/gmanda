package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class LaTeXDirectoryChooser extends ConfigurationAwareDirectoryChooser {
	
	public LaTeXDirectoryChooser(Configuration configuration, ForegroundWindowProxy proxy){
		super(configuration, proxy, "LatexDirectoryLocation", "Select the folder which contains your LaTeX source files");
	}
	
}
