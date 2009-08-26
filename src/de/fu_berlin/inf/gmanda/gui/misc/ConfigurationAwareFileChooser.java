package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.fu_berlin.inf.gmanda.util.Configuration;

public class ConfigurationAwareFileChooser extends JFileChooser {

	Configuration configuration;

	HashMap<FileFilter, String> extensionMap = new HashMap<FileFilter, String>();

	String property;

	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName, String extension,
			String description) {

		this(configuration, configurationPropertyName,
				new ExtensionDescription().append(extension, description));
	}

	public ConfigurationAwareFileChooser(Configuration configuration,
			String configurationPropertyName,
			ExtensionDescription descriptionAndExtension) {
		
		super(configuration.getProperty(configurationPropertyName, null));

		this.configuration = configuration;
		this.property = configurationPropertyName;

		for (Entry<String, Collection<String>> entry : descriptionAndExtension) {

			final String description = entry.getKey();
			final List<String> extensions = new ArrayList<String>(entry
					.getValue());

			if (extensions.size() == 0)
				throw new IllegalArgumentException();

			FileFilter ff = new FileFilter() {
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					for (String extension : extensions) {
						if (f.getName().endsWith(extension)) {
							return true;
						}
					}
					return false;
				}

				@Override
				public String getDescription() {
					return description;
				}
			};

			addChoosableFileFilter(ff);
			extensionMap.put(ff, extensions.get(0));
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
