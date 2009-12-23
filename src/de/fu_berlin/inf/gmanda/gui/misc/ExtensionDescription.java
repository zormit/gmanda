package de.fu_berlin.inf.gmanda.gui.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import de.fu_berlin.inf.gmanda.util.CMultimap;

/**
 * Easy way to define a set of file filters.
 * 
 * @see ExtensionDescriptor for a more versatile way.
 */
public class ExtensionDescription implements
		Iterable<Entry<String, Collection<String>>> {

	protected CMultimap<String, String> descriptions = new CMultimap<String, String>();

	public ExtensionDescription append(String description, String... extensions) {
		descriptions.put(description, Arrays.asList(extensions));

		return this;
	}

	@Override
	public Iterator<Entry<String, Collection<String>>> iterator() {
		return descriptions.entrySet().iterator();
	}
	
	public Collection<ExtensionDescriptor> toDescriptors(){
		
		List<ExtensionDescriptor> result = new ArrayList<ExtensionDescriptor>();
		for (final Entry<String, Collection<String>> description : this){
			result.add(new ExtensionDescriptor() {
				
				@Override
				public Collection<String> getSupportedExtensions() {
					return description.getValue();
				}
				
				@Override
				public String getDescription() {
					return description.getKey();
				}
			});
		}
		return result;
	}

	public int size() {
		return descriptions.size();
	}
}
