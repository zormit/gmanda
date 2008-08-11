package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

public interface CodedString extends CodedStringCore {

	void removeAll(List<String> selected);

	void addAll(List<String> selected);

	boolean containsAll(Iterable<String> selected);

	boolean containsAny(Iterable<? extends Code> allCodes);

	boolean containsAny(String string);

	Collection<String> getAllVariations();

	Collection<String> getAll();
	
	Collection<? extends Code> getAll(String code);

	Collection<String> getAllValues(String code);

}