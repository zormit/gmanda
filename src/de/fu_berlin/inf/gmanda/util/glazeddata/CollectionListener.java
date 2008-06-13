package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;

public interface CollectionListener<T> {

	public void add(T e);

	public void addAll(Collection<? extends T> c);

	public void remove(Object o);

	public void clear(Collection<?> c);
	
	public void removeAll(Collection<?> c);

	public void set(T oldElement, T newElement);
}
