package de.fu_berlin.inf.gmanda.gui.preferences;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.BooleanPreferenceItem;

public class DebugModeProperty extends BooleanPreferenceItem {
	
	public DebugModeProperty(Configuration configuration) {
		super(configuration, "DebugMode", false);
	}

}
