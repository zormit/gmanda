package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.picocontainer.annotations.Inject;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.LaTeXDirectoryChooser;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.gui.EnableComponentBridge;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

public class ExportCitationsToLatexAction extends AbstractAction {

	@Inject
	CommonService commonService;

	@Inject
	LaTeXDirectoryChooser latexFileChooser;

	ProjectProxy proxy;

	public ExportCitationsToLatexAction(ProjectProxy proxy) {
		super("Export Citations to LaTeX");

		this.proxy = proxy;

		EnableComponentBridge.connectNonNull(this, proxy);

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	}

	public List<String> getCitationList(File dir) {

		List<String> result = new ArrayList<String>();

		for (File tex : dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".tex"); // && !name.equals("glossary.tex");
			}
		})) {

			String input;
			try {
				input = FileUtils.readFileToString(tex);
			} catch (IOException e) {
				throw new ReportToUserException(e);
			}

			Pattern p = Pattern.compile("\\\\dref\\s*\\{((?:\\w|\\s|[@\\-.?!'\\(\\)])+?)\\}\\s*\\{((?:\\w|\\s|[@\\-.?!'\\(\\)])+?)\\}");
			Matcher m = p.matcher(input);

			while (m.find()) {
				String code = m.group(1).replaceAll("\\s+", " ").trim();
				// String value = m.group(2).trim();

				result.add(code);
			}
		}
		return result;
	}

	public Section buildSection(Project p, Section section, Collection<String> codes) {

		section.initializeDefinition(p);

		for (String code : codes) {

			// Skip the code of the section itself
			if (code.equals(section.code))
				continue;
			
			String shortCode;
			
			if (section.code != null && code.startsWith(section.code + ".")) {
				shortCode = code.substring(section.code.length() + 1);
			} else {
				shortCode = code;
			}
			
			Section item = new Section(shortCode, code);
			
			item.initializeDefinition(p);

			section.getItems().add(item);
		}
		
		return section;
	}

	public void actionPerformed(ActionEvent arg0) {

		commonService.run(new Runnable() {
			public void run() {

				Project p = proxy.getVariable();
				if (p == null)
					return;

				File openDir = latexFileChooser.getOpenFile();

				if (openDir == null)
					return;

				IProgress progress = commonService.getProgressBar("Exporting LaTeX-Citations", 5);

				try {
					List<String> citations = getCitationList(openDir);

					List<Section> categories = new ArrayList<Section>();

					categories.add(new Section("Innovations", "innovation"));
					categories.add(new Section("Episodes", "episode"));
					categories.add(new Section("Projects", "project"));
					categories.add(new Section("Activities", "activity"));
					categories.add(new Section("Concepts", "concept"));
					
					Section uncategorized = new Section("Uncategorized Codes", null);
					uncategorized.definition = ""; // Set to non-null so that it is not queried for a definition					
					
					Multimap<Section, String> citationsByCategory = new TreeMultimap<Section, String>();

					nextCitation: for (String citation : citations) {

						for (Section section : categories) {

							Code c = CodedStringFactory.parseOne(section.code + ".*");

							if (c.matches(CodedStringFactory.parseOne(citation))) {
								citationsByCategory.put(section, citation);
								continue nextCitation;
							}
						}
						citationsByCategory.put(uncategorized, citation);
					}

					List<Section> sections = new ArrayList<Section>();

					for (Entry<Section, Collection<String>> entry : citationsByCategory.asMap()
						.entrySet()) {

						sections.add(buildSection(p, entry.getKey(), entry.getValue()));
					}

					uncategorized.definition = null; // Set to null again, so no definition is generated in the LaTeX output
					uncategorized.code = "uncategorized"; 
					
					// Run template engine
					String glossary = runVelocity(sections);

					try {
						FileUtils.writeStringToFile(new File(openDir, "glossary.tex"), glossary);
					} catch (IOException e) {
						throw new ReportToUserException(e);
					}
				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}
			}
		}, "Error exporting statistics");
	}

	public class Section implements Comparable<Section> {

		public Section(String title, String code) {
			this.title = title;
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public String getCode() {
			return code;
		}

		public String getDefinition() {
			return definition;
		}

		public List<Section> getSections() {
			return sections;
		}

		public List<Section> getItems() {
			// This method only exists for namings sake
			return sections;
		}

		public void initializeDefinition(Project p) {

			if (definition != null)
				return;
			
			for (Entry<PrimaryDocument, Code> defs : p.getCodeModel().getValues(code, "def")
				.entries()) {

				String value = defs.getValue().getValue();
				
				if (value != null){
					value = StringUtils.strip(value, " \"");
					
					value = value.split("----", 2)[0].trim();
					
					if (value.length() > 0){
						if (definition == null)
							definition = value;
						else
							definition += "\n\n" + value;
					}
				}
			}

			if (definition == null) {
				System.out.println("WARN: No definition found for " + code);
				definition = "<No Definition>";
			}
		}

		String title;

		String code;

		String definition;

		List<Section> sections = new LinkedList<Section>();

		public int compareTo(Section o) {
			return this.title.compareTo(o.title);
		}
	}

	Template latexTemplate;

	VelocityContext context;

	public String runVelocity(List<Section> sections) {

		if (true || context == null) {

			Properties p = new Properties();

			p.setProperty("resource.loader", "class, file");
			p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			try {
				Velocity.init(p);
				
				context = new VelocityContext();
				
				latexTemplate = Velocity.getTemplate("resources/templates/glossary.vm");

			} catch (Exception e) {
				throw new DoNotShowToUserException(e);
			}
		}

		context.put("sections", sections);

		StringWriter writer = new StringWriter();
		try {
			latexTemplate.merge(context, writer);

			writer.close();
		} catch (Exception e) {
			throw new DoNotShowToUserException(e);
		}

		return writer.toString();
	}
}
