package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.Color;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.ColorPreferenceItem;

public class CodedColorProperty extends ColorPreferenceItem {
	
	public CodedColorProperty(Configuration configuration) {
		super(configuration, "PrimaryDocumentTreeSeenColor", Color.YELLOW);
	}

}
