/**
 * Copyright 2008 Christopher Oezbek
 * 
 * This file is part of GmanDA.
 *
 * GmanDA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GmanDA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GmanDA.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.EnumSet;

public class ExportSettings {

	public enum ExportSetting {
		SELFLOOPS, CLUSTER, UNDIRECTED, ONLYGIANTCOMPONENT, INTERCLUSTERGRAPH, SCALENODES, SCALEEDGES, COLOR, COLLAPSEEDGES;
	}

	EnumSet<ExportSetting> exportSetting;

	public ExportSettings(EnumSet<ExportSetting> exportSetting,
			Coloration coloration, String[] colorPalette,
			String[] fontColorPalette, ClusterBuilder clusterBuilder) {

		this.exportSetting = exportSetting;
		this.coloration = coloration;
		this.colorPalette = colorPalette;
		this.fontColorPalette = fontColorPalette;
		this.clusterBuilder = clusterBuilder;
	}

	public ExportSettings(ExportSettings copy) {
		this(EnumSet.copyOf(copy.exportSetting), copy.coloration,
				copy.colorPalette, copy.fontColorPalette, copy.clusterBuilder);
	}

	protected Coloration coloration;

	protected String[] colorPalette;

	protected String[] fontColorPalette;

	protected ClusterBuilder clusterBuilder;

	public boolean isIncludeSelfLoops() {
		return exportSetting.contains(ExportSetting.SELFLOOPS);
	}

	public ExportSettings setIncludeSelfLoops(boolean includeSelfLoops) {
		return copyAndSet(ExportSetting.SELFLOOPS, includeSelfLoops);
	}

	private ExportSettings copyAndSet(ExportSetting setting, boolean set) {
		ExportSettings copy = new ExportSettings(this);
		if (set)
			copy.exportSetting.add(setting);
		else
			copy.exportSetting.remove(setting);
		return copy;
	}

	public Coloration getColoration() {
		return coloration;
	}

	public ExportSettings setColoration(Coloration coloration) {
		ExportSettings copy = new ExportSettings(this);
		copy.coloration = coloration;
		return copy;
	}

	public String[] getColorPalette() {
		return colorPalette;
	}

	public ExportSettings setColorPalette(String[] colorPalette) {
		ExportSettings copy = new ExportSettings(this);
		copy.colorPalette = colorPalette;
		return copy;
	}

	public String[] getFontColorPalette() {
		return fontColorPalette;
	}

	public ExportSettings setFontColorPalette(String[] fontColorPalette) {
		ExportSettings copy = new ExportSettings(this);
		copy.fontColorPalette = fontColorPalette;
		return copy;
	}

	public ClusterBuilder getClusterBuilder() {
		return clusterBuilder;
	}

	public ExportSettings setClusterBuilder(ClusterBuilder clusterBuilder) {
		ExportSettings copy = new ExportSettings(this);
		copy.clusterBuilder = clusterBuilder;
		return copy;
	}

	public ExportSettings setCluster(boolean cluster) {
		return copyAndSet(ExportSetting.CLUSTER, cluster);
	}

	public ExportSettings setUndirected(boolean undirected) {
		return copyAndSet(ExportSetting.UNDIRECTED, undirected);
	}

	public ExportSettings setOnlyGiantComponent(boolean onlyGiantComponent) {
		return copyAndSet(ExportSetting.ONLYGIANTCOMPONENT, onlyGiantComponent);
	}

	public ExportSettings setInterClusterGraph(boolean interClusterGraph) {
		return copyAndSet(ExportSetting.INTERCLUSTERGRAPH, interClusterGraph);
	}

	public boolean isInterClusterGraph() {
		return exportSetting.contains(ExportSetting.INTERCLUSTERGRAPH);
	}

	public boolean isUndirected() {
		return exportSetting.contains(ExportSetting.UNDIRECTED);
	}

	public boolean isCluster() {
		return exportSetting.contains(ExportSetting.CLUSTER);
	}

	public boolean isOnlyGiantComponent() {
		return exportSetting.contains(ExportSetting.ONLYGIANTCOMPONENT);
	}

	public ExportSettings setScaleNodes(boolean scaleNodes) {
		return copyAndSet(ExportSetting.SCALENODES, scaleNodes);
	}

	public ExportSettings setScaleEdges(boolean scaleEdges) {
		return copyAndSet(ExportSetting.SCALEEDGES, scaleEdges);
	}

	public ExportSettings setCollapseEdges(boolean collapseEdges) {
		return copyAndSet(ExportSetting.COLLAPSEEDGES, collapseEdges);
	}

	
	public boolean isScaleNodes() {
		return exportSetting.contains(ExportSetting.SCALENODES);
	}

	public boolean isScaleEdges() {
		return exportSetting.contains(ExportSetting.SCALEEDGES);
	}

	public boolean isCollapseEdges() {
		return exportSetting.contains(ExportSetting.COLLAPSEEDGES);
	}
}