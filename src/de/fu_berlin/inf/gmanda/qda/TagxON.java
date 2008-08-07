package de.fu_berlin.inf.gmanda.qda;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.fu_berlin.inf.gmanda.util.StringUtils;

/**
 * Parser and Emitter for Tag and Taxonomy Object Notation TagxON
 */
public class TagxON {

	public class RTag {

		public LinkedList<RTag> subs = new LinkedList<RTag>();

		String name;

		String value;

		public String toString() {
			
			StringBuffer sb = new StringBuffer();
			
			sb.append(name);
			if (value != null || subs.size() > 0){
				sb.append(" : ");
			}
			if (value != null){
				sb.append(value);
			}
			if (subs.size() > 0){
				if (value != null){
					sb.append(" ");
				}
				
				sb.append("{ ").append(StringUtils.join(subs, ", ")).append(" }");
			}
			return sb.toString();
		}
	}

	public char quote = '"';

	public char escape = '\\';

	public char eq = ':';

	public char ob = '{';

	public char cb = '}';

	public char co = ',';

	public String symbols = "" + eq + ob + cb + co;

	List<String> lexer(char[] text) {

		List<String> result = new LinkedList<String>();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length; i++) {

			if (text[i] == quote) {
				String skipped = StringUtils.skipAhead(quote, escape, text, i);
				i += skipped.length() - 1;
				result.add(skipped);
				continue;
			}
			if (symbols.contains(String.valueOf(text[i]))) {
				String segment = sb.toString().trim();
				if (segment.length() > 0)
					result.add(segment);

				result.add(String.valueOf(text[i]));
				sb = new StringBuilder();
				continue;
			}
			sb.append(text[i]);
		}
		String segment = sb.toString().trim();
		if (segment.length() > 0)
			result.add(segment);
		
		return result;
	}

	public void parseInternal(RTag parent, ListIterator<String> it, boolean root) {

		RTag currentTag = null;

		while (it.hasNext()) {
			String s = it.next();

			if (s.length() > 1 || !symbols.contains(s)) {
				if (currentTag == null) {
					// tag
					currentTag = new RTag();
					currentTag.name = s;
				} else {
					// value
					currentTag.value = s;
				}
				continue;
			}
			
			char c = s.charAt(0);

			if (c == co) {
				// Comma
				if (currentTag != null) {
					parent.subs.add(currentTag);
					currentTag = null;
				}
				continue;
			}
			if (c == ob) {

				if (currentTag == null) {
					parseInternal(parent, it, false);
				} else {
					parseInternal(currentTag, it, false);
				}
				continue;
			}
			if (c == cb){
				if (currentTag != null){
					parent.subs.add(currentTag);
					currentTag = null;
				}
				if (!root) { 
					return;
				}
			}
		}
		if (currentTag != null){
			parent.subs.add(currentTag);
			currentTag = null;
		}
		
	}

	public List<RTag> parse(String text) {

		if (text == null)
			return Collections.emptyList();

		List<String> lex = lexer(text.toCharArray());

		RTag root = new RTag();

		parseInternal(root, lex.listIterator(), true);

		return root.subs;
	}

}
