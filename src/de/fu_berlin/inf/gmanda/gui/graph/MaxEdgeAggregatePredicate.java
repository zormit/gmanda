package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;

import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * A Predicate which will return true iff a given Node has at most the given
 * number of edges AFTER collapsing all edges going from the Node to the core
 * into a single one.
 */
public class MaxEdgeAggregatePredicate implements Predicate<Node> {

	protected List<Edge> edges;

	protected int maxNumberOfPeripheryEdges;

	public MaxEdgeAggregatePredicate(List<Edge> edges,
			int maxNumberOfPeripheryEdges) {
		this.edges = edges;
		this.maxNumberOfPeripheryEdges = maxNumberOfPeripheryEdges;
	}

	@Override
	public boolean apply(Node input) {

		Set<Node> neighbors = new HashSet<Node>();

		boolean core = false;

		for (Edge e : edges) {
			Node other = e.getOtherEnd(input);
			if (other != null && other != input) {
				if (other.project) {
					core = true;
					continue; // All project members count as one!
				}
				neighbors.add(other);
			}
		}
		
		// The core counts as one!
		return neighbors.size() + (core ? 1 : 0) <= maxNumberOfPeripheryEdges;
	}

}
