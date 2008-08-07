package de.fu_berlin.inf.gmanda.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
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

		Iterator<Code> codes = c.getAllCodes().iterator();
		if (!codes.hasNext()) {
			return;
		}

		Code codeToShow = codes.next();

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body style=\"font-family: monospace; font-size: 12pt;\">");

		List<String> variations = new LinkedList<String>(codeToShow.getVariations());
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
	
	public String toShortId(String s){
		return s.replaceAll("(gmane(://|\\.)|devel\\.|comp\\.)", "");
	}
	
	public String toA(PrimaryDocument pd){
		String s = pd.getFilename();
		
		return String.format("<a href='%s'>%s</a><br>", s, toShortId(s));
	}

	public String codeToHTML(Project p, String code) {

		List<PrimaryDocument> newFilterList = new ArrayList<PrimaryDocument>(p.getCodeModel()
			.getPrimaryDocuments(code));

		Collections.sort(newFilterList);

		if (newFilterList == null)
			return "";

		StringBuilder sb = new StringBuilder();

		sb.append("<h3>").append(code).append("</h3>");
		sb.append("<ul>");

		List<PrimaryDocument> noValueList = new LinkedList<PrimaryDocument>();

		for (PrimaryDocument pd : newFilterList) {

			if (pd.getFilename() != null){
				docs.put(pd.getFilename(), pd);
			}
			
			CodedString coded = CodedStringFactory.parse(pd.getCode());
			
			Collection<String> allValues = coded.getAllValues(code);

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
			sb.append("<ul>");
			for (String s : allValues) {
				sb.append("<li>").append(s.replaceAll("\n[ \t]*\n", "<p><p>")).append("</li>");
			}
			sb.append("</ul>");
			sb.append("</li>");
		}

		if (noValueList.size() > 0) {
			sb.append("<li> Occurances with no values:<br>");
			for (PrimaryDocument pd : noValueList) {
				if (pd.getFilename() != null) {
					sb.append(toA(pd));
				}
			}
			sb.append("</li>");
		}


		//		sb.append("<li> All occurances:<br>");
		//		for (PrimaryDocument pd : newFilterList) {
		//			if (pd.getFilename() != null) {
		//				sb.append(toA(pd));
		//				docs.put(pd.getFilename(), pd);
		//			}
		//		}
		//		sb.append("</li>");

		sb.append("</ul>");
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
