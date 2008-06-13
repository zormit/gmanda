package de.fu_berlin.inf.gmanda.gui.docking;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;

public class DockableViewMenu {
	
	DockableView[] views;
	ViewManager viewManager;
	
	public DockableViewMenu(DockableView[] views, ViewManager viewManager){
		this.views = views;
		this.viewManager = viewManager;
	}
	
	public JMenu getMenu(){
		JMenu result = new JMenu("Views");
		
		for (final DockableView view : views){
			result.add(new AbstractAction(view.getTitle()){
				public void actionPerformed(ActionEvent e) {
					viewManager.showDockableView(view);
				}
			});
		}
		return result;
	}
}
