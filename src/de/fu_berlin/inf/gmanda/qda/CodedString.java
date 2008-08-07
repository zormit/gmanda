package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

public interface CodedString {

	void removeAll(List<String> selected);

	void addAll(List<String> selected);

	boolean containsAll(Iterable<String> selected);
	
	boolean containsAny(Iterable<Code> allCodes);
	
	boolean containsAny(String string);
	
	boolean containsInAllSegments(String nextSearchString);
	
	Collection<String> getAllVariations();

	Collection<String> getAll();

	Collection<String> getAllValues(String code);

	Iterable<Code> getAllCodes();
	
	boolean rename(String string, String string2);

	String format();

}