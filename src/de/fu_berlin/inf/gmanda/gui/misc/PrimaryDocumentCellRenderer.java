package de.fu_berlin.inf.gmanda.gui.misc;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.fu_berlin.inf.gmanda.proxies.FilterProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.util.preferences.ColorProperties;

public class PrimaryDocumentCellRenderer extends DefaultTreeCellRenderer {

	FilterProxy filterProxy;

	ColorProperties colors;

	public PrimaryDocumentCellRenderer(FilterProxy filterProxy, ColorProperties colors) {
		this.filterProxy = filterProxy;
		this.colors = colors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
	 *      java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
		boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);

		if (value instanceof PrimaryDocument) {

			PrimaryDocument pd = (PrimaryDocument) value;

			boolean isCoded = hasCode(pd, !(expanded || leaf));
			boolean isInFilter = filterProxy.getVariable() != null && filterProxy.getVariable().filterResult
			.contains(pd);
			boolean hasBeenSeen = pd.getMetaData().containsKey("lastseen");
			
			setText(getName(pd, !(expanded || leaf)));

			if (isSelected){
				setOpaque(true);
				setBackground(colors.getSelected().getValue());
			} else {
				if (isInFilter){
					setOpaque(true);
					setBackground(colors.getMatch().getValue());
				} else {
					if (isCoded) {
						setOpaque(true);
						setBackground(colors.getCoded().getValue());
					} else {
						if (hasBeenSeen) {
							setOpaque(true);
							setBackground(colors.getSeen().getValue());
						} else {
							setOpaque(false);
							setBackground(null);
						}
					}
				}
			}
		}

		return this;
	}

	public int countChildrenAndSelf(PrimaryDocument pd) {
		int children = 1;

		for (PrimaryDocument pdc : pd.getChildren()) {
			children += countChildrenAndSelf(pdc);
		}
		return children;
	}

	public String getName(PrimaryDocument pd, boolean recurse){
		if (recurse){
			return pd.getName() + " (" + (countChildrenAndSelf(pd) - 1) + " children)";
		} else {
			return pd.getName();
		}
	}
	
	public boolean hasCode(PrimaryDocument pd, boolean recurse) {
		if (pd.getCode() != null
			&& pd.getCode().trim().length() > 0
			&& !pd.getCode().trim().equals("seen")
			&& !pd.getCode().trim().equals("spam")) {
			return true;
		}
		if (!recurse)
			return false;

		for (PrimaryDocument pdc : pd.getChildren()) {
			if (hasCode(pdc, true))
				return true;
		}
		return false;
	}

}
