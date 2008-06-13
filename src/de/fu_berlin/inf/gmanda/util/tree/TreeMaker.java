/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.tree;

public interface TreeMaker<T> {
	public TreeStructure<T> toStructure(T t);
}