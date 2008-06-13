package de.fu_berlin.inf.gmanda.gui.docking;

import bibliothek.gui.dock.station.split.SplitDockGrid;
import de.fu_berlin.inf.gmanda.gui.CodeDetailView;
import de.fu_berlin.inf.gmanda.gui.CodeListView;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeView;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentView;

public class DefaultPerspective implements DockingPerspective {

	PrimaryDocumentView primaryDocumentView;

	PrimaryDocumentTreeView primaryDocumentTree;

	CodeListView codeList;

	CodeDetailView codeDetailView;

	ViewManager viewManager;

	SplitDockGrid grid;

	public DefaultPerspective(PrimaryDocumentView primaryDocumentView,
		PrimaryDocumentTreeView primaryDocumentTree, CodeListView codeList,
		CodeDetailView codeDetailView, ViewManager viewManager) {

		this.primaryDocumentView = primaryDocumentView;
		this.primaryDocumentTree = primaryDocumentTree;
		this.codeDetailView = codeDetailView;
		this.codeList = codeList;
		this.viewManager = viewManager;
	}

	public SplitDockGrid getGrid() {
		if (grid == null) {
			grid = new SplitDockGrid();
			grid.addDockable(0, 0, 1.3, 0.8, viewManager.getDockable(primaryDocumentTree));
			grid.addDockable(1, 0, 0.7, 0.8, viewManager.getDockable(codeList));
			grid.addDockable(0, 0.8, 1.3, 1.2, viewManager.getDockable(primaryDocumentView));
			grid.addDockable(1, 0.8, 0.7, 1.2, viewManager.getDockable(codeDetailView));
		}
		return grid;
	}
	
	public String getTitle(){
		return "Coding Perspective";
	}
}
