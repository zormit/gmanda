/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.tree;

import java.util.Collection;

public class ChildrenableTreeMaker<T extends Childrenable<T>> implements TreeMaker<T> {

	public TreeStructure<T> toStructure(final T t) {
		return new TreeStructure<T>(){

			public T get() {
				return t;
			}

			public Collection<T> getChildren() {
				return t.getChildren();
			}
			
		};
	}
	
}