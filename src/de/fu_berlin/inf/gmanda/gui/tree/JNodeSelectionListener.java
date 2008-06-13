package de.fu_berlin.inf.gmanda.gui.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;

/**
 * Scenario 1: If the user clicks, this will be called and must update the
 * selectionproxy value.
 * 
 * Scenario 2: If somewhere in the program the selection is udpated, we need to
 * set our treepath accordingly and do not want to set the selection (because we
 * are receiving the change).
 * 
 */
public class JNodeSelectionListener implements TreeSelectionListener {

	SelectionProxy selection;
	
	public JNodeSelectionListener(SelectionProxy selection){
		this.selection = selection;
	}
	
	public boolean receiving = true;
	
	public boolean isReceiving() {
		return receiving;
	}
	
	public void setReceiving(boolean receiving) {
		this.receiving = receiving;
	}

	public void valueChanged(TreeSelectionEvent arg0) {

		if (!isReceiving())
			return;

		TreePath path = arg0.getPath();

		if (path != null) {
			Object[] turns = path.getPath();

			Object toShow = turns[turns.length - 1];
			
			if (toShow instanceof JNode){
				JNode node = (JNode) toShow;

				switch (node.getType()) {
				case PrimaryDocument:
					PrimaryDocumentNode pdNode = (PrimaryDocumentNode)node;
					setReceiving(false);
					selection.setVariable(pdNode.getDocument());
					setReceiving(true);
					return;
				default:	
				}
			}
		}
		selection.setVariable(null);
	}
}