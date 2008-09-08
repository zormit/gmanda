package de.fu_berlin.inf.gmanda.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class CMultimap<T, V> extends TreeMap<T, Collection<V>> {

	Supplier<? extends Collection<V>> collectionSupplier;

	public CMultimap(Multimap<T,V> from){
		this();
		for (Map.Entry<T, Collection<V>> entry : from.asMap().entrySet()){
			putAll(entry.getKey(), entry.getValue());
		}
	}
	
	
	/**
	 * Create a new empty CMultimap using ArrayLists for managing the values to
	 * each key.
	 */
	public CMultimap() {
		collectionSupplier = new Supplier<Collection<V>>() {
			public Collection<V> get() {
				return new ArrayList<V>();
			}
		};
	}

	/**
	 * To use another backing datastructures for managing the values to each
	 * key, provide a supplier that should return an empty collection.
	 */
	public CMultimap(Supplier<? extends Collection<V>> supplier) {
		collectionSupplier = supplier;
	}

	protected Collection<V> getInternal(T key) {
		Collection<V> vs = get(key);

		if (vs == null) {
			vs = collectionSupplier.get();
			put(key, vs);
		}
		return vs;
	}

	public boolean put(T key, V value) {
		return getInternal(key).add(value);
	}

	public boolean putAll(T key, Iterable<? extends V> all) {
		return Iterables.addAll(getInternal(key), all);
	}
}
