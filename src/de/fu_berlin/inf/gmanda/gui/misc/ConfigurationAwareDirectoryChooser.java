package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;

import javax.swing.JFileChooser;

import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class ConfigurationAwareDirectoryChooser extends JFileChooser {

	Configuration configuration;

	String property;
	
	ForegroundWindowProxy windowProxy;

	public ConfigurationAwareDirectoryChooser(
		Configuration configuration, 
		ForegroundWindowProxy windowProxy, 
		String configurationPropertyName,
		String dialogTitle) {
		
		this.configuration = configuration;
		this.property = configurationPropertyName;
		this.windowProxy = windowProxy;

		setDialogTitle(dialogTitle);
		
		String path = configuration.getProperty(configurationPropertyName, ".");
		if (File.separatorChar != path.charAt(path.length() - 1)){
			path += File.separatorChar;
		}
		setCurrentDirectory(new File(path));
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public File getOpenFile() {

		int returnVal = showOpenDialog(windowProxy.getVariable());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = getSelectedFile();
			if (selectedFile != null) {
				configuration.setProperty(property, selectedFile.getAbsolutePath());
			}
			return selectedFile;
		} else {
			return null;
		}
	}
}
