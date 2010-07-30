package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import de.fu_berlin.inf.gmanda.util.tree.TreeMaker;
import de.fu_berlin.inf.gmanda.util.tree.TreeStructure;
import de.fu_berlin.inf.gmanda.util.tree.TreeWalker;

public abstract class AbstractCodedString implements CodedString {

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getAll() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodes()) {
			variations.add(c.getTag());
		}
		return variations;
	}

	public Collection<String> getAllDeep() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodesDeep()) {
			variations.add(c.getTag());
		}
		return variations;
	}

	public Collection<? extends Code> getAllDeep(String code) {
		code = code.trim();
		Collection<Code> result = new LinkedList<Code>();
		for (Code c : getAllCodesDeep()) {
			if (c.getTag().equals(code))
				result.add(c);
		}
		return result;
	}

	public Iterable<? extends Code> getAllCodesDeep() {

		TreeWalker<? extends Code> result = new TreeWalker<Code>(getAllCodes(),
				new TreeMaker<Code>() {

					public TreeStructure<Code> toStructure(final Code t) {
						return new TreeStructure<Code>() {

							public Code get() {
								return t;
							}

							public Iterable<Code> getChildren() {
								List<? extends Code> children = t
										.getProperties();
								if (children.size() == 1
										&& children.get(0).getTag().equals(
												"desc"))
									return Collections.emptyList();
								else {
									// Casting here is not a problem, since
									// Iterators are read only
									@SuppressWarnings("unchecked")
									Iterable<Code> result = (Iterable<Code>) children;

									return result;
								}

							}
						};
					}

				});

		return result;
	}

	public Collection<Code> getAll(String code) {
		code = code.trim();
		Collection<Code> result = new LinkedList<Code>();
		for (Code c : getAllCodes()) {
			if (c.getTag().equals(code))
				result.add(c);
		}
		return result;
	}

	public Collection<? extends Code> getProperties(String code, String propName) {
		return getProperties(getAll(code), propName);
	}

	public static Collection<? extends Code> getProperties(
			Collection<? extends Code> codes, String propName) {
		List<Code> result = new LinkedList<Code>();
		for (Code c : codes) {
			result.addAll(getProperties(c, propName));
		}
		return result;
	}

	public static List<Code> getProperties(Code c, String propName) {
		List<Code> result = new LinkedList<Code>();

		for (Code sub : c.getProperties()) {
			if (sub.getTag().equals(propName)) {
				result.add(sub);
			}
		}
		return result;
	}

	/**
	 * Returns the first property in the given code matching the given name, or
	 * null.
	 */
	public static Code getFirstProperty(Code c, String propertyName) {
		for (Code prop : getProperties(c, propertyName)) {
			return prop;
		}
		return null;
	}

	/**
	 * Returns the text value of the first property of the given code with the
	 * given property name or the given defaultValue.
	 */
	public static String getFirstPropertyValue(Code c, String propertyName,
			String defaultValue) {
		Code c2 = getFirstProperty(c, propertyName);

		if (c2 == null)
			return defaultValue;

		String result = c2.getValue();

		if (result == null || (result = result.trim()).length() == 0)
			return defaultValue;

		return result;
	}

	/**
	 * Returns the cleaned text value of the first property of the given code
	 * with the given property name or the given defaultValue.
	 * 
	 * Cleaning means that the value is stripped of line-endings, spaces, commas
	 * and periods and is unescaped from Java notation.
	 */
	public static String getFirstPropertyValueClean(Code c,
			String propertyName, String defaultValue) {

		String result = getFirstPropertyValue(c, propertyName, null);

		if (result == null)
			return defaultValue;

		result = StringUtils.strip(result, ",. \r\n\f\t'\"");
		result = StringEscapeUtils.unescapeJava(result).trim();

		if (result.length() == 0)
			return defaultValue;

		return result;
	}

	public Collection<String> getAllValues(String code) {

		Collection<String> result = new LinkedList<String>();
		for (Code c : getAll(code)) {
			String value = c.getValue();
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}

	public Collection<String> getAllVariationsDeep() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodesDeep()) {
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
	 * {@inheritDoc}
	 */
	public boolean contains(String s) {

		for (Code c : getAllCodes()) {
			if (c.getTag().equals(s))
				return true;
		}
		return false;
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

	public boolean containsAnyDeep(String code) {

		Code search = parse(code);

		for (Code c : getAllCodesDeep()) {

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
