package de.fu_berlin.inf.gmanda.gui.preferences;

import java.awt.Color;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.ColorPreferenceItem;

public class MatchColorProperty extends ColorPreferenceItem {
	
	public MatchColorProperty(Configuration configuration) {
		super(configuration, "PrimaryDocumentTreeSeenColor", new Color(200, 200, 255));
	}

}
