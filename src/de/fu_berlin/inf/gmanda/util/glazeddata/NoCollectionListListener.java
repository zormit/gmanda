package de.fu_berlin.inf.gmanda.util.glazeddata;

public interface NoCollectionListListener<T> {
	
	public void add(T t);
	
	public void remove(T t);
	
	public void set(T oldT, T newT);

}
