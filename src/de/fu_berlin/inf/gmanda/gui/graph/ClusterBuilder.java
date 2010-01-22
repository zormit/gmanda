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

import java.util.List;

import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

public interface ClusterBuilder {
	/**
	 * Returns a list of clusters identified for the given project.
	 * 
	 * The first in the list should be the project core!
	 */
	public List<Cluster> getClusters(Graph g, List<Node> nodes,
			List<Edge> edges);
}