package de.fu_berlin.inf.gmanda.util.gui;


public class NotNullConverter<T> implements Converter<T, Boolean> {
	
	public Boolean convert(T t){
		return t != null;
	}

}
