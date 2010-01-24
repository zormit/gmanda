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

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicates;

import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * ClusterBuilder which builds a periphery by a maximum number of edges to other
 * people, but counting all edges to core members (as identified by Node.project
 * == node) as a single one.
 */
public class MaxEdgeAggregatePeripheryBuilder implements ClusterBuilder {

	protected int coreMinMonths;

	protected int maxNumberOfPeripheryEdges;

	public MaxEdgeAggregatePeripheryBuilder(int maxNumberOfPeripheryEdges,
			int coreMinMonths) {
		this.maxNumberOfPeripheryEdges = maxNumberOfPeripheryEdges;
		this.coreMinMonths = coreMinMonths;
	}

	@Override
	public List<Cluster> getClusters(Graph g, List<Node> nodes, List<Edge> edges) {

		Cluster core = new CoreClusterBuilder(new MinMonthPredicate(
				coreMinMonths)).getCluster(g, nodes, edges);

		Cluster periphery = new DefaultClusterBuilder(Predicates.and(
				new UnclusteredPredicate(), new MaxEdgeAggregatePredicate(
						edges, maxNumberOfPeripheryEdges)), "periphery",
				"#00FF00").getCluster(g, nodes, edges);

		Cluster coDevelopers = new DefaultClusterBuilder(
				new UnclusteredPredicate(), "coDevelopers", "#FF0000")
				.getCluster(g, nodes, edges);

		return Arrays.asList(core, coDevelopers, periphery);

	}
}