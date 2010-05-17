package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.picocontainer.annotations.Inject;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.LaTeXDirectoryChooser;
import de.fu_berlin.inf.gmanda.gui.preferences.DebugModeProperty;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.AbstractCodedString;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.gui.EnableComponentBridge;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.tree.Childrenable;
import de.fu_berlin.inf.gmanda.util.tree.ChildrenableTreeWalker;

public class ExportCitationsToLatexAction extends AbstractAction {

	@Inject
	CommonService commonService;

	@Inject
	LaTeXDirectoryChooser latexFileChooser;

	@Inject
	DebugModeProperty debugMode;

	@Inject
	VelocitySupport velocity;

	public static final String DEFINITION_MACRO_NAME = "dref";

	ProjectProxy proxy;

	public ExportCitationsToLatexAction(ProjectProxy proxy) {
		super("Export Citations to LaTeX");

		this.proxy = proxy;

		EnableComponentBridge.connectNonNull(this, proxy);

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	}

	public String hyphenate(String s) {

		s = s.replace(".", "\\dothyp{}");
		s = s.replace("@", "\\atnohyp{}");

		return s;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCitationList(File dir) {

		List<String> result = new ArrayList<String>();

		for (File tex : (Collection<File>) FileUtils.listFiles(dir,
				new String[] { "tex" }, true)) {

			String input;
			try {
				input = FileUtils.readFileToString(tex);
			} catch (IOException e) {
				throw new ReportToUserException(e);
			}

			extractCodes(
					input,
					"\\\\"
							+ DEFINITION_MACRO_NAME
							+ "\\s*\\{((?:\\w|\\s|[@\\-.?!'\\(\\)])+?)\\}\\s*\\{((?:\\w|\\s|\\/|[@\\-.?!'\\(\\)])+?)\\}",
					result);
			extractCodes(input, "\\\\ddref"
					+ "\\s*\\{((?:\\w|\\s|[@\\-.?!'\\(\\)])+?)\\}", result);
		}
		return result;
	}

	protected void extractCodes(String input, String pattern,
			List<String> result) {

		Pattern p = Pattern.compile(pattern);

		Matcher m = p.matcher(input);

		while (m.find()) {
			String code = m.group(1).replaceAll("\\s+", " ").trim();
			// String value = m.group(2).trim();
			System.out.println(code);

			result.add(code);
		}
	}

	public Section buildSection(Project p, Section section,
			Collection<String> codes) {

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

			Section item = new Section(section, shortCode, code, null, null);

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

				IProgress progress = commonService.getProgressBar(
						"Exporting LaTeX-Citations", 5);

				try {
					List<String> citations = getCitationList(openDir);

					for (Code c : p.getCodeModel().getAllCodesDeep("export").values()) {
						export(c, p, openDir, citations);
					}

				} catch (Exception e) {
					throw new ReportToUserException(e);
				} finally {
					progress.done();
				}
			}
		}, "Error exporting statistics");
	}

	public class Section implements Comparable<Section>, Childrenable<Section> {

		protected String text;

		public Section(Section parent, String title, String code, String label,
				String text) {
			this.title = title;
			this.code = code;
			this.label = label;
			this.parent = parent;
			this.text = text;
		}

		public Section getParent() {
			return parent;
		}

		public String getTitle() {
			return hyphenate(title);
		}

		public String getCode() {
			return code;
		}

		public String getLabel() {
			if (label == null) {
				return "def:" + getCode();
			} else {
				return label;
			}
		}

		public int getSectionDepth() {

			Section parent = this.getParent();
			if (parent == null)
				return 0;

			return parent.getSectionDepth() + 1;
		}

		public String getSectionMacro() {
			
			String result = sectionDepths.get((Integer)getSectionDepth());
			if (result == null)
				return "section";
			return result;
		}
		

		public String getCodeHyp() {
			return hyphenate(code);
		}

		public String getDefinition() {
			if (text != null)
				return text;
			return definition;
		}

		public List<Section> getSections() {
			return sections;
		}

		public List<Section> getItems() {
			return items;
		}

		public void initializeDefinition(Project p) {

			if (definition != null || text != null)
				return;

			for (Entry<PrimaryDocument, Code> defs : p.getCodeModel()
					.getValues(code, "def").entries()) {

				Code defCode = defs.getValue();

				// Check if there is an explicit title given in the definition
				Collection<? extends Code> titles = defCode
						.getProperties("title");
				if (titles.size() > 0) {
					String newTitle = titles.iterator().next().getValue();
					newTitle = StringUtils.strip(newTitle, " \"");
					if (newTitle.length() > 0) {
						// If so, we overwrite the given title
						title = newTitle;
					}
				}

				for (Code def : defCode.getProperties("desc")) {

					String value = def.getValue();

					if (value != null) {
						value = StringUtils.strip(value, " \"");

						value = value.split("----", 2)[0].trim();

						if (value.length() > 0) {
							if (definition == null)
								definition = value;
							else
								definition += "\n\n" + value;
						}
					}
				}
			}

			if (definition == null) {
				// @SECURITY should sanitize the code
				definition = "\\PackageWarning{GmanDA}{No Definition found for '"
						+ code + "'	}" + "No Definition found";
			}
			definition = definition.trim();
		}

		Section parent;

		String title;

		String code;

		String label;

		String definition;

		List<Section> sections = new LinkedList<Section>();

		List<Section> items = new LinkedList<Section>();

		public int compareTo(Section o) {
			return this.title.compareTo(o.title);
		}

		@Override
		public Collection<Section> getChildren() {
			return getSections();
		}
	}

	protected BiMap<Integer, String> sectionDepths;
	
	protected void export(Code exportInstructions, Project p, File openDir,
			List<String> citations) {

		String exportSectionDepth = AbstractCodedString
				.getFirstPropertyValueClean(exportInstructions, "sectionDepths",
						"section,subsection,subsubsection,paragraph,subparagraph");
		
		sectionDepths = HashBiMap.create();
		int i = 0;
		for (String depth : exportSectionDepth.split("\\s*,\\s*")){
			sectionDepths.put(i++, depth);
		}
		
		List<Section> categoriesNested = new ArrayList<Section>();

		for (Code subCode : exportInstructions.getProperties("section")) {
			categoriesNested.add(buildCategories(null, subCode));
		}

		Section uncategorized = null;

		List<Section> categories = new ArrayList<Section>();
		for (Section s : new ChildrenableTreeWalker<Section>(categoriesNested)) {
			if ("-".equals(s.code)) {
				uncategorized = s;
			}
			categories.add(s);
		}

		if (uncategorized == null) {
			uncategorized = new Section(null, "Uncategorized Codes", null,
					"sec:uncategorized", null);
		}

		/**
		 * // Set to non-null so that it // is not queried for a // definition
		 */
		uncategorized.definition = "";

		Multimap<Section, String> citationsByCategory = TreeMultimap.create();

		nextCitation: for (String citation : citations) {

			for (Section section : categories) {

				if (section.code == null || "-".equals(section.code))
					continue;

				Code c = CodedStringFactory.parseOne(section.code + ".*");

				if (c.matches(CodedStringFactory.parseOne(citation))) {
					citationsByCategory.put(section, citation);
					continue nextCitation;
				}
			}
			citationsByCategory.put(uncategorized, citation);
		}

		for (Entry<Section, Collection<String>> entry : citationsByCategory
				.asMap().entrySet()) {

			buildSection(p, entry.getKey(), entry.getValue());
		}

		/*
		 * Set to null again, so no definition is generated in the LaTeX output
		 */
		uncategorized.definition = null;
		uncategorized.code = "uncategorized";

		// Run template engine
		String glossary = velocity
				.run("sections", categoriesNested, "glossary");

		String filename = AbstractCodedString.getFirstPropertyValueClean(
				exportInstructions, "file", "glossary.tex");
		try {
			FileUtils.writeStringToFile(new File(openDir, filename), glossary);
		} catch (IOException e) {
			throw new ReportToUserException(e);
		}
	}

	private Section buildCategories(Section parent, Code c) {

		String title = AbstractCodedString.getFirstPropertyValueClean(c,
				"title", "unknown");
		String code = AbstractCodedString.getFirstPropertyValueClean(c, "code",
				null);
		String label = AbstractCodedString.getFirstPropertyValueClean(c,
				"label", null);
		String text = AbstractCodedString.getFirstPropertyValueClean(c, "text",
				null);

		Section newSection = new Section(parent, title, code, label, text);

		for (Code subCode : c.getProperties("section")) {
			newSection.getSections().add(buildCategories(newSection, subCode));
		}

		return newSection;
	}
}
