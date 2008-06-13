package de.fu_berlin.inf.gmanda.gui.docking;

import java.awt.Component;

public interface DockableView {
	
	public Component getComponent();
	
	public String getTitle();
	
	public String getTooltip();

	public String getId();

}
