package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;

import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * A Predicate which returns true if the given Node has <= the given number of
 * edges to other Nodes.
 */
public class MaxEdgePredicate implements Predicate<Node> {

	protected List<Edge> edges;

	protected int maxNumberOfPeripheryEdges;

	public MaxEdgePredicate(List<Edge> edges, int maxNumberOfPeripheryEdges) {
		this.edges = edges;
		this.maxNumberOfPeripheryEdges = maxNumberOfPeripheryEdges;
	}

	@Override
	public boolean apply(Node input) {

		Set<Node> neighbors = new HashSet<Node>();

		for (Edge e : edges) {
			Node other = e.getOtherEnd(input);
			if (other != null && other != input)
				neighbors.add(other);
		}

		return neighbors.size() <= maxNumberOfPeripheryEdges;
	}

}
