package de.fu_berlin.inf.gmanda.gui.misc;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.fu_berlin.inf.gmanda.util.Configuration;

public class ConfigurationAwareFileChooser extends JFileChooser {

	FileFilter ff;

	Configuration configuration;

	String description;
	String extension;
	String property;

	public ConfigurationAwareFileChooser(Configuration configuration, String configurationPropertyName, final String extension,
		final String description) {
		super(new File(configuration.getProperty(configurationPropertyName, ".")));

		this.configuration = configuration;
		this.description = description;
		this.extension = extension;
		this.property = configurationPropertyName;

		ff = new FileFilter() {
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
	}

	public File getSaveFile() {

		int returnVal = showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory().getAbsolutePath());

			// Append extension if not given
			File f = getSelectedFile();
			if (getFileFilter() == ff && !f.getName().endsWith(extension)) {
				f = new File(f.getParent(), f.getName() + extension);
			}
			return f;
		} else {
			return null;
		}

	}

	public File getOpenFile() {

		int returnVal = showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.setProperty(property, getCurrentDirectory().getAbsolutePath());

			return getSelectedFile();
		} else {
			return null;
		}
	}
}
