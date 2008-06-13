package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import de.fu_berlin.inf.gmanda.util.IterableUnroller;
import de.fu_berlin.inf.gmanda.util.StringUtils;
import de.fu_berlin.inf.gmanda.util.StringUtils.JoinConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.PlainConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

public class CodedString {

	public class Code {

		public Code(String singleCode) {
			int index = singleCode.indexOf('=');

			if (index != -1 && index + 1 < singleCode.length()) {
				value = singleCode.substring(index + 1);
				singleCode = singleCode.substring(0, index);
			}

			setWholeCode(singleCode);
		}

		public void setWholeCode(String singleCode) {

			wholeCodeUntrimmed = singleCode;

			codeLevels = new LinkedList<String>();

			for (String level : singleCode.trim().split("\\.")) {
				if (level.trim().length() > 0)
					codeLevels.add(level);
			}
		}

		/**
		 * Derived from wholeCode
		 */
		LinkedList<String> codeLevels;

		String value;
		String wholeCodeUntrimmed;

		public String getValue() {
			return value;
		}

		public String toString(boolean withValue, boolean whiteSpace) {
			StringBuilder sb = new StringBuilder();

			if (whiteSpace) {
				sb.append(wholeCodeUntrimmed);
			} else {
				sb.append(wholeCodeUntrimmed.trim());
			}

			if (withValue && value != null && value.trim().length() > 0) {
				sb.append('=');
				if (whiteSpace)
					sb.append(value);
				else
					sb.append(value.trim());
			}
			return sb.toString();
		}

		public String toString() {
			return toString(true, false);
		}

		public boolean rename(String fromRename, String toRename) {

			if (!wholeCodeUntrimmed.contains(fromRename))
				return false;

			String newWholeCode = wholeCodeUntrimmed.replace(fromRename, toRename);

			if (newWholeCode.trim().equals("") && value != null && !value.trim().equals("")) {
				setWholeCode("???.orphaned description");
			} else {
				setWholeCode(newWholeCode);
			}

			return true;
		}

		public Collection<String> getVariations() {
			Collection<String> variations = new LinkedList<String>();

			StringBuilder sb = new StringBuilder();

			for (String string : codeLevels) {
				if (sb.length() > 0) {
					sb.append('.');
				}
				sb.append(string);
				variations.add(sb.toString());
			}
			return variations;
		}

		public boolean matchesAny(Iterable<Code> codes) {
			for (Code c : codes) {
				if (this.matches(c))
					return true;
			}
			return false;
		}

		/**
		 * Will return true if both codes are identical by their code Levels or
		 * if the codes match up to a * of this
		 * 
		 * <pre>
		 * hello.world matches hello.world
		 * '*' matches everything
		 * hello.* matches hello.world and hello
		 * </pre>
		 * 
		 * @param otherCode
		 * @return
		 */
		public boolean matches(Code otherCode) {

			if (codeLevels.size() == 0)
				return codeLevels.size() == 0;

			Iterator<String> thisIterator = codeLevels.iterator();
			Iterator<String> otherIterator = otherCode.codeLevels.iterator();

			do {
				String mine = thisIterator.next();
				if (mine.trim().equals("*"))
					return true;

				if (!otherIterator.hasNext())
					return false;

				String other = otherIterator.next();

				if (!mine.trim().equals(other.trim()))
					return false;
			} while (thisIterator.hasNext());

			return true;
		}

		public String format() {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = 0;

			StringTokenizer st = new StringTokenizer(wholeCodeUntrimmed, " \t\n\r\f", true);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				if (!" \t\n\r\f".contains(token)) {
					break;
				}

				if ("\r\n\f".contains(token)) {
					hasNewLinesAtBeginning++;
				}
			}

			String oldValue = toString(true, false);

			StringBuilder sb = new StringBuilder();

			sb.append(StringUtils.join(codeLevels, "."));

			if (value != null && value.trim().length() > 0) {
				sb.append('=');

				String value = this.value.trim().replaceAll("\\n *", "\n");

				List<String> lines = new LinkedList<String>();
				for (String s : value.split("\\n\\n")) {
					lines.add(s.replaceAll("\\s+", " "));
				}
				value = StringUtils.join(lines, "\n");

				if (value.length() > 80 - sb.length() || value.contains("\n")) {

					sb.append("\n");

					lines.clear();

					for (String s : value.split("\n")) {

						StringBuffer total = new StringBuffer();
						StringBuffer sb2 = new StringBuffer();
						sb2.append(" ");
						for (String s2 : s.split("\\s+")) {
							if (sb2.length() + s2.length() < 80)
								sb2.append(" ").append(s2);
							else {
								total.append(sb2.toString()).append('\n');
								sb2 = new StringBuffer("  ");
								sb2.append(s2);
							}
						}
						total.append(sb2);
						lines.add(total.toString());
					}

					sb.append(StringUtils.join(lines, "\n\n"));

				} else {
					sb.append(value);
				}
			}

			if (oldValue != null)
				assert sb.toString().replaceAll("\\s", "").equals(oldValue.replaceAll("\\s", "")) : sb
					.toString()
					+ " \n\n" + oldValue;

			if (hasNewLinesAtBeginning >= 2) {
				sb.insert(0, '\n');
			}

			return sb.toString();
		}
	}

	List<List<Code>> codes = new LinkedList<List<Code>>();

	/**
	 * Starting from position pos in text will capture all chars until c is
	 * found (including), returning the resulting string.
	 * 
	 * @param c
	 * @param text
	 * @param pos
	 * @return
	 */
	String skipAhead(char c, char[] text, int pos) {

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

	List<String> split(String s, char separator, char escapeChar) {
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

	public CodedString(String stringOfcodes) {

		for (String segments : split(stringOfcodes, ';', '\"')) {
			List<Code> segmentList = new LinkedList<Code>();

			for (String code : split(segments, ',', '\"')) {
				if (code.trim().length() > 0)
					segmentList.add(new Code(code));
			}
			if (segmentList.size() > 0)
				codes.add(segmentList);
		}
	}

	/**
	 * Returns a list of all plain codes (without values behind the =)
	 * 
	 * @return
	 */
	public Collection<String> getAll() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodes()) {
			variations.add(c.wholeCodeUntrimmed.trim());
		}
		return variations;
	}

	public Collection<String> getSegments() {

		Collection<String> result = new LinkedList<String>();

		for (List<Code> c : codes) {
			result.add(StringUtils.join(c, ", ", new PlainConverter<Code>()));
		}
		return result;
	}

	public Collection<String> getAllValues(String code) {
		code = code.trim();
		Collection<String> values = new LinkedList<String>();
		for (Code c : getAllCodes()) {
			if (c.wholeCodeUntrimmed.trim().equals(code) && c.value != null)
				values.add(c.value);
		}
		return values;
	}

	public Collection<String> getAllVariations() {
		Collection<String> variations = new LinkedList<String>();

		for (Code c : getAllCodes()) {
			variations.addAll(c.getVariations());
		}
		return variations;
	}

	public Iterable<Code> getAllCodes() {
		return new IterableUnroller<Code>(codes);
	}

	public String toString() {
		return StringUtils.join(codes, ";", new JoinConverter<Code>(",",
			new StringConverter<Code>() {
				public String toString(Code t) {
					return t.toString(true, true);
				}
			}));
	}

	/**
	 * Add the given code to the last segment of this coded string
	 * 
	 * @param s
	 */
	public void add(String s) {
		s = s.trim();
		if (s.length() == 0)
			return;

		if (codes.size() == 0) {
			codes.add(new LinkedList<Code>());
		}
		codes.get(codes.size() - 1).add(new Code(s));
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
	 * Check all segments for the given code, but checking only the code (before
	 * a possible equal sign)
	 * 
	 * @param s
	 * @return
	 */
	public boolean contains(String s) {
		for (List<Code> list : codes) {
			for (Code c : list) {
				if (c.wholeCodeUntrimmed.trim().equals(s))
					return true;
			}
		}
		return false;
	}

	public void remove(String s) {
		Iterator<List<Code>> lit = codes.iterator();

		while (lit.hasNext()) {
			List<Code> list = lit.next();

			Iterator<Code> it = list.iterator();

			while (it.hasNext()) {
				Code code = it.next();
				if (code.wholeCodeUntrimmed.trim().equals(s))
					it.remove();
			}
			if (list.size() == 0)
				lit.remove();
		}
	}

	public boolean rename(String fromRename, String toRename) {

		boolean result = false;

		for (List<Code> segment : codes) {
			for (Code c : segment) {
				result |= c.rename(fromRename, toRename);
			}
		}
		return result;
	}

	public boolean containsAll(Iterable<String> codes) {
		for (String s : codes) {
			if (!contains(s))
				return false;
		}
		return true;
	}

	public boolean containsAny(String code) {

		Code search = new Code(code);

		for (Code c : getAllCodes()) {

			if (search.matches(c))
				return true;
		}
		return false;

	}

	public boolean containsAny(Iterable<Code> codes) {

		for (Code c : getAllCodes()) {

			for (Code search : codes) {

				if (search.matches(c))
					return true;
			}
		}
		return false;
	}

	public boolean containsInAllSegments(String code) {

		Code search = new Code(code);

		nextSegment: for (List<Code> segment : codes) {

			for (Code c : segment) {
				if (c.matches(search))
					continue nextSegment;
			}

			// We did not find the code that we search for
			return false;
		}
		return true;
	}

	public String format() {

		return StringUtils.join(codes, ";\n\n", new JoinConverter<Code>(",\n",
			new StringConverter<Code>() {
				public String toString(Code t) {
					return t.format();
				}
			}));
	}

}
