/*
 * Created on 09.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
	public static <T> String join(Collection<T> strings, String separator) {
		return join(strings, separator, new PlainConverter<T>());
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
	
	/**
	 * Starting from position pos in text will capture all chars until c is
	 * found (including), returning the resulting string.
	 * 
	 * @param c
	 * @param text
	 * @param pos
	 * @return
	 */
	public static String skipAhead(char c, char[] text, int pos) {

		StringBuilder sb = new StringBuilder();

		sb.append(text[pos++]);
		while (pos < text.length && text[pos] != c) {
			sb.append(text[pos]);
			pos++;
		}
		if (pos < text.length)
			sb.append(text[pos]);

		return sb.toString();
	}
	
	/**
	 * Starting from position pos in text will capture all chars until delim is
	 * found (including), returning the resulting string.
	 * 
	 * If a character escape preceedes the delimiter it is not interpreted 
	 * as finishing the capture.
	 */
	public static String skipAhead(char delim, char escape, char[] text, int pos){
		
		StringBuilder sb = new StringBuilder();

		sb.append(text[pos++]);
		
		while (pos < text.length && text[pos] != delim) {
			
			if (text[pos] == escape){
				while (pos < text.length && text[pos] == escape){
					sb.append(text[pos]);
					pos++;
				}
			}
		
			if (pos < text.length){
				sb.append(text[pos]);
				pos++;
			}
		}
		if (pos < text.length)
			sb.append(text[pos]);

		return sb.toString();
	}
	
	

	public static List<String> split(String s, char separator, char escapeChar) {
		List<String> result = new LinkedList<String>();

		if (s == null)
			return result;

		StringBuilder sb = new StringBuilder();

		char[] text = s.toCharArray();

		for (int i = 0; i < text.length; i++) {

			if (text[i] == escapeChar) {
				String skipped = skipAhead('\"', text, i);
				i += skipped.length() - 1;
				sb.append(skipped);
				continue;
			}
			if (text[i] == separator) {
				String segment = sb.toString();
				if (segment.length() > 0)
					result.add(segment);
				sb = new StringBuilder();
				continue;
			}
			sb.append(text[i]);
		}
		String segment = sb.toString();
		if (segment.length() > 0)
			result.add(segment);

		return result;
	}

	

}
