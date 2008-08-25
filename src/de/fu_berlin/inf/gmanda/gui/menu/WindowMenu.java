package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenu;

import de.fu_berlin.inf.gmanda.gui.actions.BackSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.ForwardSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.FullscreenTextViewAction;
import de.fu_berlin.inf.gmanda.gui.actions.RewindSelectionAction;
import de.fu_berlin.inf.gmanda.gui.actions.ShowPreferencesAction;
import de.fu_berlin.inf.gmanda.gui.docking.DockablePerspectiveMenu;
import de.fu_berlin.inf.gmanda.gui.docking.DockableViewMenu;

public class WindowMenu extends JMenu {

	public WindowMenu(ShowPreferencesAction showPreferencesAction, BackSelectionAction back, RewindSelectionAction rewind,
		ForwardSelectionAction forward, FullscreenTextViewAction fullscreen,
		DockableViewMenu dockableViewMenu, DockablePerspectiveMenu dockablePerspectiveMenu) {
		super("Window");

		add(back);
		add(rewind);
		add(forward);
		addSeparator();
		add(fullscreen);
		add(dockablePerspectiveMenu.getMenu());
		add(dockableViewMenu.getMenu());
		addSeparator();
		add(showPreferencesAction);
	}

}
