package de.fu_berlin.inf.gmanda.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import de.fu_berlin.inf.gmanda.qda.CodedString;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.CodedString.Code;
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

		CodedString c = new CodedString(f);

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

	public String codeToHTML(Project p, String code){

		List<PrimaryDocument> newFilterList = p.getCodeModel().getPrimaryDocuments(code);

		if (newFilterList == null)
			return "";

		StringBuilder sb = new StringBuilder();
		
		sb.append("<h3>").append(code).append("</h3>");
		sb.append("<ul>");
		for (PrimaryDocument pd : newFilterList) {

			Collection<String> allValues = new CodedString(pd.getCode()).getAllValues(code);
			
			if (allValues == null || allValues.size() == 0)
				continue;
			
			sb.append("<li>");
			if (pd.getFilename() != null) {
				sb.append(String.format("<a href='%s'>%s</a>", pd.getFilename(), pd.getFilename()));
				docs.put(pd.getFilename(), pd);
			} else {
				sb.append("Document with no file");
			}

			if (allValues != null) {
				sb.append("<ul>");
				for (String s : allValues) {
					sb.append("<li>").append(s).append("</li>");
				}
				sb.append("</ul>");
			}
			sb.append("</li>");
		}
		sb.append("<li> All occurances: ");
		for (PrimaryDocument pd : newFilterList) {
			if (pd.getFilename() != null) {
				sb.append(String.format("<a href='%s'>%s</a>", pd.getFilename(), pd.getFilename()));
				docs.put(pd.getFilename(), pd);
			}
		}
		sb.append("</li>");
		
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
