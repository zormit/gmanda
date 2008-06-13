/*
 * Created on 04.01.2005
 *
 */
package de.fu_berlin.inf.gmanda.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.net.URL;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.preferences.ScrollOnShowProperty;
import de.fu_berlin.inf.gmanda.imports.GmaneFacade;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StateChangeListener;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class TextView extends JScrollPane {

	JTextPane pane = new JTextPane();

	protected Object toShow;
	protected String search;

	Desktop desktop;
	boolean initialized = false;

	public void browse(final URL url) {

		commonService.run(new Runnable() {
			public void run() {
				if (!initialized) {
					if (Desktop.isDesktopSupported()) {
						Desktop d = Desktop.getDesktop();

						if (d.isSupported(Desktop.Action.BROWSE)) {
							TextView.this.desktop = d;
						}
					}
					initialized = true;
				}
				if (desktop != null) {
					try {
						desktop.browse(url.toURI());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, "Error opening URL");

	}

	ScrollOnShowProperty scrollOnShow;

	CommonService commonService;

	GmaneFacade gmane;

	public TextView(ScrollOnShowProperty scrollOnShow, SelectionProxy selectionProxy,
		SearchStringProxy search, GmaneFacade gmane, CommonService service) {
		super();

		this.scrollOnShow = scrollOnShow;
		this.gmane = gmane;
		this.commonService = service;

		setBorder(BorderFactory.createEmptyBorder());

		pane.setEditable(false);
		// pane.setFocusable(false);
		pane.setPreferredSize(new Dimension(400, 200));
		pane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		pane.setFont(new Font("Courier New", 0, 12));

		/**
		 * Make links clickable
		 */
		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					browse(arg0.getURL());
				}
			}
		});

		setViewportView(pane);

		invalidate();

		final StateChangeListener<PrimaryDocument> pdListener = new StateChangeListener<PrimaryDocument>() {
			public void stateChangedNotification(PrimaryDocument t) {
				update();
			}
		};

		selectionProxy.add(new VariableProxyListener<Object>() {
			public void setVariable(Object show) {
				if (toShow != null && toShow instanceof PrimaryDocument)
					((PrimaryDocument) toShow).getTextChangeNotifier().remove(pdListener);

				toShow = show;

				if (toShow != null && toShow instanceof PrimaryDocument)
					((PrimaryDocument) toShow).getTextChangeNotifier().add(pdListener);

				update();
			}
		});
		this.toShow = selectionProxy.getVariable();

		search.addAndNotify(new VariableProxyListener<String>() {
			public void setVariable(String search) {
				TextView.this.search = search;
				update();
			}
		});
	}

	public String toHTML(String html) {
		return TextView.toHTML(html, search);
	}

	public static String toHTML(String html, String search) {

		// html = html.replace("&", "&amp;");
		html = html.replace(">", "&gt;");
		html = html.replace("<", "&lt;");
		// html = html.replaceAll("\\n\\s*(&gt;)*\\s*(--+|__+)\\s*\\n", "<hr>");

		if (search != null && search.length() > 0) {
			html = html.replaceAll("(?i)(" + Pattern.quote(search) + ")",
				"<span style=\"background-color:red; color:white;\">$1</span>");
		}

		html = toBoldAndItalic(html);

		/* comment is for easy detection of line ends later */
		html = html.replaceAll("\n", "<br>");
		html = html.replaceAll("\r", "");
		html = html.replaceAll("\f", "");

		// color the replied portions, depth up to 6
		html = html.replaceAll(
			"<br>([ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#880088;\">$1</span>");

		html = html.replaceAll(
			"<br>([ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#ff7700;\">$1</span>");

		html = html.replaceAll("<br>([ \t]*&gt;[ \t]*&gt;[ \t]*&gt;[ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#888800;\">$1</span>");

		html = html.replaceAll("<br>([ \t]*&gt;[ \t]*&gt;[ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#00bb00;\">$1</span>");

		html = html.replaceAll("<br>([ \t]*&gt;[ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#000090;\">$1</span>");

		html = html.replaceAll("<br>([ \t]*&gt;.*?)(?=<br>)",
			"<br><span style=\"color:#ee0000;\">$1</span>");

		// html and body
		html = "<html><body>" + html + "</body></html>";

		html = toHyperLink(html);

		// www.xxxxx
		html = html.replaceAll(
			"([^a-zA-Z0-9._+-/?=~!@#$%&:])(www[.][a-zA-Z0-9._+-/?=~!@#$%&:]+[^]. >\t])([] ,<\t])",
			"$1<a href=\"http://$2\">$2</a>$3");

		// wrap emails
		html = html.replaceAll(
			"([ :,&lt;>\t])([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,4})([] ,&gt;<\t])",
			"$1<a href=\"mailto:$2\">$2</a>$3");

		// remove double empty lines
		html = html.replaceAll("<br>\\s*<br>(\\s*<br>)+", "<br><br>");

		// this replaces all double spaces with space symbol. All long
		// spaces
		// are converted appropriately,
		// except single space which is single space in html anyway
		html = html.replaceAll("  ", "&nbsp;&nbsp;");

		// nicer source formatting
		html = html.replace("<br>", "\n<br>");

		// remove very first br
		html = html.replaceFirst("<body>(\\s*<br>)*",
			"<body style=\"font-family: monospace; font-size: 12pt;\">\n    ");
		html = html.replaceFirst("(<br>\\s*)*</body>", "<br><br></body>");

		// System.out.println(html);

		return html;
	}

	public static String toHyperLink(String html) {
		// wrap hyperlinks http(s)
		html = html.replaceAll("(https{0,1}://[a-zA-Z0-9._+-/?=~!@#$%&:]+[^]. )<\t])",
			"<a href=\"$1\">$1</a>");

		return html;
	}

	public static String toBoldAndItalic(String html) {
		// underscores
		html = html.replaceAll("([\\s>*])_([^\\s_]+( [^\\s_]+?){0,4}?)_(?=[\\s<*])",
			"$1_<u>$2</u>_");

		// bolds
		html = html.replaceAll("([\\s>_])\\*([^\\s*]+( [^\\s*]+?){0,4}?)\\*(?=[\\s<_])",
			"$1*<b>$2</b>*");
		return html;
	}

	public void update() {

		if (toShow == null || !(toShow instanceof PrimaryDocument || toShow instanceof Project)) {
			setEnabled(false);
			pane.setText("");
		} else {
			setEnabled(true);
			if (toShow instanceof PrimaryDocument) {

				final PrimaryDocument pd = (PrimaryDocument) toShow;

				pane.setContentType("text/html");
				pane.setFont(new Font("Courier", 0, 10));

				// Do not call setMetaData to void this triggering a file
				// change.
				pd.getMetaData().put("lastseen", new DateTime().toString());

				pane.setText("loading...");

				// getText is a potentially long running operation...
				commonService.run(new Runnable() {
					public void run() {
						String text = pd.getText(gmane);
						String html = toHTML(text);

						pane.setText(html);

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								invalidate();
								resetScrollPosition();
							}
						});
					}
				}, "Error Fetching Text from Gmane");

			} else if (toShow instanceof Project) {
				pane.setText("Number of primary documents: "
					+ ((Project) toShow).getPrimaryDocuments());
			}
		}

	}

	public void resetScrollPosition() {

		if (scrollOnShow.getValue()) {
			Rectangle rect = null;
			try {
				Document d = pane.getDocument();
				String content = d.getText(0, d.getLength()).toLowerCase();

				int index = -1;
				if (search != null) {
					index = content.indexOf(search);
				}

				if (index != -1) {
					rect = pane.modelToView(index);
				}

			} catch (BadLocationException e) {
				// If this happens we just put the scroll bar to 0
			}

			if (rect != null) {
				rect.y = Math.max(0, rect.y - 50);
				rect.height = rect.height + 50;
				pane.scrollRectToVisible(rect);
				return;
			}
		}

		getVerticalScrollBar().setValue(0);
	}
}
