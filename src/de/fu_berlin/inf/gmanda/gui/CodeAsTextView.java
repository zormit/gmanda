package de.fu_berlin.inf.gmanda.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.misc.GmandaHyperlinkListener;
import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.Slice;
import de.fu_berlin.inf.gmanda.util.CStringUtils;
import de.fu_berlin.inf.gmanda.util.DeferredVariableProxyListener;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.StringJoiner;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.CStringUtils.StringConverter;

public class CodeAsTextView extends JScrollPane {

	JTextPane pane = new JTextPane();

	ProjectProxy project;

	CodeDetailProxy codeDetailProxy;

	SelectionProxy selection;

	public CodeAsTextView(ProjectProxy projectProxy, SelectionProxy selection,
		CodeDetailProxy filterTextProxy, GmandaHyperlinkListener linkListener) {
		super();

		this.selection = selection;
		this.project = projectProxy;
		this.codeDetailProxy = filterTextProxy;

		/**
		 * Make links clickable
		 */
		pane.addHyperlinkListener(linkListener);

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				update();
			}
		});

		filterTextProxy.add(new DeferredVariableProxyListener<String>(
			new VariableProxyListener<String>() {
				public void setVariable(String newValue) {
					update();
				}
			}, 400, TimeUnit.MILLISECONDS));

		setBorder(BorderFactory.createEmptyBorder());

		pane.setEditable(false);
		pane.setPreferredSize(new Dimension(400, 400));
		pane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		pane.setFont(new Font("Courier New", 0, 12));

		setViewportView(pane);

		pane.setContentType("text/html");
		pane.setFont(new Font("Courier", 0, 10));

		invalidate();
	}

	public void update() {

		if (!isVisible())
			return;

		Project p = project.getVariable();
		String f = codeDetailProxy.getVariable();

		if (p == null || f == null || f.trim().length() == 0) {
			pane.setText("");
			return;
		}

		CodedString c = CodedStringFactory.parse(f);

		Iterator<? extends Code> codes = c.getAllCodes().iterator();
		if (!codes.hasNext()) {
			return;
		}

		Code codeToShow = codes.next();

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb
			.append("<body style=\"font-family: monospace; font-size: 12pt; padding: 2px; margin: 2px;\">");

		List<String> variations = new LinkedList<String>(codeToShow.getTagVariations());
		Collections.reverse(variations);
		for (String s : variations) {
			sb.append(codeToHTML(p, s));
		}

		sb.append("</body></html>");

		pane.setText(sb.toString());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				invalidate();
				getVerticalScrollBar().setValue(0);
			}
		});
	}

	public static String toShortId(String s) {
		return s.replaceAll("(gmane(://|\\.)|devel\\.|comp\\.)", "");
	}

	public static String toA(PrimaryDocument pd) {
		String s = pd.getFilename();

		if (s == null)
			return pd.getName();

		return String.format("<a href='%s'>%s</a>", s, toShortId(s));
	}

	public static String toFilterA(Code c) {

		StringJoiner result = new StringJoiner(".");
		StringJoiner sb = new StringJoiner(".");
		for (String s : c.getTagLevels()) {
			result.append(toFilterA(sb.toString(), s));
		}
		return result.toString();
	}

	public static String toFilterA(String tag, String toDisplay) {
		try {
			return String.format("<a href='gmaneFilter://%s'>%s</a>", URLEncoder.encode(tag,
				"UTF-8"), StringEscapeUtils.escapeHtml(toDisplay));
		} catch (IOException e) {
			return tag;
		}
	}

	public static String toFilterA(String s) {
		return toFilterA(s, s);
	}

	public String codeToHTML(Project p, String code) {

		List<PrimaryDocument> newFilterList = new ArrayList<PrimaryDocument>(p.getCodeModel()
			.getPrimaryDocuments(code));

		Collections.sort(newFilterList);

		if (newFilterList == null)
			return "";

		StringBuilder sb = new StringBuilder();

		sb.append("<h3>").append(toFilterA(code)).append("</h3>");

		List<PrimaryDocument> noValueList = new LinkedList<PrimaryDocument>();

		List<Pair<DateTime, Pair<Code, PrimaryDocument>>> dateBased = new LinkedList<Pair<DateTime, Pair<Code, PrimaryDocument>>>();

		List<Pair<Code, PrimaryDocument>> noDate = new LinkedList<Pair<Code, PrimaryDocument>>();

		// First definition
		sb.append("<h4>Definition</h4>");

		for (PrimaryDocument pd : newFilterList) {

			for (Code c : CodedStringFactory.parse(pd.getCode()).getAll(code)) {

				List<? extends Code> subs = c.getProperties();

				for (Code sub : subs) {
					if (sub.getTag().equals("def")) {
						sb.append("<p>");
						sb.append(code2html(sub, pd, true));
						sb.append("</p>");
					}
					if (sub.getTag().equals("date")) {
						String date = StringUtils.strip(sub.getValue(), " \"");
						DateTime d;
						try {
							d = new DateTime(date);
							dateBased.add(new Pair<DateTime, Pair<Code, PrimaryDocument>>(d,
								new Pair<Code, PrimaryDocument>(c, pd)));
						} catch (IllegalArgumentException e) {
							noDate.add(new Pair<Code, PrimaryDocument>(c, pd));
						}
					}
				}
			}
		}

		// Then properties
		properties2html(p, code, sb);

		// Then sorted by date
		if (dateBased.size() > 0) {

			{ // First the date based codes we found
				sb.append("<h4>Sorted by date</h4>");

				@SuppressWarnings("unchecked")
				Comparator<Pair<DateTime, Pair<Code, PrimaryDocument>>> co = Pair.pCompare();
				Collections.sort(dateBased, co);

				sb.append("<ul>");
				for (Pair<DateTime, Pair<Code, PrimaryDocument>> next : dateBased) {
					code2html(sb, next.v.p, next.v.v);
				}
				sb.append("</ul>");
			}

			if (noDate.size() > 0) {
				sb.append("<h4>Codes missing a date</h4>");
				Comparator<Pair<Code, PrimaryDocument>> co = Pair.vCompare();
				Collections.sort(noDate, co);

				sb.append("<ul>");
				for (Pair<Code, PrimaryDocument> next : noDate) {
					code2html(sb, next.p, next.v);
				}
				sb.append("</ul>");
			}
		}

		sb.append("<h4>All</h4>");

		sb.append("<ul>");

		for (PrimaryDocument pd : newFilterList) {

			CodedString coded = CodedStringFactory.parse(pd.getCode());

			Collection<? extends Code> allCodes = coded.getAll(code);

			if (allCodes == null || allCodes.size() == 0) {
				noValueList.add(pd);
				continue;
			} else {
				boolean found = false;

				for (Code c : allCodes) {
					String value = c.getValue();
					if (value != null && value.trim().length() > 0) {
						found = true;
						break;
					}
				}
				if (!found) {
					noValueList.add(pd);
					continue;
				}
			}

			sb.append("<li>");
			if (pd.getFilename() != null) {
				sb.append(toA(pd));
			} else {
				sb.append("Document with no file");
			}
			sb.append("<br><ul>");

			for (Code c : allCodes) {
				sb.append("<li>");
				sb.append(code2html(c, true));
				sb.append("</li>");
			}
			sb.append("</ul>");
			sb.append("</li>");
		}

		if (noValueList.size() > 0) {
			sb.append("<li> Occurances with no values:<br>");
			for (PrimaryDocument pd : noValueList) {
				if (pd.getFilename() != null) {
					sb.append(toA(pd)).append("<br>");
				}
			}
			sb.append("</li>");
		}
		sb.append("</ul>");

		return sb.toString();
	}

	public static String pd2a(PrimaryDocument pd) {
		if (pd.getFilename() != null) {
			return toA(pd);
		} else {
			return "Document with no file";
		}
	}

	public static String code2html(Code sub, PrimaryDocument pd, boolean expandSubCodes) {

		StringBuilder sb = new StringBuilder();

		sb.append(code2html(sub, expandSubCodes));
		sb.append(" (<i>Definition from ").append(pd2a(pd)).append("</i>)");

		return sb.toString();

	}

	private void properties2html(Project p, String code, StringBuilder sb) {

		Slice parentSlice = p.getCodeModel().getInitialFilterSlice(code).select(
			CodedStringFactory.parseOne(code));

		for (Entry<String, Slice> slice : parentSlice.slice().entrySet()) {

			String property = slice.getKey().trim();
			Slice childSlice = slice.getValue();

			if (property.startsWith("def") || property.startsWith("desc")
				|| property.trim().startsWith("quote"))
				continue;

			if (property.startsWith("#"))
				sb.append(surround("<li>" + toFilterA(property) + ":<ul>", property2html(property,
					childSlice), "</ul></li>"));
			else
				sb.append(surround("<li>" + toFilterA(property) + ":<ul>", slice2html(property,
					childSlice), "</ul></li>"));
		}
	}

	public static String surround(String start, String middle, String end) {
		if (middle.trim().length() > 0) {
			return start + middle + end;
		} else {
			return "";
		}
	}

	public static String slice2html(String code, Slice s) {

		StringBuilder sb = new StringBuilder();

		sb.append(slice2html(s, "def"));

		String noValues = CStringUtils.join(s.getDocuments().entrySet(), "<br>",
			new StringConverter<Entry<PrimaryDocument, Collection<Code>>>() {
				public String toString(Entry<PrimaryDocument, Collection<Code>> docs) {
					return pd2html(docs.getKey(), docs.getValue());
				}
			});

		sb.append(noValues);

		return surround("<ul>", sb.toString(), "</ul>");
	}

	public static String property2html(String code, Slice s) {

		StringBuilder sb = new StringBuilder();

		sb.append(slice2html(s, "def"));

		for (Entry<String, Slice> entry2 : s.slice().entrySet()) {

			String aProp2 = entry2.getKey();
			Slice sub2 = entry2.getValue();

			if (aProp2.equals("def"))
				continue;

			if (aProp2.equals("desc")) {
				sb.append(CStringUtils.join(sub2.getDocuments().entrySet(), "<br>",
					new StringConverter<Entry<PrimaryDocument, Collection<Code>>>() {
						public String toString(Entry<PrimaryDocument, Collection<Code>> docs) {
							return pd2html(docs.getKey(), docs.getValue());
						}
					}));
			} else {
				sb.append(surround("<li>" + toFilterA(aProp2) + ":", slice2html(aProp2, sub2), "</li>"));
			}
		}

		String noValues = CStringUtils.join(s.getDocuments().entrySet(), "<br>",
			new StringConverter<Entry<PrimaryDocument, Collection<Code>>>() {
				public String toString(Entry<PrimaryDocument, Collection<Code>> docs) {
					if (docs.getValue().size() == 0)
						return pd2html(docs.getKey(), docs.getValue());
					else
						return null;
				}
			});

		sb.append(noValues);

		return surround("<ul>", sb.toString(), "</ul>");
	}

	public static String pd2html(PrimaryDocument pd, Collection<Code> codes) {

		StringBuilder sb = new StringBuilder();

		if (pd.getFilename() != null) {
			sb.append(toA(pd));
		}
		if (codes.size() > 0) {
			sb.append("<ul>");
			for (Code c : codes) {
				sb.append("<li>").append(code2html(c, true)).append("</li>");
			}
			sb.append("</ul>");
		}

		return sb.toString();
	}

	private static String slice2html(Slice sub, String string) {

		StringBuilder sb = new StringBuilder();

		for (Entry<PrimaryDocument, Collection<Code>> entry : sub.select(
			CodedStringFactory.parseOne(string)).getDocuments().entrySet()) {

			String result = codes2html(entry.getValue(), entry.getKey());
			if (result.trim().length() > 0) {
				sb.append(result);
			}
		}

		return sb.toString();
	}

	private static String codes2html(Collection<Code> codes, PrimaryDocument pd) {

		StringBuilder sb = new StringBuilder();
		for (Code c : codes) {
			code2html(sb, c, pd);
		}
		return sb.toString();
	}

	private static void code2html(StringBuilder sb, Code c, PrimaryDocument pd) {

		sb.append("<li>");
		sb.append(code2html(c, pd, true));
		sb.append("</li>");
	}

	public static String code2html(Code c, boolean expandSubCodes) {

		StringBuilder sb = new StringBuilder();

		List<? extends Code> values = c.getProperties();

		if ((values.size() == 1 && c.getTag().equals("desc")) || !expandSubCodes) {
			sb.append(surround("<p>", c.getValue().replaceAll("\n[ \t]*\n", "</p><p>"), "</p>"));
			return sb.toString();
		}

		sb.append(toFilterA(c.getTag())).append(": ");

		if (values.size() == 1 && values.get(0).getTag().equals("desc")) {
			sb.append(surround("<p>", values.get(0).getValue().replaceAll("\n[ \t]*\n", "</p><p>"), "</p>"));
		} else {
			sb.append("<ul>");
			for (Code sub : c.getProperties()) {
				sb.append("<li>");
				sb.append(code2html(sub, true));
				sb.append("</li>");
			}
			sb.append("</ul>");
		}

		return sb.toString();
	}

	public String getTitle() {
		return "Code Detail View";
	}

	public String getTooltip() {
		return "Shows details for the Code currently entered in the Code View";
	}

	public Component getView() {
		return this;
	}

}
