package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.List;

import com.google.common.base.Predicate;

import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * A SingleClusterBuilder which will use the given Predicate for detecting
 * members of the project core and which will set the Node.project field to
 * true.
 */
public class CoreClusterBuilder extends DefaultClusterBuilder {

	public CoreClusterBuilder(Predicate<Node> memberPredicate) {
		super(memberPredicate, "core", "#FFFFFF");
	}

	@Override
	public Cluster getCluster(Graph g, List<Node> nodes, List<Edge> edges) {

		Cluster result =  super.getCluster(g, nodes, edges);
		
		for (Node member : result.getAuthors()) {
			member.project = true;
		}
		return result;
	}
}
