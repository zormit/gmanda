package de.fu_berlin.inf.gmanda.gui.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.fu_berlin.inf.gmanda.gui.preferences.ColorProperties;
import de.fu_berlin.inf.gmanda.proxies.FilterProxy;

public class JNodeCellRenderer extends DefaultTreeCellRenderer {

	public class JNodeInternalCellRenderer extends DefaultTreeCellRenderer {
		
		public ColorProperties getColors(){
			return colors;
		}
		
		public FilterProxy getFilterProxy(){
			return filterProxy;
		}
	}
	
	JNodeInternalCellRenderer cellRenderer = new JNodeInternalCellRenderer();
	
	FilterProxy filterProxy;

	ColorProperties colors;

	public JNodeCellRenderer(FilterProxy filterProxy, ColorProperties colors) {
		this.filterProxy = filterProxy;
		this.colors = colors;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
		boolean expanded, boolean leaf, int row, boolean hasFocus) {

		if (value instanceof JNode) {

			JNode node = (JNode) value;
			
			cellRenderer.setOpaque(false);
			cellRenderer.setBackground(null);
			
			return node.getTreeCellRendererComponent(tree, cellRenderer, sel, expanded, leaf, row, hasFocus);
			
		} else {
			
			cellRenderer.setOpaque(false);
			cellRenderer.setBackground(null);

			return cellRenderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}

}
