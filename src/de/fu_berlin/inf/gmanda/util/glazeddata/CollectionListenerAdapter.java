package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;

public class CollectionListenerAdapter<T> implements ListListener<T> {

	CollectionListener<T> listener;
	
	public CollectionListenerAdapter(CollectionListener<T> listener){
		this.listener = listener;
	}
	
	public void add(T e) {
		listener.add(e);
	}

	public void add(int index, T element) {
		add(element);
	}

	public void addAll(Collection<? extends T> c) {
		listener.addAll(c);
	}

	public void addAll(int index, Collection<? extends T> c) {
		addAll(c);
	}

	public void clear(Collection<?> c) {
		listener.clear(c);
	}

	public void remove(Object o) {
		listener.remove(o);
	}

	public void remove(int index, Object o) {
		remove(o);
	}

	public void removeAll(Collection<?> c) {
		listener.removeAll(c);
	}

	public void set(int index, T oldElement, T newElement) {
		listener.set(oldElement, newElement);
	}
}
