package de.fu_berlin.inf.gmanda.util.preferences;

import java.io.File;

import de.fu_berlin.inf.gmanda.gui.preferences.ConfigurationPreferenceItem;
import de.fu_berlin.inf.gmanda.util.Configuration;

public class FilePreferenceItem extends ConfigurationPreferenceItem<File> {
	
	public FilePreferenceItem(Configuration conf, String key, File defaultValue, FileConverter f) {
		super(conf, key, defaultValue, f, f);
	}
}