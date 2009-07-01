package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.Pair;

public class ConfigurationAwareFileChooser extends JFileChooser {

	Configuration configuration;

	HashMap<FileFilter, String> extensionMap = new HashMap<FileFilter, String>();
	
	String property;

	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName,
			String extension, String description){
		
		this(configuration, configurationPropertyName, 
				Collections.singletonList(new Pair<String, String>(extension, 
						description)));
	}
	
	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName,
			List<Pair<String, String>> extensionsAndDescriptions) {

		this.configuration = configuration;
		this.property = configurationPropertyName;

		for (Pair<String, String> extensionAndDescription : extensionsAndDescriptions) {
			
			final String extension = extensionAndDescription.p;
			final String description = extensionAndDescription.v;
			
			
			FileFilter ff = new FileFilter() {
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					return f.getName().endsWith(extension);
				}

				@Override
				public String getDescription() {
					return description;
				}
			};

			addChoosableFileFilter(ff);
			extensionMap.put(ff, extension);
		}

	}

	public File getSaveFile() {

		int returnVal = showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory()
					.getAbsolutePath());

			// Append extension if not given
			File f = getSelectedFile();
			
			String extension = extensionMap.get(getFileFilter());
			if (extension != null && !f.getName().endsWith(extension)) {
				return new File(f.getParent(), f.getName() + extension);
			}
			return f;
		} else {
			return null;
		}

	}

	public File getOpenFile() {

		int returnVal = showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory()
					.getAbsolutePath());

			return getSelectedFile();
		} else {
			return null;
		}
	}
}
