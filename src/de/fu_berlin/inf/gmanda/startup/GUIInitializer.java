package de.fu_berlin.inf.gmanda.startup;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;

public class GUIInitializer implements Initializer {

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Ignore
		}
		
		// Show tooltips until user moves mouse
	    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	    ToolTipManager.sharedInstance().setInitialDelay(1500);
	}

}
