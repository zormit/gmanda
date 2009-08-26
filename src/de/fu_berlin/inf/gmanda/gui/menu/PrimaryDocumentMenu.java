package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import de.fu_berlin.inf.gmanda.gui.actions.FetchGmaneListToFileAction;
import de.fu_berlin.inf.gmanda.gui.actions.ImportFromMboxAction;
import de.fu_berlin.inf.gmanda.gui.actions.JumpToIdAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadGmaneListAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadPrimaryDocumentAction;
import de.fu_berlin.inf.gmanda.gui.actions.MakeAllAvailableAction;
import de.fu_berlin.inf.gmanda.gui.actions.ResetFilterAction;

public class PrimaryDocumentMenu extends JMenu {

	public PrimaryDocumentMenu(JumpToIdAction id, LoadPrimaryDocumentAction load,
		LoadGmaneListAction loadGmane, 
		ResetFilterAction reset, 
		FetchGmaneListToFileAction fetchGmane,
		MakeAllAvailableAction makeAvailable,
		ImportFromMboxAction mboxAction) {
		super("Documents");

		add(new JMenuItem(id));
		add(new JMenuItem(reset));
		add(new JSeparator());
		add(new JMenuItem(load));
		add(new JMenuItem(loadGmane));
		add(new JMenuItem(mboxAction));
		add(new JMenuItem(makeAvailable));
	}
}
