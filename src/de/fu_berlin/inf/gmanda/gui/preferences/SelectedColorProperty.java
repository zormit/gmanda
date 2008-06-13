package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.Color;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.ColorPreferenceItem;

public class SelectedColorProperty extends ColorPreferenceItem {
	
	public SelectedColorProperty(Configuration configuration) {
		super(configuration, "PrimaryDocumentTreeSeenColor", new Color(0x00008B));
	}

}
