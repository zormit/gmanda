package de.fu_berlin.inf.gmanda.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import com.google.common.collect.Iterables;

public class CMultimap<T,V> extends TreeMap<T, Collection<V>> {
	
	public boolean put(T key, V value) {
		Collection<V> vs = get(key); 
		
		if (vs == null){
			vs = new ArrayList<V>();
			put(key, vs);
		}
		
		return vs.add(value);
	}

	public boolean putAll(T key, Iterable<? extends V> all) {
		Collection<V> vs = get(key); 
		
		if (vs == null){
			vs = new ArrayList<V>();
			put(key, vs);
		}
		
		return Iterables.addAll(vs, all);
	};
	

}
