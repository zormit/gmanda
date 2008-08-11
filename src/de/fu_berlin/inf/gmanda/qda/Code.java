package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

public interface Code {
	
	public String getTag();

	public boolean hasValue();
	
	public String getValue();

	public Collection<String> getTagVariations();

	public String toString(boolean withValue, boolean whiteSpace);

	public boolean renameTag(String fromRename, String toRename);

	public boolean matches(Code c);

	/**
	 * Returns a formatted string of the code. Every line after the first should be indented with given number of spaces.
	 * 
	 * The formatter should break lines at the given width. 
	 */
	public String format(int indent, int width);

	public boolean matchesAny(Iterable<? extends Code> allCodes);
	
	public List<? extends Code> getProperties();
}
