package de.fu_berlin.inf.gmanda.gui.preferences;

import javax.swing.JComponent;
import javax.swing.JTextField;

import de.fu_berlin.inf.gmanda.util.preferences.PreferenceItem;

public class IntegerPreferenceUI extends JTextField implements PreferenceUI<Integer> {

	PreferenceItem<Integer> preferenceItem;
	
	public IntegerPreferenceUI(String titel, PreferenceItem<Integer> b) {
		super(titel);
		this.preferenceItem = b;
	}
	
	public void refresh(){
		this.setText(Integer.toBinaryString(preferenceItem.getValue()));
	}
	
	public void store(){
		
		int newValue;
		try {
			newValue = Integer.parseInt(this.getText());
			preferenceItem.setValue(newValue);
		} catch (Exception e){
			refresh();
		}
	}
	
	public PreferenceItem<Integer> getPreferenceItem(){
		return preferenceItem;
	}

	public JComponent getPreferenceUI() {
		return this;
	}
}
