package de.fu_berlin.inf.gmanda.qda.tagxon;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.qda.AbstractCode;
import de.fu_berlin.inf.gmanda.qda.AbstractCodedString;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.util.CStringUtils;
import de.fu_berlin.inf.gmanda.util.CStringUtils.StringConverter;

/**
 * Parser and Emitter for Tag and Taxonomy Object Notation TagxON
 */
public class TagxON {

	public class TagxONCodedString extends AbstractCodedString {

		List<RTag> tags;

		public CodedString rename(String fromRename, String toRename) {

			TagxONCodedString result = new TagxONCodedString();
			result.tags = new LinkedList<RTag>();

			boolean changed = false;

			for (Code c : getAllCodes()) {
				RTag newCode = (RTag) c.renameTag(fromRename, toRename);
				if (newCode != c)
					changed = true;

				tags.add(newCode);
			}

			if (changed)
				return result;
			else
				return this;
		}

		public void add(String s) {
			s = s.trim();
			if (s.length() == 0)
				return;

			tags.add(parse(s));
		}

		public String format() {
			return CStringUtils.join(tags, ",\n", new StringConverter<Code>() {

				public String toString(Code t) {
					return t.format(0, 80);
				}

			});
		}

		public Iterable<? extends Code> getAllCodes() {
			return tags;
		}

		public RTag parse(String s) {

			TagxONCodedString fullParse = TagxON.this.parseCodedString(s);

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

		public RTag(String tag) {
			super(tag);
		}

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

		public Code renameTag(String fromRename, String toRename) {

			if (!tag.contains(fromRename))
				return this;

			String newTag = tag.replace(fromRename, toRename);

			if (newTag.equals(tag))
				return this;

			if (newTag.trim().equals("") && hasValue()) {
				newTag = "???.orphaned description";
			}

			return new RTagLeaf(newTag, value);
		}

		public Object clone() {
			RTagLeaf clone;
			try {
				clone = (RTagLeaf) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new DoNotShowToUserException(e);
			}
			clone.value = value;
			return clone;
		}

		public RTagLeaf(String tag, String value) {
			super(tag);
			this.value = value;
		}

		public String format(int indent, int width) {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = CStringUtils.countNewLinesAtBeginning(tag);

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
			sb.append(CStringUtils.wrap(value, sb.length(), indent, width));

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

		public RTagComposite(String tag) {
			super(tag);
		}

		public Code renameTag(String fromRename, String toRename) {

			boolean changed = false;

			String newTag;
			if (tag.contains(fromRename)) {
				newTag = tag.replace(fromRename, toRename);

				if (!newTag.equals(tag)) {
					changed = true;
					if (newTag.trim().equals("") && hasValue()) {
						newTag = "???.orphaned description";
					}
				}
			} else {
				newTag = tag;
			}

			RTagComposite result = new RTagComposite(newTag);

			for (RTag c : subs) {
				RTag newCode = (RTag) c.renameTag(fromRename, toRename);
				if (newCode != c)
					changed = true;

				result.subs.add(newCode);
			}

			if (changed)
				return result;
			else
				return this;
		}

		public LinkedList<RTag> subs = new LinkedList<RTag>();

		public String format(final int indent, final int width) {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = CStringUtils.countNewLinesAtBeginning(tag);

			// Easy Case: No Value!
			if (!hasValue()) {
				if (hasNewLinesAtBeginning > 1) {
					return '\n' + CStringUtils.spaces(indent) + getTag();
				} else {
					return getTag();
				}
			}

			// TODO make wrap configurable for line that would fit.
			// String unformatted = toString();
			//
			// if (unformatted.length() + indent < width &&
			// !unformatted.contains("\n")) {
			// return unformatted;
			// }

			StringBuilder sb = new StringBuilder();

			if (hasNewLinesAtBeginning > 1) {
				sb.append('\n').append(CStringUtils.spaces(indent));
			}

			sb.append(getTag()).append(":");

			if (subs.size() == 1 && subs.get(0).getTag().equals("desc")) {

				String value = CStringUtils.wrap(subs.get(0).getValue(), indent + 2, indent + 2,
					width);

				if (sb.length() + value.length() < width && !value.contains("\n")) {
					sb.append(" ").append(value);
				} else {
					sb.append("\n").append(CStringUtils.spaces(indent + 2)).append(value);
				}
			} else {
				sb.append(" {\n");

				sb.append(CStringUtils.join(subs, ",\n", new StringConverter<RTag>() {

					public String toString(RTag t) {
						return CStringUtils.spaces(indent + 2) + t.format(indent + 2, width);
					}

				}));

				sb.append("\n").append(CStringUtils.spaces(indent)).append("}");
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

			return "{" + CStringUtils.join(subs, ", ") + "}";
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
				String skipped = CStringUtils.skipAhead(quote, escape, text, i);
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
					currentTag = new RTagComposite(s);
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

	public TagxONCodedString parseCodedString(String text) {

		if (text == null)
			return null;

		List<String> lex = lexer(text.toCharArray());

		RTagComposite root = new RTagComposite("root");

		parseInternal(root, lex.listIterator(), true);

		TagxONCodedString result = new TagxONCodedString();
		result.tags = root.subs;

		return result;
	}

}
