package de.fu_berlin.inf.gmanda.gui.tabulation;

import java.awt.Dimension;
import java.awt.Font;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.gui.CodeAsTextView;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.proxies.CodeDetailProxy;
import de.fu_berlin.inf.gmanda.proxies.FilterTextProxy;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SelectionProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Pair;
import de.fu_berlin.inf.gmanda.util.CStringUtils;

public class TabulationCanvas extends JScrollPane {

	JTextPane pane = new JTextPane();

	ProjectProxy project;

	CodeDetailProxy codeDetailProxy;

	SelectionProxy selection;

	FilterTextProxy filter;

	CommonService commonService;

	public TabulationCanvas(ProjectProxy projectProxy, SelectionProxy selection,
		FilterTextProxy filter, CommonService common) {
		super();

		this.selection = selection;
		this.project = projectProxy;
		this.filter = filter;
		this.commonService = common;

		pane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					String filename = arg0.getDescription();
					if (filename.startsWith("gmane://")) {

						PrimaryDocument pd = project.getVariable().getCodeModel().getByFilename(
							filename);

						if (pd != null)
							TabulationCanvas.this.selection.setVariable(pd);
					}
					if (filename.startsWith("gmaneFilter://")) {
						try {
							TabulationCanvas.this.filter.setVariable(URLDecoder.decode(filename
								.substring("gmaneFilter://".length()), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							TabulationCanvas.this.filter.setVariable(filename
								.substring("gmaneFilter://".length()));
						}
					}

				}
			}
		});

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

	public Iterable<PrimaryDocument> getDocs() {
		return PrimaryDocument.getTreeWalker(project.getVariable().getPrimaryDocuments());
	}

	public void update(final TabulationSettings settings) {

		pane.setText("computing tabulation...");

		commonService.run(new Runnable() {
			public void run() {

				Project p = project.getVariable();
				if (p == null)
					return;

				List<PrimaryDocument> x = p.getCodeModel().getPrimaryDocuments(settings.xDim);

				LinkedHashSet<Set<String>> table = new LinkedHashSet<Set<String>>();
				LinkedHashSet<String> headerRow = new LinkedHashSet<String>();
				headerRow.add(CodeAsTextView.toFilterA(settings.yDim));
				headerRow.add(CodeAsTextView.toFilterA(settings.xDim));

				table.add(headerRow);

				for (Pair<String, List<PrimaryDocument>> pair : p.getCodeModel().partition(x,
					settings.yDim)) {

					LinkedHashSet<String> row = new LinkedHashSet<String>();

					row.add(CodeAsTextView.toFilterA(pair.p));
					row.add(CStringUtils.join(Lists.transform(pair.v,
						new Function<PrimaryDocument, String>() {
							public String apply(PrimaryDocument pd) {
								return CodeAsTextView.toA(pd);
							}
						}), " "));

					table.add(row);
				}

				String s = runVelocity(table);

				pane.setText(s);
			}
		}, "Error calculating tabulation");
	}

	Template tableTemplate;

	VelocityContext context;

	public void initVelocity() {

		if (context == null) {

			Properties p = new Properties();

			p.setProperty("resource.loader", "class, file");
			p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			try {
				Velocity.init(p);

				context = new VelocityContext();

				tableTemplate = Velocity.getTemplate("resources/templates/table.vm");
			} catch (Exception e) {
				throw new DoNotShowToUserException(e);
			}
		}
	}

	public String runVelocity(Set<Set<String>> table) {

		initVelocity();

		context.put("rows", table);

		StringWriter writer = new StringWriter();
		try {
			tableTemplate.merge(context, writer);

			writer.close();
		} catch (Exception e) {
			throw new DoNotShowToUserException(e);
		}

		return writer.toString();
	}

}