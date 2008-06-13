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

import de.fu_berlin.inf.gmanda.proxies.SelectionViewManager;
import de.fu_berlin.inf.gmanda.proxies.SelectionViewManager.View;
import de.fu_berlin.inf.gmanda.util.glazeddata.AnyChangeListListener;
import de.fu_berlin.inf.gmanda.util.glazeddata.AnyChangeListListenerAdapter;

public class BackSelectionAction extends AbstractAction {

	SelectionViewManager manager;
	
	public BackSelectionAction(SelectionViewManager viewManager) {
		super("Back");
	
		this.manager = viewManager;
	
		this.manager.previousViews.addListener(new AnyChangeListListenerAdapter<View>(new AnyChangeListListener<View>(){
			public void changed() {
				setEnabled(manager.previousViews.size() != 0);
			}
		}));
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
	}

	public void actionPerformed(ActionEvent arg0) {
		this.manager.back();
	}

}
