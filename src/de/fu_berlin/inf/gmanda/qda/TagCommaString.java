package de.fu_berlin.inf.gmanda.qda;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fu_berlin.inf.gmanda.util.IterableUnroller;
import de.fu_berlin.inf.gmanda.util.StringUtils;
import de.fu_berlin.inf.gmanda.util.StringUtils.JoinConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

public class TagCommaString extends AbstractCodedString {

	public class TagCodedCode extends AbstractCode { 
		
		public TagCodedCode(String code) {

			String tag;
			
			int index = code.indexOf('=');
			
			if (index != -1 && index + 1 < code.length()) {
				value = code.substring(index + 1);
				tag = code.substring(0, index);
			} else {
				tag = code;
			}

			setTag(tag);
		}

		String value;

		public String getValue() {
			return value;
		}

		public String toString(boolean withValue, boolean whiteSpace) {
			StringBuilder sb = new StringBuilder();

			if (whiteSpace) {
				sb.append(tag);
			} else {
				sb.append(tag.trim());
			}

			if (withValue && hasValue()) {
				sb.append('=');
				if (whiteSpace)
					sb.append(value);
				else
					sb.append(value.trim());
			}
			return sb.toString();
		}

		public String format(int indent, int width) {

			// If there is more than one newline at the beginning, we want to
			// keep one of it.
			int hasNewLinesAtBeginning = 0;

			StringTokenizer st = new StringTokenizer(tag, " \t\n\r\f", true);
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

			sb.append(StringUtils.join(tagLevels, "."));

			if (hasValue()) {
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
						sb2.append(StringUtils.spaces(indent));
						for (String s2 : s.split("\\s+")) {
							if (sb2.length() + s2.length() < 80)
								sb2.append(" ").append(s2);
							else {
								total.append(sb2.toString()).append('\n');
								sb2 = new StringBuffer(StringUtils.spaces(indent));
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
		
		public List<Code> getProperties() {
			
			if (value == null){
				return Collections.emptyList();
			}
			
			String value = org.apache.commons.lang.StringUtils.strip(this.value, "\" \n\r\f\t");
			
			Pattern p = Pattern.compile("(memo|date|desc|summary|milestone|def|value|vdef|quote|ref|title|cause)\\s*:\\s*");
			Matcher m = p.matcher(value);
			
			List<Code> result = new LinkedList<Code>();
			
			String lastKey = "desc";
			StringBuilder currentValue = new StringBuilder();
			
			int pos = 0;
			
			while (m.find()){
			
				int start = m.start();
				
				String key = m.group(1);
				
				if (pos < start){
					currentValue.append(value.substring(pos, start));
				}
				
				pos = m.end();
				
				if (currentValue.length() > 0){
					result.add(new TagCodedCode(lastKey + "=\"" + org.apache.commons.lang.StringUtils.strip(currentValue.toString(), ", \n\r\f\t") + "\""));
					currentValue = new StringBuilder();
				}
				lastKey = key;
			}
			
			if (pos < value.length()){
				currentValue.append(value.substring(pos));
			}
			
			if (currentValue.length() > 0){
				result.add(new TagCodedCode(lastKey + "=\"" + org.apache.commons.lang.StringUtils.strip(currentValue.toString(), ", \n\r\f\t") + "\""));
			}
			
			return result; 
		}
	}

	List<List<Code>> codes = new LinkedList<List<Code>>();

	public TagCommaString(String stringOfcodes) {

		for (String segments : StringUtils.split(stringOfcodes, ';', '\"')) {
			List<Code> segmentList = new LinkedList<Code>();

			for (String code : StringUtils.split(segments, ',', '\"')) {
				if (code.trim().length() > 0)
					segmentList.add(new TagCodedCode(code));
			}
			if (segmentList.size() > 0)
				codes.add(segmentList);
		}
	}

	public String format() {

		return StringUtils.join(codes, ";\n\n", new JoinConverter<Code>(",\n",
			new StringConverter<Code>() {
				public String toString(Code t) {
					return t.format(2, 80);
				}
			}));
	}

	public Code parse(String s) {
		return new TagCodedCode(s);
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
		codes.get(codes.size() - 1).add(new TagCodedCode(s));
	}

	public void remove(String s) {
		Iterator<List<Code>> lit = codes.iterator();

		while (lit.hasNext()) {
			List<Code> list = lit.next();

			Iterator<Code> it = list.iterator();

			while (it.hasNext()) {
				Code code = it.next();
				if (code.getTag().equals(s))
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
				result |= c.renameTag(fromRename, toRename);
			}
		}
		return result;
	}

}
