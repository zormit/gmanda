package de.fu_berlin.inf.gmanda.gui.menu;

import javax.swing.JMenuBar;

public class MainWindowMenuBar extends JMenuBar {

    public MainWindowMenuBar(FileMenu fileMenu, EditMenu editMenu, PrimaryDocumentMenu pdMenu, ToolsMenu toolsMenu,
    	WindowMenu windowMenu){
    	super();
    	
    	add(fileMenu);
    	add(editMenu);
    	add(pdMenu);
    	add(toolsMenu);
    	add(windowMenu);
    }
}