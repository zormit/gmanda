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

import com.google.common.base.Predicate;

import de.fu_berlin.inf.gmanda.graph.Graph.Node;

/**
 * A Predicate which returns true if the given Node has not been assigned to a
 * cluster yet.
 */
public class UnclusteredPredicate implements Predicate<Node> {

	@Override
	public boolean apply(Node a) {
		return a.getCluster() == null;
	}

	public String toString() {
		return " not in any other cluster yet";
	}
}