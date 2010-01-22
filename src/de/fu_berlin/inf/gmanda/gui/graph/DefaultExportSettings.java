package de.fu_berlin.inf.gmanda.gui.graph;

import org.colorbrewer.ColorBrewerPalettes;

public class DefaultExportSettings extends ExportSettings {

	public DefaultExportSettings(boolean selfLoops, boolean cluster,
			boolean undirected, boolean onlyGiantComponent) {

		super(selfLoops, cluster, undirected, onlyGiantComponent, false,
				Coloration.MONTH, ColorBrewerPalettes.ylorrd6,
				ColorBrewerPalettes.ylorrd6Font, new MonthClusterBuilder(1, 9));
	}

}
