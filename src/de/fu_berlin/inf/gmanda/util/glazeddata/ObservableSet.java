package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class ObservableSet<T> implements Set<T> {
	
	Set<T> backing = new HashSet<T>();

	Set<CollectionListener<? super T>> listeners = new HashSet<CollectionListener<? super T>>();

	public void addListener(CollectionListener<? super T> listener) {
		listeners.add(listener);
	}

	public void removeListener(CollectionListener<? super T> listener) {
		listeners.remove(listener);
	}

	public boolean add(T e) {
		boolean result = backing.add(e);
		if (!result)
			return result;

		for (CollectionListener<? super T> listener : listeners) {
			listener.add(e);
		}

		return result;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean result = backing.addAll(c);
		if (!result)
			return result;

		for (CollectionListener<? super T> listener : listeners) {
			listener.addAll(c);
		}

		return result;
	}
	
	public void clear() {
		
		List<T> copy = new LinkedList<T>(backing);
		
		backing.clear();

		for (CollectionListener<? super T> listener : listeners) {
			listener.clear(copy);
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		
		boolean result = backing.remove(o);
	
		if (!result)
			return result;
		
		for (CollectionListener<? super T> listener : listeners) {
			listener.remove((T)o);
		}
		return result;
	}
	
	public boolean removeAll(Collection<?> c) {
		
		boolean result = backing.removeAll(c);
		
		for (CollectionListener<? super T> listener : listeners) {
			listener.removeAll(c);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public boolean retainAll(Collection<?> c) {
	
		Collection<T> removed = null;
		
		if (listeners.size() > 0)
			removed = CollectionUtils.removeAll(backing, c);
		
		boolean result = backing.retainAll(c);

		for (CollectionListener<? super T> i : listeners) {
			i.removeAll(removed);
		}
		
		return result;
	}

	public boolean contains(Object o) {
		return backing.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return backing.containsAll(c);
	}

	public boolean isEmpty() {
		return backing.isEmpty();
	}

	public Iterator<T> iterator() {
		return backing.iterator();
	}

	public int size() {
		return backing.size();
	}

	public Object[] toArray() {
		return backing.toArray();
	}

	public <S> S[] toArray(S[] a) {
		return backing.toArray(a);
	}
}
