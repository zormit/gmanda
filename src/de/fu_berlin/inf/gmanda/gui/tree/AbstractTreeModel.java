package de.fu_berlin.inf.gmanda.gui.tree;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

public abstract class AbstractTreeModel implements TreeModel {

	public Set<TreeModelListener> listeners = new HashSet<TreeModelListener>();
	
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}
	
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}
	
	public Set<TreeModelListener> getListeners() {
		return listeners;
	}
	
	public boolean isLeaf(Object arg0) {
		return getChildCount(arg0) == 0;
	}
	
}
