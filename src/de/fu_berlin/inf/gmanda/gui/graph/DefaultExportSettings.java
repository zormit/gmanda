package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.Arrays;
import java.util.EnumSet;

import org.colorbrewer.ColorBrewerPalettes;

public class DefaultExportSettings extends ExportSettings {

	public DefaultExportSettings(ExportSetting...settings) {
		super(EnumSet.copyOf(Arrays.asList(settings)),
				Coloration.MONTH, ColorBrewerPalettes.ylorrd6,
				ColorBrewerPalettes.ylorrd6Font, new MonthClusterBuilder(1, 9));
	}

}
