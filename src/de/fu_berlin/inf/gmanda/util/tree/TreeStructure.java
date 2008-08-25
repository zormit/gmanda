/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.tree;


public interface TreeStructure<T> {
	T get();

	Iterable<T> getChildren();
}