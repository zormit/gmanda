/*
 * Created on 05.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.fu_berlin.inf.gmanda.gui.preferences.GmandaPreferenceWindow;

public class ShowPreferencesAction extends AbstractAction {

	de.fu_berlin.inf.gmanda.gui.preferences.GmandaPreferenceWindow preferenceWindow;
	
    public ShowPreferencesAction(GmandaPreferenceWindow preferenceWindow){
        super("Preferences...");
        this.preferenceWindow = preferenceWindow;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P,
                KeyEvent.CTRL_MASK));
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
    }

    public void actionPerformed(ActionEvent arg0) {
        preferenceWindow.showPreferences();
    }

}
