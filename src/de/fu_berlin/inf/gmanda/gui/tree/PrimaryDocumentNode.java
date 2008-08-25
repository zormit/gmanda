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
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;

public abstract class PrimaryDocumentNode extends JNode {

	PrimaryDocument pd;
	
	public PrimaryDocumentNode(JNode parent, PrimaryDocument d){
		super(parent);
		pd = d;
	}
	
	public PrimaryDocument getDocument(){
		return pd;
	}
	
	public abstract List<JNode> getChildren();
	
	public CellType getType() {
		return CellType.PrimaryDocument;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, JNodeInternalCellRenderer renderer,
		boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		renderer.getTreeCellRendererComponent(tree, this, sel, expanded, leaf, row, hasFocus);

		PrimaryDocument pd = getDocument();

		boolean highLight;

		if (expanded || leaf) {
			renderer.setText(pd.getName());

			highLight = pd.getCodeAsString() != null && pd.getCodeAsString().trim().length() > 0
				&& !pd.getCodeAsString().trim().equals("seen")
				&& !pd.getCodeAsString().trim().equals("spam");
		} else {
			renderer.setText(pd.getName() + " (" + (pd.countChildrenAndSelf() - 1) + " children)");

			highLight = pd.hasCode();
		}

		if (highLight) {
			renderer.setOpaque(true);
			if (sel) {
				renderer.setBackground(renderer.getColors().getSelected().getValue());
			} else if (renderer.getFilterProxy().getVariable() != null
				&& renderer.getFilterProxy().getVariable().filterResult.contains(pd)) {
				renderer.setBackground(renderer.getColors().getMatch().getValue());
			} else {
				renderer.setBackground(renderer.getColors().getCoded().getValue());
			}

		} else {
			if (pd.getMetaData().containsKey("lastseen") && !sel) {
				renderer.setOpaque(true);
				renderer.setBackground(renderer.getColors().getSeen().getValue());
			} else {
				renderer.setOpaque(false);
				renderer.setBackground(null);
			}
		}
		return renderer;
	}
}
