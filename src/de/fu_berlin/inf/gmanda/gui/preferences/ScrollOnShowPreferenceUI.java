package de.fu_berlin.inf.gmanda.gui.preferences;


public class ScrollOnShowPreferenceUI extends BooleanPreferenceCheckbox {

	public ScrollOnShowPreferenceUI(ScrollOnShowProperty property) {
		super("Jump to first occurance of search term", property);
	}

}
