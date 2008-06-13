package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import de.fu_berlin.inf.gmanda.gui.actions.CloseAction;
import de.fu_berlin.inf.gmanda.gui.actions.ExitAction;
import de.fu_berlin.inf.gmanda.gui.actions.LoadAction;
import de.fu_berlin.inf.gmanda.gui.actions.NewAction;
import de.fu_berlin.inf.gmanda.gui.actions.SaveAction;
import de.fu_berlin.inf.gmanda.gui.actions.SaveAsAction;

public class FileMenu extends JMenu {

	public FileMenu(NewAction open, LoadAction load, SaveAction save, SaveAsAction saveAs,
		CloseAction close, ExitAction exit) {
		super("File");

		add(new JMenuItem(open));
		add(new JMenuItem(load));
		add(new JMenuItem(save));
		add(new JMenuItem(saveAs));
		add(new JMenuItem(close));
		add(new JSeparator());
		add(new JMenuItem(exit));
	}

}
