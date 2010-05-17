package de.fu_berlin.inf.gmanda.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

public class CUtils {

	/**
	 * Returns at most max elements from the beginning of the given iterable.
	 */
	public static <T> LinkedList<T> first(Iterable<T> it, int max) {

		LinkedList<T> result = new LinkedList<T>();

		int i = 0;
		for (T t : it) {
			if (i >= max)
				return result;
			result.add(t);
			i++;
		}
		return result;
	}

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static int size(Iterable<?> t) {
		int result = 0;

		for (@SuppressWarnings("unused")
		Object o : t) {
			result++;
		}

		return result;
	}

	public static String cleanAuthor(String author) {
		author = author.replaceFirst("<.*$", "");
		author = author.replaceAll("[\"\\\\]", "").trim();

		// Flip "Doe, John" into "John Doe"
		author = author.replaceAll("(\\w*?), *(.*)$", "$2 $1");
		
		// Add space after dot
		author = author.replaceAll("\\.", ". ");
		
		// Collapse spaces
		author = author.replaceAll("\\s+", " ");
		
		// Make case canonical
		author = upperCaseEachFirstCharacter(author);

		// Shorten to 30 characters max
		if (author.length() >= 30) {
			author = author.substring(0, 27) + "...";
		}

		// Return as ASCII
		return CStringUtils.convertNonAscii(author);
	}

	public static String upperCaseEachFirstCharacter(String input) {

		input = input.toLowerCase();

		StringBuilder result = new StringBuilder();

		for (String word : input.split("\\s+")) {
			result.append(upperCaseFirstCharacter(word));
			result.append(" ");
		}
		
		return result.toString().trim();
	}

	public static Object upperCaseFirstCharacter(String s) {
		if (s.length() > 1)
			return s.substring(0, 1).toUpperCase()
					+ s.substring(1).toLowerCase();
		else
			return s.toUpperCase();
	}

	public static String cleanTitle(String name) {
		name = name.replaceAll("\\s+", " ");
		name = name.trim();
		return CStringUtils.convertNonAscii(name);
	}

	public static Image loadImageResource(String resourcePath) {
		return Toolkit.getDefaultToolkit().getImage(getResource(resourcePath));
	}

	public static URL getResource(String resourcePath) {
		return CUtils.class.getClassLoader().getResource(resourcePath);
	}

	public static String getResourceAsString(String resourcePath) throws IOException {
		return IOUtils.toString(CUtils.getResourceAsStream(resourcePath));
	}

	public static InputStream getResourceAsStream(String resourcePath) throws IOException {
		return getResource(resourcePath)
				.openConnection().getInputStream();
	}

	public static <S, T extends Collection<S>> T addAll(
			T collection,
			Iterable<S> iterable) {
		CollectionUtils.addAll(collection, iterable.iterator());
		return collection;
	}

}
