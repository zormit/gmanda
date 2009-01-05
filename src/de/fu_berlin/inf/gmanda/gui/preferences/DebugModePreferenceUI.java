package de.fu_berlin.inf.gmanda.gui.preferences;


public class DebugModePreferenceUI extends BooleanPreferenceCheckbox {

	public DebugModePreferenceUI(DebugModeProperty property) {
		super("Enable debugging mode (extra output, auto-reloading of templates,...)", property);
	}

}
