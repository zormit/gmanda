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

import javax.swing.JTree;

import de.fu_berlin.inf.gmanda.gui.tree.JNodeCellRenderer.JNodeInternalCellRenderer;

public abstract class NamedNode extends JNode {

	public NamedNode(JNode parent) {
		super(parent);
	}

	public abstract String getName();

	@Override
	public CellType getType() {
		return CellType.Other;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, JNodeInternalCellRenderer renderer,
		boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		return renderer.getTreeCellRendererComponent(tree, getName(), sel, expanded, leaf, row,
			hasFocus);
	}
}