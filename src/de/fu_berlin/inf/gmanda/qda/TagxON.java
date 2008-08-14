package de.fu_berlin.inf.gmanda.qda;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.fu_berlin.inf.gmanda.util.StringUtils;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

/**
 * Parser and Emitter for Tag and Taxonomy Object Notation TagxON
 */
public class TagxON {

	public class TagxONCodedString extends AbstractCodedString {

		List<RTag> tags;

		public void add(String s) {
			s = s.trim();
			if (s.length() == 0)
				return;

			tags.add(parse(s));
		}

		public String format() {
			return StringUtils.join(tags, ",\n", new StringConverter<Code>() {

				public String toString(Code t) {
					return t.format(0, 80);
				}

			});
		}

		public Iterable<? extends Code> getAllCodes() {
			return tags;
		}

		public RTag parse(String s) {

			TagxONCodedString fullParse = TagxON.this.parse(s);

			if (fullParse.tags.size() > 0)
				return fullParse.tags.get(0);
			else
				return null;
		}

		public void remove(String s) {
			Iterator<RTag> it = tags.iterator();

			while (it.hasNext()) {
				Code code = it.next();
				if (code.getTag().equals(s))
					it.remove();
			}
		}
	}

	public abstract class RTag extends AbstractCode {

		public String toString(boolean withValue, boolean whiteSpace) {
			StringBuffer sb = new StringBuffer();

			if (whiteSpace) {
				sb.append(tag);
			} else {
				sb.append(tagTrimmed);
			}

			if (withValue && hasValue()) {
				sb.append(": ");

				if (whiteSpace) {
					sb.append(getValue());
				} else {
					sb.append(getValue().trim());
				}
			}
			return sb.toString();
		}

	}

	public class RTagLeaf extends RTag {

		String value;

		public RTagLeaf(String tag, String value) {
			this.value = value;
			setTag(tag);
		}

		public String format(int indent, int width) {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = StringUtils.countNewLinesAtBeginning(tag);

			// Easy Case: No Value!
			if (!hasValue()) {
				if (hasNewLinesAtBeginning > 1) {
					return '\n' + getTag();
				} else {
					return getTag();
				}
			}

			// Store for checking later!
			String oldValue = null;
			assert (oldValue = toString(true, false)) == oldValue;

			StringBuilder sb = new StringBuilder();
			sb.append(getTag()).append(": ");
			sb.append(StringUtils.wrap(value, sb.length(), indent, width));

			assert sb.toString().replaceAll("\\s", "").equals(oldValue.replaceAll("\\s", "")) : "\nNew Value:\n"
				+ sb.toString() + " \n\nOld Value:\n" + oldValue;

			if (hasNewLinesAtBeginning >= 2) {
				sb.insert(0, '\n');
			}

			return sb.toString();
		}

		public List<? extends Code> getProperties() {
			return Collections.singletonList(new RTagLeaf("desc", getValue()));
		}

		public String getValue() {
			return value;
		}

	}

	public class RTagComposite extends RTag {

		public LinkedList<RTag> subs = new LinkedList<RTag>();

		public String format(final int indent, final int width) {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = StringUtils.countNewLinesAtBeginning(tag);

			// Easy Case: No Value!
			if (!hasValue()) {
				if (hasNewLinesAtBeginning > 1) {
					return '\n' + StringUtils.spaces(indent) + getTag();
				} else {
					return getTag();
				}
			}

			// TODO make wrap configurable for line that would fit.
			// String unformatted = toString();
			//
			// if (unformatted.length() + indent < width &&
			// !unformatted.contains("\n")) {
			//   return unformatted;
			// }

			StringBuilder sb = new StringBuilder();

			if (hasNewLinesAtBeginning > 1) {
				sb.append('\n').append(StringUtils.spaces(indent));
			}

			sb.append(getTag()).append(":");

			if (subs.size() == 1 && subs.get(0).getTag().equals("desc")) {

				String value = StringUtils.wrap(subs.get(0).getValue(), indent + 2, indent + 2,
					width);

				if (sb.length() + value.length() < width && !value.contains("\n")){
					sb.append(" ").append(value);
				} else {
					sb.append("\n").append(StringUtils.spaces(indent + 2)).append(value);
				}
			} else {
				sb.append(" {\n");

				sb.append(StringUtils.join(subs, ",\n", new StringConverter<RTag>() {

					public String toString(RTag t) {
						return StringUtils.spaces(indent + 2) + t.format(indent + 2, width);
					}

				}));

				sb.append("\n").append(StringUtils.spaces(indent)).append("}");
			}

			return sb.toString();
		}

		public List<? extends Code> getProperties() {
			return subs;
		}

		public String getValue() {
			if (subs.size() == 0)
				return null;

			if (subs.size() == 1 && subs.get(0).getTag().equals("desc")) {
				return subs.get(0).getValue();
			}

			return "{" + StringUtils.join(subs, ", ") + "}";
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
				String nextToken = sb.toString();

				// Skip all tokens that are just made up from white space
				if (nextToken.trim().length() > 0)
					result.add(nextToken);

				result.add(String.valueOf(text[i]));
				sb = new StringBuilder();
				continue;
			}
			sb.append(text[i]);
		}

		String nextToken = sb.toString();
		// Skip all tokens that are just made up from white space
		if (nextToken.trim().length() > 0)
			result.add(nextToken);

		return result;
	}

	public void parseInternal(RTagComposite parent, ListIterator<String> it, boolean root) {

		RTagComposite currentTag = null;

		while (it.hasNext()) {
			String s = it.next();

			if (s.length() > 1 || !symbols.contains(s)) {
				if (currentTag == null) {
					// tag
					currentTag = new RTagComposite();
					currentTag.setTag(s);
				} else {
					// value
					currentTag.subs.add(new RTagLeaf("desc", s));
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
				if (currentTag != null) {
					parent.subs.add(currentTag);
					currentTag = null;
				}
				continue;
			}
			if (c == cb) {
				if (currentTag != null) {
					parent.subs.add(currentTag);
					currentTag = null;
				}
				if (!root) {
					return;
				}
				continue;
			}
		}
		if (currentTag != null) {
			parent.subs.add(currentTag);
			currentTag = null;
		}

	}

	public TagxONCodedString parse(String text) {

		if (text == null)
			return null;

		List<String> lex = lexer(text.toCharArray());

		RTagComposite root = new RTagComposite();

		parseInternal(root, lex.listIterator(), true);

		TagxONCodedString result = new TagxONCodedString();
		result.tags = root.subs;

		return result;
	}

}
