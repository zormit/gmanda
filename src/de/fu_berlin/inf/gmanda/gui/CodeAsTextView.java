package de.fu_berlin.inf.gmanda.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class CodeAsTextView extends JScrollPane {

	JTextPane pane = new JTextPane();

	ProjectProxy project;

	CodeDetailProxy codeDetailProxy;

	SelectionProxy selection;

	public CodeAsTextView(ProjectProxy projectProxy, SelectionProxy selection,
		CodeDetailProxy filterTextProxy) {
		super();

		this.selection = selection;
		this.project = projectProxy;
		this.codeDetailProxy = filterTextProxy;

		/**
		 * Make links clickable
		 */
		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					String filename = arg0.getDescription();
					if (docs.containsKey(filename))
						CodeAsTextView.this.selection.setVariable(docs.get(filename));
				}
			}
		});

		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				update();
			}
		});

		filterTextProxy.add(new VariableProxyListener<String>() {
			public void setVariable(String newValue) {
				update();
			}
		});

		setBorder(BorderFactory.createEmptyBorder());

		pane.setEditable(false);
		// pane.setFocusable(false);
		pane.setPreferredSize(new Dimension(400, 400));
		pane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		pane.setFont(new Font("Courier New", 0, 12));

		setViewportView(pane);

		pane.setContentType("text/html");
		pane.setFont(new Font("Courier", 0, 10));

		invalidate();
	}

	public HashMap<String, PrimaryDocument> docs = new HashMap<String, PrimaryDocument>();

	public void update() {

		if (!isVisible())
			return;

		docs.clear();

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
		sb.append("<body style=\"font-family: monospace; font-size: 12pt;\">");

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

	public static String toFilterA(String s) {
		try {
			return String.format("<a href='gmaneFilter://%s'>%s</a>", URLEncoder.encode(s, "UTF-8"), StringEscapeUtils.escapeHtml(s));
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public String codeToHTML(Project p, String code) {

		List<PrimaryDocument> newFilterList = new ArrayList<PrimaryDocument>(p.getCodeModel()
			.getPrimaryDocuments(code));
		
		Collections.sort(newFilterList);

		if (newFilterList == null)
			return "";

		StringBuilder sb = new StringBuilder();

		sb.append("<h3>").append(code).append("</h3>");

		List<PrimaryDocument> noValueList = new LinkedList<PrimaryDocument>();

		List<Pair<DateTime, Pair<Code, PrimaryDocument>>> dateBased = new LinkedList<Pair<DateTime, Pair<Code, PrimaryDocument>>>();
		List<Pair<Code, PrimaryDocument>> noDate = new LinkedList<Pair<Code, PrimaryDocument>>();

		// First definition
		sb.append("<h5>Definition</h5>");

		for (PrimaryDocument pd : newFilterList) {

			for (Code c : CodedStringFactory.parse(pd.getCode()).getAll(code)) {

				List<? extends Code> subs = c.getProperties();

				for (Code sub : subs) {
					if (sub.getTag().equals("def")) {

						sb.append("<p>");
						sb.append(sub.getValue());
						sb.append(" (<i>Definition from ");
						if (pd.getFilename() != null) {
							sb.append(toA(pd));
						} else {
							sb.append("Document with no file");
						}
						sb.append("</i>)");
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

		// First sorted by date
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

		sb.append("<h5>All</h5>");

		sb.append("<ul>");
		
		for (PrimaryDocument pd : newFilterList) {

			if (pd.getFilename() != null) {
				docs.put(pd.getFilename(), pd);
			}

			CodedString coded = CodedStringFactory.parse(pd.getCode());

			Collection<? extends Code> allValues = coded.getAll(code);

			if (allValues == null || allValues.size() == 0) {
				noValueList.add(pd);
				continue;
			}

			
			sb.append("<li>");
			if (pd.getFilename() != null) {
				sb.append(toA(pd));
			} else {
				sb.append("Document with no file");
			}
			sb.append("<br><ul>");
			
			for (Code c : allValues){
				sb.append("<li>");
				sb.append(code2html(c));
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

	private void code2html(StringBuilder sb, Code c, PrimaryDocument pd) {

		sb.append("<li>");
		sb.append(code2html(c));
		sb.append(" (<i>from ");
		if (pd.getFilename() != null) {
			sb.append(toA(pd));
		} else {
			sb.append("Document with no file");
		}
		sb.append("</i>)");
		sb.append("</li>");
	}

	public static String code2html(Code c) {

		StringBuilder sb = new StringBuilder();

		List<? extends Code> values = c.getProperties();

		sb.append(c.getTag()).append(": ");

		if (values.size() == 1 && values.get(0).getTag().equals("desc")) {
			sb.append(values.get(0).getValue().replaceAll("\n[ \t]*\n", "<p><p>"));
		} else {
			sb.append("<ul>");
			for (Code sub : c.getProperties()) {
				sb.append("<li>");
				sb.append(code2html(sub));
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
