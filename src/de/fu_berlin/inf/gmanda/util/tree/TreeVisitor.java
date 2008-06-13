/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.tree;

public interface TreeVisitor<T> {
	void accept(T t);
}