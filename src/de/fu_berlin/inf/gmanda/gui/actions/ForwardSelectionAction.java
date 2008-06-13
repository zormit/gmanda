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

public class ForwardSelectionAction extends AbstractAction {

	SelectionViewManager manager;
	
	public ForwardSelectionAction(SelectionViewManager viewManager) {
		super("Forward");
	
		this.manager = viewManager;
	
		this.manager.nextViews.addListener(new AnyChangeListListenerAdapter<View>(new AnyChangeListListener<View>(){
			public void changed() {
				setEnabled(manager.nextViews.size() != 0);
			}
		}));
		
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK));
		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
	}

	public void actionPerformed(ActionEvent arg0) {
		this.manager.forward();
	}

}
