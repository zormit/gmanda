package de.fu_berlin.inf.gmanda.gui.docking;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

public class DockablePerspectiveMenu {
	
	DockingPerspective[] perspectives;
	ViewManager viewManager;
	
	public DockablePerspectiveMenu(DockingPerspective[] perspectives, ViewManager viewManager){
		this.perspectives = perspectives;
		this.viewManager = viewManager;
	}
	
	public JMenu getMenu(){
		JMenu result = new JMenu("Perspectives");
		
		for (final DockingPerspective perspective : perspectives){
			result.add(new AbstractAction(perspective.getTitle()){
				public void actionPerformed(ActionEvent e) {
					viewManager.showPerspective(perspective);
				}
			});
		}
		return result;
	}
}
