/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.tree;

import java.util.Collection;

public interface TreeStructure<T> {
	T get();

	Collection<T> getChildren();
}