package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;


public class AnyChangeListListenerAdapter<T> implements ListListener<T> {

	AnyChangeListListener<T> listener;
	
	public AnyChangeListListenerAdapter(AnyChangeListListener<T> listener){
		this.listener = listener;
	}
	
	public void add(T e) {
		listener.changed();
	}

	public void add(int index, T element) {
		listener.changed();
	}

	public void addAll(Collection<? extends T> c) {
		listener.changed();
	}

	public void addAll(int index, Collection<? extends T> c) {
		listener.changed();
	}

	public void clear(Collection<?> c) {
		listener.changed();
	}

	public void remove(Object o) {
		listener.changed();
	}

	public void remove(int index, Object o) {
		listener.changed();
	}

	public void removeAll(Collection<?> c) {
		listener.changed();
	}

	public void set(int index, T oldElement, T newElement) {
		listener.changed();
	}
}
