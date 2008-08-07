package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.List;

public interface Code {
	
	public String getCode();

	public boolean hasValue();
	
	public String getValue();

	public Collection<String> getVariations();

	public String toString(boolean withValue, boolean whiteSpace);

	public boolean rename(String fromRename, String toRename);

	public boolean matches(Code c);

	public String format();

	public boolean matchesAny(Iterable<Code> allCodes);
	
	public List<Code> getProperties();
}
