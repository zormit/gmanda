package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCodedString implements CodedString {

	/**
	 * Returns a list of all plain codes (without values behind the =)
	 * 
	 * @return
	 */
	public Collection<String> getAll() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodes()) {
			variations.add(c.getTag());
		}
		return variations;
	}
	
	public Collection<Code> getAll(String code){
		code = code.trim();
		Collection<Code> result = new LinkedList<Code>();
		for (Code c : getAllCodes()) {
			if (c.getTag().equals(code))
				result.add(c);
		}
		return result;
	}
	
	public Collection<? extends Code> getProperties(String code, String propName){
		return getProperties(getAll(code), propName);
	}
	
	public static Collection<? extends Code> getProperties(Collection<? extends Code> codes, String propName){
		
		List<Code> result = new LinkedList<Code>();
		
		for (Code c : codes){
			for (Code sub : c.getProperties()){
				if (sub.getTag().equals(propName)){
					result.add(sub);
				}
			}
		}
		return result;
	}

	public Collection<String> getAllValues(String code) {
		
		Collection<String> result = new LinkedList<String>();
		for (Code c : getAll(code)){
			String value = c.getValue();
			if (value != null){
				result.add(value);
			}
		}
		return result;
	}

	public Collection<String> getAllVariations() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodes()) {
			variations.addAll(c.getTagVariations());
		}
		return variations;
	}

	public void addAll(List<String> toAdd) {
		for (String s : toAdd) {
			if (!contains(s))
				add(s);
		}
	}

	public void removeAll(List<String> toRemove) {
		for (String s : toRemove) {
			remove(s);
		}
	}

	/**
	 * Check for the given code by exact match
	 */
	public boolean contains(String s) {

		for (Code c : getAllCodes()) {
			if (c.getTag().equals(s))
				return true;
		}
		return false;
	}

	public boolean rename(String fromRename, String toRename) {

		boolean result = false;

		for (Code c : getAllCodes()) {
			result |= c.renameTag(fromRename, toRename);
		}
		return result;
	}

	public boolean containsAll(Iterable<String> codes) {
		for (String s : codes) {
			if (!contains(s))
				return false;
		}
		return true;
	}

	public boolean containsAny(String code) {

		Code search = parse(code);

		for (Code c : getAllCodes()) {

			if (search.matches(c))
				return true;
		}
		return false;

	}

	public boolean containsAny(Iterable<? extends Code> codes) {

		for (Code c : getAllCodes()) {

			for (Code search : codes) {

				if (search.matches(c))
					return true;
			}
		}
		return false;
	}
}
