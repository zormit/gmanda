package de.fu_berlin.inf.gmanda.qda;

import java.util.Map;

import de.fu_berlin.inf.gmanda.util.CMultimap;

/**
 * Immutable
 */
public interface Slice<T> {
	
	
	
	/**
	 * Returns the documents currently presented by this slice
	 * @return
	 */
	public CMultimap<T, Code> getDocuments();
	
	/**
	 * Taking the current set of documents, returns a slice centerd on the given code.
	 */
	public Slice<T> select(Code code);
		 
	/**
	 * Returns a subset of the this slice, by filtering all documents 
	 * that do not contain the given code
	 * at the currently selected level
	 */
	public Slice<T> filter(Code code);
	
	/**
	 * Returns a map of subslices based on the trimmed values for the given code. 
	 */
	public Map<String, Slice<T>> slice();
	
	public Map<String, Slice<T>> slice(String by, int depth);
	
	public Slice<String> sliceAndPack(String by, int depth);
		
	

}
