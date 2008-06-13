package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;

import de.fu_berlin.inf.gmanda.gui.actions.FormatCodeAction;

public class EditMenu extends JMenu {

	public EditMenu(FormatCodeAction format){
		super("Edit");
		add(format);
	}
	
}
