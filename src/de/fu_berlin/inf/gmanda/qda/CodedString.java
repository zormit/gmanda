package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

public interface CodedString extends CodedStringCore {

	public Code parse(String s);
	
	void removeAll(List<String> selected);

	void addAll(List<String> selected);

	boolean containsAll(Iterable<String> selected);

	boolean containsAny(Iterable<? extends Code> allCodes);

	boolean containsAny(String string);

	Collection<String> getAllVariationsDeep();

	/**
	 * Returns a list of all plain codes (without values behind the =)
	 * 
	 * @return
	 */
	Collection<String> getAll();
	
	/**
	 * Returns a list of all codes nested arbitrarily deep
	 * 
	 * @return
	 */
	Collection<String> getAllDeep();
	
	Collection<? extends Code> getAll(String code);
	
	Collection<? extends Code> getAllDeep(String code);

	Collection<String> getAllValues(String code);
	
	Collection<? extends Code> getProperties(String code, String propName);

	Iterable<? extends Code> getAllCodesDeep();	
}