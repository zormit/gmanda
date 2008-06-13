package de.fu_berlin.inf.gmanda.gui.docking;

import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;
import de.fu_berlin.inf.gmanda.gui.CodeDetailView;
import de.fu_berlin.inf.gmanda.gui.CodeListView;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeView;
import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentView;
import de.fu_berlin.inf.gmanda.gui.visualisation.VisualizationPane;

public class VisualizationPerspective implements DockingPerspective {

	PrimaryDocumentView primaryDocumentView;

	PrimaryDocumentTreeView primaryDocumentTree;

	CodeListView codeList;

	CodeDetailView codeDetailView;

	ViewManager viewManager;

	VisualizationPane visualizationPane;

	public VisualizationPerspective(PrimaryDocumentView primaryDocumentView,
		PrimaryDocumentTreeView primaryDocumentTree, CodeListView codeList,
		CodeDetailView codeDetailView, ViewManager viewManager, VisualizationPane visualizationPane) {

		this.primaryDocumentView = primaryDocumentView;
		this.primaryDocumentTree = primaryDocumentTree;
		this.codeDetailView = codeDetailView;
		this.codeList = codeList;
		this.viewManager = viewManager;
		this.visualizationPane = visualizationPane;
	}

	public SplitDockGrid getGrid() {
		SplitDockGrid grid = new SplitDockGrid();
		grid.addDockable(0, 0, 1.3, 0.8, viewManager.getDockable(visualizationPane));
		grid.addDockable(1, 0, 0.7, 0.8, viewManager.getDockable(codeList));
		StackDockStation sds = new StackDockStation();
		sds.drop(viewManager.getDockable(primaryDocumentView));
		sds.drop(viewManager.getDockable(primaryDocumentTree));
		grid.addDockable(0, 0.8, 1.3, 1.2, sds);
		grid.addDockable(1, 0.8, 0.7, 1.2, viewManager.getDockable(codeDetailView));
		return grid;
	}

	public String getTitle() {
		return "Visualization Perspective";
	}
}
