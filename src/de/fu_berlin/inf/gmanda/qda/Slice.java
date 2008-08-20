package de.fu_berlin.inf.gmanda.qda;

import java.util.Map;

import de.fu_berlin.inf.gmanda.util.CMultimap;

/**
 * Immutable
 */
public interface Slice {
	
	/**
	 * Returns the documents currently presented by this slice
	 * @return
	 */
	public CMultimap<PrimaryDocument, Code> getDocuments();
	
	/**
	 * Taking the current set of documents, returns a slice centerd on the given code.
	 */
	public Slice select(Code code);
		 
	/**
	 * Returns a subset of the this slice, by filtering all documents 
	 * that do not contain the given code
	 * at the currently selected level
	 */
	public Slice filter(Code code);
	
	/**
	 * Returns a map of subslices based on the trimmed values for the given code. 
	 */
	public Map<String, Slice> slice();
	

}
