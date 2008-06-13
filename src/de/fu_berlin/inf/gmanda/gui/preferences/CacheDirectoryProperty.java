package de.fu_berlin.inf.gmanda.gui.preferences;

import java.io.File;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.FileConverter;
import de.fu_berlin.inf.gmanda.util.preferences.FilePreferenceItem;

/**
 * The Cache Directory is used for caching the following things:
 * 
 * <ul>
 * <li> mbox files downloaded from Gmane </li>
 * <li> primary documents </li>
 * <li> lucene search index </li>
 * </ul>
 */
public class CacheDirectoryProperty extends FilePreferenceItem {

	public CacheDirectoryProperty(Configuration configuration, FileConverter f) {
		super(configuration, "CacheLocation", new File("."), f);
	}

}
