package de.fu_berlin.inf.gmanda.util.gui;

public interface Converter<T1, T2> {
	
	public T2 convert(T1 t1);

}
