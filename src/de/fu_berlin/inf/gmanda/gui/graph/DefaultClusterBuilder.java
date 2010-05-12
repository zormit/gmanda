package de.fu_berlin.inf.gmanda.gui.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * A straight forward cluster builder, which will just use the given predicate to select members.
 * 
 * The builder returns a cluster with the given name and color.
 */
public class DefaultClusterBuilder implements SingleClusterBuilder, ClusterBuilder {
	
	protected Predicate<Node> memberPredicate;
	
	protected String name;
	
	protected String color;
	
	public DefaultClusterBuilder(Predicate<Node> memberPredicate, String name,
			String color) {
		this.memberPredicate = memberPredicate;
		this.name = name;
		this.color = color;
	}


	@Override
	public Cluster getCluster(Graph g, List<Node> nodes, List<Edge> edges) {

		Cluster result = g.new Cluster(name, color);
		
		Collection<Node> projectMembers = Collections2.filter(nodes,
				memberPredicate);

		for (Node member : projectMembers) {
			result.add(member);
		}

		System.out.println("Cluster '"+result.getName()+"' (color: "+ result.getColor()+ ") contains all people for who are " + memberPredicate.toString());
		return result;
	}


	@Override
	public List<Cluster> getClusters(Graph g, List<Node> nodes, List<Edge> edges) {
		return Arrays.asList(getCluster(g, nodes, edges));
	}
}
