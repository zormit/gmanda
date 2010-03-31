package de.fu_berlin.inf.gmanda.qda;

public interface CodedStringCore {

	/**
	 * Add a code with the given tag to the end of this CodedString.
	 */
	void add(String tag);

	/**
	 * Remove all top-level codes with the given tag from this CodedString.
	 */
	void remove(String tag);

	/**
	 * Returns true, iff there is a top-level code, with exactely the given tag.
	 */
	boolean contains(String tag);

	/**
	 * Returns all top-level Codes of this CodedString
	 */
	Iterable<? extends Code> getAllCodes();

	/**
	 * Rename in all Codes the tags matching from to to.
	 * 
	 * @return will return a new CodedStringCore representing the result of the
	 *         rename OR this if the CodedString did not change
	 */
	CodedString rename(String from, String to);

	/**
	 * Return a pretty printed version of this CodedString
	 * 
	 * @return
	 */
	String format();

}