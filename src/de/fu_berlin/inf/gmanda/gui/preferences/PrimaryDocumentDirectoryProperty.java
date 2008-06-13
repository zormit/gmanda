package de.fu_berlin.inf.gmanda.gui.preferences;

import java.io.File;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.FileConverter;
import de.fu_berlin.inf.gmanda.util.preferences.FilePreferenceItem;

/**
 * The PrimaryDocumentDirectory is used to save the last directory the user has
 * loaded PrimaryDocuments from or where he wanted to download Gmane-Mbox files
 * to.
 */
public class PrimaryDocumentDirectoryProperty extends FilePreferenceItem {

	public PrimaryDocumentDirectoryProperty(Configuration configuration, FileConverter f) {
		super(configuration, "PDLocation", new File("."), f);
	}

}
