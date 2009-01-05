package de.fu_berlin.inf.gmanda.gui.preferences;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import de.fu_berlin.inf.gmanda.util.preferences.BooleanPreferenceItem;
import de.fu_berlin.inf.gmanda.util.preferences.PreferenceItem;

public class BooleanPreferenceCheckbox extends JCheckBox implements PreferenceUI<Boolean> {

	BooleanPreferenceItem preferenceItem;
	
	public BooleanPreferenceCheckbox(String titel, BooleanPreferenceItem b) {
		super(titel);
		this.preferenceItem = b;
	}
	
	public void refresh(){
		this.setSelected(preferenceItem.getValue());
	}
	
	public void store(){
		preferenceItem.setValue(this.isSelected());
	}
	
	public PreferenceItem<Boolean> getPreferenceItem(){
		return preferenceItem;
	}

	public JComponent getPreferenceUI() {
		return this;
	}
	

}
