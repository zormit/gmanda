package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;

import de.fu_berlin.inf.gmanda.gui.actions.FormatCodeAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertDateAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertSessionLogTemplateAction;
import de.fu_berlin.inf.gmanda.gui.actions.InsertSubCodeTemplateAction;

public class EditMenu extends JMenu {

	public EditMenu(FormatCodeAction format, InsertDateAction insertDate, InsertSubCodeTemplateAction insertSubCode, InsertSessionLogTemplateAction insertSessionLog){
		super("Edit");
		add(format);
		add(insertDate);
		add(insertSubCode);
		add(insertSessionLog);
	}
	
}
