package de.fu_berlin.inf.gmanda.gui.tree;

import javax.swing.tree.TreePath;


public class JNodeTreeModel extends AbstractTreeModel {
	
	JNode root;
	
	public JNodeTreeModel(JNode root){
		this.root = root;
	}
	
	public Object getChild(Object arg0, int arg1) {
		return ((JNode)arg0).getChildren().get(arg1);
	}

	public int getChildCount(Object arg0) {
		return ((JNode)arg0).getChildCount();
	}

	public int getIndexOfChild(Object arg0, Object arg1) {
		return ((JNode)arg0).getChildren().indexOf(arg1);
	}

	public Object getRoot() {
		return root;
	}

	public void valueForPathChanged(TreePath arg0, Object arg1) {
		throw new RuntimeException();	
	}

}
