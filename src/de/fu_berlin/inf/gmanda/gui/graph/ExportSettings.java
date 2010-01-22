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


public class ExportSettings {

	public ExportSettings(boolean selfLoops, boolean cluster,
			boolean undirected, boolean onlyGiantComponent,
			boolean interClusterGraph, Coloration coloration,
			String[] colorPalette, String[] fontColorPalette,
			ClusterBuilder clusterBuilder) {

		this.includeSelfLoops = selfLoops;
		this.cluster = cluster;
		this.undirected = undirected;
		this.onlyGiantComponent = onlyGiantComponent;
		this.interClusterGraph = interClusterGraph;
		this.coloration = coloration;
		this.colorPalette = colorPalette;
		this.fontColorPalette = fontColorPalette;
		this.clusterBuilder = clusterBuilder;
	}

	public ExportSettings(ExportSettings copy) {
		this(copy.includeSelfLoops, copy.cluster, copy.undirected,
				copy.onlyGiantComponent, copy.interClusterGraph,
				copy.coloration, copy.colorPalette, copy.fontColorPalette,
				copy.clusterBuilder);
	}

	protected boolean includeSelfLoops;

	protected boolean cluster;
	
	protected boolean undirected;

	protected boolean onlyGiantComponent;

	protected boolean interClusterGraph;

	protected Coloration coloration;

	protected String[] colorPalette;

	protected String[] fontColorPalette;

	protected ClusterBuilder clusterBuilder;


	public boolean isIncludeSelfLoops() {
		return includeSelfLoops;
	}

	public ExportSettings setIncludeSelfLoops(boolean includeSelfLoops) {
		ExportSettings copy = new ExportSettings(this);
		copy.includeSelfLoops = includeSelfLoops;
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
		ExportSettings copy = new ExportSettings(this);
		copy.cluster = cluster;
		return copy;
	}

	public ExportSettings setUndirected(boolean undirected) {
		ExportSettings copy = new ExportSettings(this);
		copy.undirected = undirected;
		return copy;
	}

	public void setOnlyGiantComponent(boolean onlyGiantComponent) {
		this.onlyGiantComponent = onlyGiantComponent;
	}

	public ExportSettings setInterClusterGraph(boolean interClusterGraph) {
		ExportSettings copy = new ExportSettings(this);
		copy.interClusterGraph = interClusterGraph;
		return copy;
	}


	public boolean isInterClusterGraph() {
		return interClusterGraph;
	}

	public boolean isUndirected() {
		return undirected;
	}

	public boolean isCluster() {
		return cluster;
	}

	public boolean isOnlyGiantComponent() {
		return onlyGiantComponent;
	}
}