/*
 * Created on 09.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author oezbek
 * 
 */
public class StringUtils {

	/**
	 * 
	 * @param word
	 *            The word that is supposed to get a plural s depending on the
	 *            number
	 * @param number
	 *            The number of items.
	 * @return Will return "no words" if number is zero, "1 word" if number is
	 *         one and "number words" if number is anything else
	 */
	public static String pluralS(int i, String s) {
		return "" + (i != 0 ? "" + i : "no") + " " + s + (i != 1 ? "s" : "");
	}
	
	public static String commonPrefix(String a, String b){
		if (a == null || b == null)
			throw new NullPointerException();
		
		char[] c = a.toCharArray();
		char[] d = b.toCharArray();
		
		int max = Math.min(c.length, d.length);
		
		int i = 0;
		while (i < max && c[i] == d[i]){
			i++;
		}
		return a.substring(0, i);
	}

	/**
	 * Returns a concatenation of all strings given in the collection,
	 * separating each two strings with the given separator.
	 */
	public static String join(Collection<String> strings, String separator) {
		return join(strings, separator, new PlainConverter<String>());
	}

	public interface StringConverter<T> {
		public String toString(T t);
	}
	
	public interface FromConverter<T> {
		public T fromString(String s);
	}

	public static class PlainConverter<T> implements StringConverter<T> {
		public String toString(Object t) {
			return t.toString();
		}
	}

	public static class JoinConverter<T> implements StringConverter<Collection<T>> {

		String separator;
		StringConverter<? super T> converter;

		public JoinConverter(String separator) {
			this.separator = separator;
			this.converter = new PlainConverter<T>();
		}

		public JoinConverter(String separator, StringConverter<T> subconverter) {
			this.separator = separator;
			this.converter = subconverter;
		}

		public String toString(Collection<T> t) {
			return StringUtils.join(t, separator, converter);
		}
	}

	public static <T> String join(Collection<T> strings, String separator,
		StringConverter<? super T> converter) {
		Iterator<T> it = strings.iterator();

		StringBuilder sb = new StringBuilder();
		if (it.hasNext())
			sb.append(converter.toString(it.next()));
		while (it.hasNext()) {
			sb.append(separator).append(converter.toString(it.next()));
		}
		return sb.toString();
	}
	
	public static String indent(String s, int i){
		StringIBuilder sb = new StringIBuilder();
		sb.indent(i);
		sb.append(s);
		return sb.toString();
	}

}
