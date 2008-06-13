package de.fu_berlin.inf.gmanda.gui.docking;

import bibliothek.gui.dock.station.split.SplitDockGrid;

/**
 * A perspective is a special layout of views
 */
public interface DockingPerspective {
	
	public String getTitle();
	
	public SplitDockGrid getGrid();

}
