package de.fu_berlin.inf.gmanda.util;

import java.util.Iterator;

public class IterableUnroller<T> implements Iterable<T> {

	Iterable<? extends Iterable<T>> collection;
	
	public IterableUnroller(Iterable<? extends Iterable<T>> t){
		this.collection = t;
	}
	
	public Iterator<T> iterator() {
		return new Iterator<T>(){
			Iterator<? extends Iterable<T>> it1 = collection.iterator();
			Iterator<T> it2;
			
			public boolean hasNext() {
				if (it2 == null || !it2.hasNext())
					return it1.hasNext();
				
				return it2.hasNext();
			}

			public T next() {
				while (it2 == null || !it2.hasNext()){
					it2 = it1.next().iterator();
				}
				return it2.next();
			}

			public void remove() {
				it2.remove();
			}
		};
	}
}
