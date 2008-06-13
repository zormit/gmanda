package de.fu_berlin.inf.gmanda.gui.preferences;

import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.preferences.BooleanPreferenceItem;

public class ScrollOnShowProperty extends BooleanPreferenceItem {
	
	public ScrollOnShowProperty(Configuration configuration) {
		super(configuration, "ScrollOnShow", true);
	}

}
