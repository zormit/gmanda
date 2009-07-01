package de.fu_berlin.inf.gmanda.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Class which can be used to generate a temporary velocity template which has
 * whitespace corrected for better output.
 */
public class VelocityWhitespaceRepair {

	class Lines {

		int indent;

		List<String> lines = new ArrayList<String>();

		public String toString() {
			StringBuilder sb = new StringBuilder();

			String indentSpaces = CStringUtils.spaces(indent);

			for (String line : lines) {
				sb.append(indentSpaces).append(line).append("\n");
			}
			return sb.toString();
		}

	}

	String fixWhitespaceInternal(StringTokenizer st, int depth) {

		List<String> indentedLines = new ArrayList<String>();

		int minIndent = Integer.MAX_VALUE;

		String lastLine = null;

		while (st.hasMoreTokens()) {

			String line = st.nextToken();

			int spaces = CStringUtils.getLeadingWhiteSpaceWidth(line);

			String startOfLine = line.substring(spaces);

			if (startOfLine.startsWith("#end")) {
				lastLine = startOfLine;
				break;
			}

			// Find smallest indent that is not an empty line
			if (line.trim().length() != 0 && spaces < minIndent) {
				minIndent = spaces;
			}

			indentedLines.add(line);

			if (startOfLine.startsWith("#if")
					|| startOfLine.startsWith("#macro")
					|| startOfLine.startsWith("#foreach")) {
				indentedLines.add(fixWhitespaceInternal(st, spaces));
			}
		}

		StringJoiner joiner = new StringJoiner("\n");

		for (String eachLine : indentedLines) {

			for (String split : eachLine.split("\\n")) {
				if (StringUtils.stripStart(split, " ").startsWith("#")) {
					joiner.append(StringUtils.stripStart(split, " "));
				} else if (split.trim().length() == 0) {
					joiner.append("");
				} else {
					joiner.append(split.substring(minIndent - depth));
				}
			}
		}

		if (lastLine != null)
			joiner.append(lastLine);

		return joiner.toString();
	}

	public String fixWhitespace(String input) {

		return fixWhitespaceInternal(new StringTokenizer(input, "\n\r\f"), 0);
	}

	public static void main(String[] args) throws IOException {

		FileUtils.writeStringToFile(new File(
				"resources/templates/glossaryWhitespace.vm"),
				new VelocityWhitespaceRepair().fixWhitespace(FileUtils
						.readFileToString(new File(
								"resources/templates/glossary.vm"))));

	}

}
