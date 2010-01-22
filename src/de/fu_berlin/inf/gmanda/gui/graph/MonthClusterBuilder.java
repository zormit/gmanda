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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.fu_berlin.inf.gmanda.graph.Graph;
import de.fu_berlin.inf.gmanda.graph.Graph.Cluster;
import de.fu_berlin.inf.gmanda.graph.Graph.Edge;
import de.fu_berlin.inf.gmanda.graph.Graph.Node;

public class MonthClusterBuilder implements ClusterBuilder {

	protected int coreMinMonths;

	protected int peripheryMaxMonths;

	/**
	 * Will assign clusters, by looking at the month participated.
	 * 
	 * Participants with x <= peripheryMaxMonths will be assigned periphery
	 * 
	 * Participants with coreMinMonths <= x will be assigned core
	 * 
	 * All others will be co-developers.
	 */
	public MonthClusterBuilder(int peripheryMaxMonths, int coreMinMonths) {
		this.peripheryMaxMonths = peripheryMaxMonths;
		this.coreMinMonths = coreMinMonths; 

		if (peripheryMaxMonths < 0 || peripheryMaxMonths > coreMinMonths
				|| coreMinMonths > 12)
			throw new IllegalArgumentException();
	}

	@Override
	public List<Cluster> getClusters(Graph g, List<Node> nodes, List<Edge> edges) {

		Collection<Node> projectMembers = Collections2.filter(nodes,
				new Predicate<Node>() {
					@Override
					public boolean apply(Node a) {
						return a.months.getNonZeroBins() >= coreMinMonths;
					}
				});

		/**
		 * List<Author> projectMembers = getProjectMembersAsClique(
		 * conversations, plainAuthors);
		 */

		Cluster core = g.new Cluster("core", "#FFFFFF");
		for (Node member : projectMembers) {
			core.add(member);
			member.project = true;
		}

		System.out.println("Cluster 'core' contains all people with >= "
				+ coreMinMonths + " of 12 month present in the project.");

		Collection<Node> peripheralMembers = Collections2.filter(nodes,
				new Predicate<Node>() {
					@Override
					public boolean apply(Node a) {
						return a.months.getNonZeroBins() <= peripheryMaxMonths;
					}
				});
		Cluster periphery = g.new Cluster("periphery", "#00FF00");
		for (Node member : peripheralMembers) {
			periphery.add(member);
		}
		System.out
				.println("Cluster 'periphery' (green border) contains all people with <= "
						+ peripheryMaxMonths
						+ " month(s) present in the project.");

		List<Node> coMembers = new ArrayList<Node>();
		Cluster coDevelopers = g.new Cluster("coDevelopers", "#FF0000");
		for (Node member : nodes) {
			if (member.getCluster() == null) {
				coDevelopers.add(member);
				coMembers.add(member);
			}
		}

		System.out
				.println("Cluster 'coDevelopers' (red border) contains all other people ("
						+ peripheryMaxMonths
						+ " < x < "
						+ coreMinMonths
						+ " months.");

		return Arrays.asList(core, coDevelopers, periphery);
	}
}