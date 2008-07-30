package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;

import de.fu_berlin.inf.gmanda.gui.actions.FormatCodeAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertDateAction;

public class EditMenu extends JMenu {

	public EditMenu(FormatCodeAction format, InsertDateAction insertDate){
		super("Edit");
		add(format);
		add(insertDate);
	}
	
}
