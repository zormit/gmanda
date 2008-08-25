package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

/**
 * Immutable
 */
public interface Code {
	
	/**
	 * Returns the whole (trimmed) tag of this code.
	 * 
	 * For instance for a code 
	 * 
	 * concept.confusion: "Hello"
	 * 
	 * This method returns 'concept.confusion'
	 */
	public String getTag();

	/**
	 * Returns true, if this Code has a value.
	 * 
	 * For instance for a code 
	 * 
	 * concept.confusion: "Hello"
	 * 
	 * This method returns 'true'
	 */
	public boolean hasValue();
	
	/**
	 * Returns true, if this Code has a value.
	 * 
	 * For instance for a code 
	 * 
	 * concept.confusion: { value: "Hello", desc: "Good" }
	 * 
	 * This method returns 
	 * 
	 * { value: "Hello", desc: "Good" }
	 */
	public String getValue();

	public Collection<String> getTagVariations();
	
	public Collection<String> getTagLevels();

	public String toString(boolean withValue, boolean whiteSpace);

	/**
	 * Uses the given fromRename as a literal expression for calling 
	 * 
	 * tag.replace(fromRename, toRename)
	 * 
	 * If the Code changes a new instance is returned, otherwise this is returned.
	 */
	public Code renameTag(String fromRename, String toRename);

	public boolean matches(Code c);

	/**
	 * Returns a formatted string of the code. Every line after the first should be indented with given number of spaces.
	 * 
	 * The formatter should break lines at the given width. 
	 */
	public String format(int indent, int width);

	public boolean matchesAny(Iterable<? extends Code> allCodes);

	/**
	 * Returns all sub codes of this code
	 */
	public List<? extends Code> getProperties();
	
	/**
	 * Returns a collection of all those codes from getProperties that have the given name (tag).
	 */
	public Collection<? extends Code> getProperties(String propName);
}
