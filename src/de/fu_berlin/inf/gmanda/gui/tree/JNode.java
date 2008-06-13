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
package de.fu_berlin.inf.gmanda.gui.tree;

import java.awt.Component;
import java.util.List;

import javax.swing.JTree;

import de.fu_berlin.inf.gmanda.gui.tree.JNodeCellRenderer.JNodeInternalCellRenderer;

public abstract class JNode {

	public enum CellType { 
		PrimaryDocument, AllOccurances, MailingList, Project, Code, Other;
	}

	public Component getTreeCellRendererComponent(JTree tree, JNodeInternalCellRenderer renderer,
		boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		return renderer
			.getTreeCellRendererComponent(tree, this, sel, expanded, leaf, row, hasFocus);

	}

	public abstract CellType getType();

	JNode parent;

	public JNode(JNode parent) {
		this.parent = parent;
	}

	public JNode getParent() {
		return this.parent;
	}

	/**
	 * Each JNode is expected to cache this list
	 */
	public abstract List<JNode> getChildren();

	/**
	 * Subclasses can override, if they can determine the number of children
	 * without getting the list of children initialized themselves.
	 */
	public int getChildCount() {
		return getChildren().size();
	}
}