package de.fu_berlin.inf.gmanda.util.tree;

import java.util.Collection;

public class ChildrenableTreeWalker<T extends Childrenable<T>> extends TreeWalker<T> {
	
	public ChildrenableTreeWalker(T root){
		super(root, new ChildrenableTreeMaker<T>());
	}
	
	public ChildrenableTreeWalker(Collection<T> root){
		super(root, new ChildrenableTreeMaker<T>());
	}
}
