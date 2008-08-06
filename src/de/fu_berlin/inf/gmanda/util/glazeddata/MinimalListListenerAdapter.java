package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;


public class MinimalListListenerAdapter<T> implements ListListener<T> {

	MinimalListListener<T> listener;
	
	public MinimalListListenerAdapter(MinimalListListener<T> listener){
		this.listener = listener;
	}
	
	public void add(T e) {
		listener.add(e);
	}

	public void add(int index, T element) {
		add(element);
	}

	public void addAll(Collection<? extends T> c) {
		for (T t : c){
			add(t);
		}
	}

	public void addAll(int index, Collection<? extends T> c) {
		addAll(c);
	}

	public void clear(Collection<?> c) {
		removeAll(c);
	}

	@SuppressWarnings("unchecked")
	public void remove(Object o) {
		listener.remove((T)o);
	}

	public void remove(int index, Object o) {
		remove(o);
	}

	public void removeAll(Collection<?> c) {
		for (Object t : c){
			remove(t);
		}
	}

	public void set(int index, T oldElement, T newElement) {
		remove(oldElement);
		add(newElement);
	}
}
