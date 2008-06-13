package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;

public interface ListListener<T> {

	public void add(T e);

	public void add(int index, T element);

	public void addAll(Collection<? extends T> c);

	public void addAll(int index, Collection<? extends T> c);

	public void remove(Object o);

	public void remove(int index, Object o);

	public void clear(Collection<?> c);
	
	public void removeAll(Collection<?> c);

	public void set(int index, T oldElement, T newElement);
}
