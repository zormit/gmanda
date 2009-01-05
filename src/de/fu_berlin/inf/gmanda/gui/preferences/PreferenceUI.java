package de.fu_berlin.inf.gmanda.gui.preferences;

import javax.swing.JComponent;

import de.fu_berlin.inf.gmanda.util.preferences.PreferenceItem;

public interface PreferenceUI<T> {

	void refresh();
	
	void store();
	
	PreferenceItem<T> getPreferenceItem();
	
	JComponent getPreferenceUI();
	
}
