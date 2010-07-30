package de.fu_berlin.inf.gmanda.qda;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.common.collect.Multimap;

import de.fu_berlin.inf.gmanda.util.StringJoiner;

public class ToHtmlHelper {
	
	public static String definition(CodeModel cm, String code){
		
		Multimap<PrimaryDocument, Code> defs = cm.getValues(code, "def");
		
		if (defs.size() == 0){
			return "<b>No Definition found</b>";
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append("<b>Definition</b></p>");

			for (Map.Entry<PrimaryDocument, Code> def : defs.entries()){
				sb.append(code2html(def.getValue(), true, false));
				sb.append(" (<i>Definition from ").append(pd2a(def.getKey(), code)).append("</i>)");
				sb.append("</p>");
			}
			return sb.toString();
		}
	}
	
	public static String code2html(Code sub, PrimaryDocument pd, boolean expandSubCodes) {

		StringBuilder sb = new StringBuilder();

		sb.append(code2html(sub, expandSubCodes, false));
		sb.append(" (<i>Definition from ").append(pd2a(pd, null)).append("</i>)");

		return sb.toString();

	}

	
	public static void code2html(StringBuilder sb, Code c, PrimaryDocument pd) {

		sb.append("<li>");
		sb.append(code2html(c, pd, true));
		sb.append("</li>");
	}

	public static String code2html(Code c, boolean expandSubCodes, boolean tagName) {

		StringBuilder sb = new StringBuilder();

		List<? extends Code> values = c.getProperties();

		if ((values.size() == 1 && c.getTag().equals("desc")) || !expandSubCodes) {
			sb.append(surround("<p>", c.getValue().replaceAll("\n[ \t]*\n", "</p><p>"), "</p>"));
			return sb.toString();
		}

		if (tagName)
			sb.append(toFilterA(c)).append(": ");

		if (values.size() == 1 && values.get(0).getTag().equals("desc")) {
			sb.append(surround("<p>", values.get(0).getValue().replaceAll("\n[ \t]*\n",
				"</p><p>"), "</p>"));
		} else {
			sb.append("<ul>");
			for (Code sub : c.getProperties()) {
				sb.append("<li>");
				sb.append(code2html(sub, true, true));
				sb.append("</li>");
			}
			sb.append("</ul>");
		}

		return sb.toString();
	}
	

	public static String surround(String start, String middle, String end) {
		if (middle.trim().length() > 0) {
			return start + middle + end;
		} else {
			return "";
		}
	}
	
	public static String pd2a(PrimaryDocument pd, String jumpTo) {
		if (pd.getFilename() != null) {
			return toA(pd, jumpTo);
		} else {
			return "Document with no file";
		}
	}
	
	public static String toShortId(String s) {
		return s.replaceAll("(gmane(://|\\.)|devel\\.|comp\\.)", "");
	}

	public static String toA(PrimaryDocument pd, String jumpTo) {
		String s = pd.getFilename();

		if (s == null || s.trim().length() == 0)
			return pd.getName();
		
		String query = "";
		if (jumpTo != null && jumpTo.trim().length() > 0){
			try {
				query = "?jumpTo=" + URLEncoder.encode(jumpTo, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}

		return String.format("<a href='%s%s'>%s</a>", s, query, toShortId(s));
	}

	public static String toFilterA(Code c){
		return toFilterA(0, c);
	}
	
	public static String toFilterA(int skipLevels, Code c) {

		StringJoiner result = new StringJoiner(".");
		StringJoiner sb = new StringJoiner(".");
		
		for (String s : c.getTagLevels()) {
			sb.append(s);
			if (skipLevels <= 0){
				result.append(toFilterAHref(sb.toString(), s));
			} else {
				skipLevels--;
			}
		}

		if (result.length() == 0){
			result.append(".");
		}
		
		return "<nobr>" + result.toString() + "</nobr>";
	}

	public static String toFilterAHref(String tag, String toDisplay) {
		try {
			return String.format("<a href='gmaneFilter://%s'>%s</a>", URLEncoder.encode(tag,
				"UTF-8"), StringEscapeUtils.escapeHtml(toDisplay));
		} catch (IOException e) {
			return tag;
		}
	}

	public static String toFilterA(String s){
		return toFilterA(0, s);
	}
	
	public static String toFilterA(int skipLevels, String s) {
		return toFilterA(skipLevels, CodedStringFactory.parseOne(s));
	}
	
	
}
