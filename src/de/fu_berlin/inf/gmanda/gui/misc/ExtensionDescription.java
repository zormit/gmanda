package de.fu_berlin.inf.gmanda.gui.misc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import de.fu_berlin.inf.gmanda.util.CMultimap;

public class ExtensionDescription implements Iterable<Entry<String, Collection<String>>> {
	
	protected CMultimap<String, String> descriptions = new CMultimap<String, String>(); 
	
	public ExtensionDescription append(String description, String...extensions){
		descriptions.put(description, Arrays.asList(extensions));
		
		return this;
	}

	@Override
	public Iterator<Entry<String, Collection<String>>> iterator() {
		return descriptions.entrySet().iterator();
	}
}
