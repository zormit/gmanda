package de.fu_berlin.inf.gmanda.qda;

import java.util.Iterator;

import de.fu_berlin.inf.gmanda.qda.tagxon.TagxON;

public class CodedStringFactory {

	static TagxON t = new TagxON();

	/**
	 * Returns the CodedString using TagxOn notation contained in the given
	 * string or null if the string does not contain any codes
	 */
	public static CodedString parse(String s) {
		return t.parseCodedString(s);
	}

	/**
	 * @return the first code contained in the given String or null if the
	 *         string does not contain any code.
	 */
	public static Code parseOne(String s) {

		CodedString c = t.parseCodedString(s);
		if (c == null)
			return null;

		Iterator<? extends Code> it = c.getAllCodes().iterator();

		if (it.hasNext())
			return it.next();
		else
			return null;
	}

}
