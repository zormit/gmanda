package de.fu_berlin.inf.gmanda.gui.misc;

import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class StorageFileChooser extends ConfigurationAwareDirectoryChooser {
	
	public StorageFileChooser(Configuration configuration, ForegroundWindowProxy proxy){
		super(configuration, proxy, "StorageLocation");
	}
	
}
