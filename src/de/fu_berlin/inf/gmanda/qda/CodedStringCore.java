package de.fu_berlin.inf.gmanda.qda;


public interface CodedStringCore {

	void add(String s);

	void remove(String s);

	boolean contains(String string);

	Iterable<? extends Code> getAllCodes();

	boolean rename(String string, String string2);

	String format();
	
	Code parse(String s);

}