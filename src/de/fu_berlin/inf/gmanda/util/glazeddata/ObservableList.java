package de.fu_berlin.inf.gmanda.util.glazeddata;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;

public class ObservableList<T> implements List<T> {

	public List<ListListener<? super T>> listeners = new LinkedList<ListListener<? super T>>();
	
	public List<T> backing;

	/**
	 * Creates an ObservableList with a LinkedList<T> as backing.
	 */
	public ObservableList(){
		this.backing = new LinkedList<T>();
	}
	
	public void addListener(ListListener<? super T> listener) {
		listeners.add(listener);
	}

	public void removeListener(ListListener<? super T> listener) {
		listeners.remove(listener);
	}
	
	public ObservableList(List<T> backing) {
		if (backing == null)
			throw new IllegalArgumentException();
		this.backing = backing;
	}

	public boolean add(T e) {
		boolean result = backing.add(e);

		for (ListListener<? super T> i : listeners) {
			i.add(e);
		}

		return result;
	}

	public void add(int index, T element) {
		backing.add(index, element);

		for (ListListener<? super T> i : listeners) {
			i.add(index, element);
		}
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean result = backing.addAll(c);

		for (ListListener<? super T> i : listeners) {
			i.addAll(c);
		}
		
		return result;
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		boolean result = backing.addAll(index, c);

		for (ListListener<? super T> i : listeners) {
			i.addAll(index, c);
		}

		return result;
	}

	public void clear() {
		
		List<T> copy = new LinkedList<T>(backing);
		
		backing.clear();

		for (ListListener<? super T> i : listeners) {
			i.clear(copy);
		}
	}

	public boolean remove(Object o) {
		boolean result = backing.remove(o);

		for (ListListener<? super T> i : listeners) {
			i.remove(o);
		}

		return result;
	}

	public T remove(int index) {
		T t = backing.remove(index);

		for (ListListener<? super T> i : listeners) {
			i.remove(index, t);
		}
		
		return t;
	}

	public boolean removeAll(Collection<?> c) {
		boolean result = backing.removeAll(c);

		for (ListListener<? super T> i : listeners) {
			i.removeAll(c);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean retainAll(Collection<?> c) {
		
		Collection<T> removed = null;
		
		if (listeners.size() > 0)
			removed = CollectionUtils.removeAll(backing, c);
		
		boolean result = backing.retainAll(c);

		for (ListListener<? super T> i : listeners) {
			i.removeAll(removed);
		}
		
		return result;
	}

	public T set(int index, T element) {
		T t = backing.set(index, element);
		
		for (ListListener<? super T> i : listeners) {
			i.set(index, t, element);
		}
		
		return t;
	}

	public boolean contains(Object o) {
		return backing.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return backing.containsAll(c);
	}

	public T get(int index) {
		return backing.get(index);
	}

	public int indexOf(Object o) {
		return backing.indexOf(o);
	}

	public boolean isEmpty() {
		return backing.isEmpty();
	}

	public Iterator<T> iterator() {
		return backing.iterator();
	}

	public int lastIndexOf(Object o) {
		return backing.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return backing.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return backing.listIterator(index);
	}

	public int size() {
		return backing.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return backing.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return backing.toArray();
	}

	public <S> S[] toArray(S[] a) {
		return backing.toArray(a);
	}

}
