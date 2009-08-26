package de.fu_berlin.inf.gmanda.gui.tabulation;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.picocontainer.annotations.Inject;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import de.fu_berlin.inf.gmanda.exports.VelocitySupport;
import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.misc.GmandaHyperlinkListener;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.qda.Slice;
import de.fu_berlin.inf.gmanda.qda.ToHtmlHelper;
import de.fu_berlin.inf.gmanda.util.CMultimap;
import de.fu_berlin.inf.gmanda.util.CStringUtils;
import de.fu_berlin.inf.gmanda.util.CUtils;

public class TabulationCanvas extends JScrollPane {

	@Inject
	ProjectProxy project;

	@Inject
	CommonService commonService;
	
	@Inject
	VelocitySupport velocity;

	JTextPane pane = new JTextPane();

	public TabulationCanvas(GmandaHyperlinkListener linkListener) {
		super();

		pane.addHyperlinkListener(linkListener);

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
		return PrimaryDocument.getTreeWalker(project.getVariable()
				.getPrimaryDocuments());
	}

	public <T> Map<String, Slice<T>> byValue(Iterable<? extends Code> codes,
			Slice<T> s) {

		for (Code c : codes) {
			s = s.select(c);
		}

		return s.slice();
	}

	public <T> Multimap<String, T> preprocess(String dim, Slice<T> all) {

		Multimap<String, T> result = new TreeMultimap<String, T>();

		if (dim == null || dim.trim().length() == 0) {
			return result;
		}

		LinkedList<Code> codes = Lists.newLinkedList(CodedStringFactory.parse(
				dim).getAllCodes());

		String lastTag = codes.getLast().getTag();
		int depth;
		try {
			depth = Integer.parseInt(lastTag);
			codes.removeLast();
		} catch (Exception e) {
			depth = 0;
		}

		Map<String, Slice<T>> slices = new TreeMap<String, Slice<T>>(byValue(
				codes, all));

		for (Entry<String, Slice<T>> row : slices.entrySet()) {
			String codeString = row.getKey();

			if (codeString.equals("def") || codeString.equals("quote")
					|| codeString.equals("date") || codeString.equals("desc"))
				continue;

			if (depth > 0) {
				codeString = StringUtils.join(CUtils.first(CodedStringFactory
						.parseOne(codeString).getTagLevels(), depth), ".");
			}

			result.putAll(codeString, row.getValue().getDocuments().keySet());
		}
		return result;
	}

	int joinSkipLevels = 0;

	public void update(final TabulationSettings settings) {

		pane.setText("computing tabulation...");

		commonService.run(new Runnable() {

			public void run() {

				try {
					if (settings.groupBy.trim().length() == 0) {
						joinSkipLevels = 0;
						tabulateByPrimaryDocuments(settings);
						return;
					}

					if (settings.yDim.trim().length() == 0) {
						settings.yDim = null;
					}

					if (settings.xDim.trim().length() == 0) {
						settings.xDim = null;
					}

					if (settings.xDim == null) {
						settings.noIntersectX = true;
					}

					if (settings.yDim == null) {
						settings.noIntersectY = true;
					}

					Project p = project.getVariable();
					if (p == null)
						return;

					String filterBy = settings.filterBy.trim();
					if (filterBy.length() == 0) {
						filterBy = "episode";
					}
					Code c = CodedStringFactory.parseOne(filterBy);
					Slice<PrimaryDocument> all = p.getCodeModel()
							.getInitialFilterSlice(c.getTag());

					do {
						List<? extends Code> props = c.getProperties();

						if (props.size() == 0)
							break;

						c = props.get(0);

						if (c.getTag().equals("desc")
								&& c.getProperties().size() == 1) {
							break;
						}
						all = all.select(c);
					} while (true);

					joinSkipLevels = CodedStringFactory.parseOne(
							settings.groupBy).getTagLevels().size();
					Slice<String> grouped = all.sliceAndPack(settings.groupBy,
							0);

					TreeSet<String> allGrouped = new TreeSet<String>(grouped
							.getDocuments().keySet());

					Multimap<String, String> xSlices = preprocess(
							settings.xDim, grouped);
					Multimap<String, String> ySlices = preprocess(
							settings.yDim, grouped);

					CMultimap<String, String> xCheckSlices = new CMultimap<String, String>(
							xSlices);

					List<List<String>> table = new ArrayList<List<String>>();

					{ // Header row
						List<String> headerRow = new ArrayList<String>();
						String xHeader = ToHtmlHelper.toFilterA(getLastRelevantTag(settings.xDim)) + " &rarr;<br/>";
						if (settings.yDim != null) {
							headerRow.add(xHeader + ToHtmlHelper.toFilterA(getLastRelevantTag(settings.yDim)) + " &darr;");
						} else {
							headerRow.add(xHeader + "No y-dimension &darr;");
						}

						for (String s : xSlices.keySet()) {
							headerRow.add(ToHtmlHelper.toFilterA(s));
						}

						if (settings.noIntersectX) {
							headerRow.add("[no intersection]");
						}

						table.add(headerRow);
					}

					for (Entry<String, Collection<String>> row : ySlices
							.asMap().entrySet()) {

						String rowTag = row.getKey();
						Collection<String> rowPDs = row.getValue();
						HashSet<String> checkRowPDs = new HashSet<String>(
								rowPDs);

						List<String> htmlRow = new ArrayList<String>();

						htmlRow.add(ToHtmlHelper.toFilterA(rowTag));

						for (Entry<String, Collection<String>> column : xSlices
								.asMap().entrySet()) {

							String columnTag = column.getKey();
							Collection<String> columnPDs = column.getValue();

							@SuppressWarnings("unchecked")
							LinkedList<String> cell = new LinkedList<String>(
									CollectionUtils.intersection(rowPDs,
											columnPDs));

							checkRowPDs.removeAll(cell);
							xCheckSlices.get(columnTag).removeAll(cell);
							allGrouped.removeAll(cell);

							htmlRow.add(join(cell));
						}

						if (settings.noIntersectX) {
							htmlRow.add(join(checkRowPDs));
						}

						table.add(htmlRow);
					}

					if (settings.noIntersectY) {
						// Into the last row, put all items, that you could not
						// match so far.
						List<String> htmlRow = new ArrayList<String>();
						htmlRow.add("[no intersection]");

						for (Entry<String, Collection<String>> row : xCheckSlices
								.entrySet()) {
							Collection<String> column = row.getValue();
							htmlRow.add(join(column));
							allGrouped.removeAll(column);
						}
						if (settings.noIntersectX) {
							htmlRow.add(join(allGrouped));
						}
						table.add(htmlRow);
					}

					// Remove all completely empty rows and columns
					if (settings.removeEmptyRowsColumns) {

						// Delete Rows
						nextRow: for (Iterator<List<String>> iterator = table
								.iterator(); iterator.hasNext();) {
							List<String> list = iterator.next();

							Iterator<String> column = list.iterator();
							column.next(); // Skip header

							while (column.hasNext()) {
								if (column.next().trim().length() > 0)
									continue nextRow;
							}
							iterator.remove();
						}

						// Delete Columns
						@SuppressWarnings("unchecked")
						Iterator<String>[] columnIts = new Iterator[table
								.size()];
						int i = 0;
						for (List<String> list : table) {
							columnIts[i] = list.iterator();
							columnIts[i].next();
							i++;
						}

						while (columnIts[0].hasNext()) {
							columnIts[0].next();
							
							boolean deleteRow = true;
							for (i = 1; i < columnIts.length; i++) {
								if (columnIts[i].next().trim().length() > 0) {
									deleteRow = false;
								}
							}

							if (deleteRow) {
								for (Iterator<String> it : columnIts) {
									it.remove();
								}
							}
							
						}
					}

					String text = velocity.run("rows", table, "table");

					pane.setText(text);
				} catch (RuntimeException e) {
					pane.setText(CUtils.getStackTrace(e));
					throw e;
				}
			}

			private void tabulateByPrimaryDocuments(TabulationSettings settings) {

				Project p = project.getVariable();
				if (p == null)
					return;

				Slice<PrimaryDocument> all = p.getCodeModel()
						.getInitialFilterSlice("episode");

				Multimap<String, PrimaryDocument> xSlices = preprocess(
						settings.xDim, all);
				Multimap<String, PrimaryDocument> ySlices = preprocess(
						settings.yDim, all);

				CMultimap<String, PrimaryDocument> xCheckSlices = new CMultimap<String, PrimaryDocument>(
						xSlices);

				List<List<String>> table = new ArrayList<List<String>>();

				{ // Header row
					List<String> headerRow = new ArrayList<String>();
					
					String xHeader = ToHtmlHelper.toFilterA(getLastRelevantTag(settings.xDim)) + " &rarr;<br/>";
					if (settings.yDim != null) {
						headerRow.add(xHeader + ToHtmlHelper.toFilterA(getLastRelevantTag(settings.yDim)) + " &darr;");
					} else {
						headerRow.add(xHeader + "No y-dimension &darr;");
					}
					
					for (String s : xSlices.keySet()) {
						headerRow.add(ToHtmlHelper.toFilterA(s));
					}
					headerRow.add("[no intersection]");
					table.add(headerRow);
				}

				for (Entry<String, Collection<PrimaryDocument>> row : ySlices
						.asMap().entrySet()) {

					String rowTag = row.getKey();
					Collection<PrimaryDocument> rowPDs = row.getValue();
					HashSet<PrimaryDocument> checkRowPDs = new HashSet<PrimaryDocument>(
							rowPDs);

					List<String> htmlRow = new ArrayList<String>();

					htmlRow.add(ToHtmlHelper.toFilterA(rowTag));

					for (Entry<String, Collection<PrimaryDocument>> column : xSlices
							.asMap().entrySet()) {

						String columnTag = column.getKey();
						Collection<PrimaryDocument> columnPDs = column
								.getValue();

						@SuppressWarnings("unchecked")
						LinkedList<PrimaryDocument> cell = new LinkedList<PrimaryDocument>(
								CollectionUtils.intersection(rowPDs, columnPDs));

						checkRowPDs.removeAll(cell);
						xCheckSlices.get(columnTag).removeAll(cell);

						htmlRow.add(join(cell));
					}

					htmlRow.add(join(checkRowPDs));

					table.add(htmlRow);
				}

				List<String> htmlRow = new ArrayList<String>();
				htmlRow.add("[no intersection]");

				for (Entry<String, Collection<PrimaryDocument>> row : xCheckSlices
						.entrySet()) {
					htmlRow.add(join(row.getValue()));
				}

				table.add(htmlRow);

				String text = velocity.run("rows", table, "table");

				pane.setText(text);
			}

		}, "Error calculating tabulation");
	}
	
	public static String getLastRelevantTag(String code){
		LinkedList<Code> codes = Lists.newLinkedList(CodedStringFactory.parse(
				code).getAllCodes());

		if (codes.size() == 0){
			return "";
		}
		
		String lastTag = codes.getLast().getTag();
		try {
			Integer.parseInt(lastTag);
			codes.removeLast();
		} catch (Exception e) {
			// The last code is not a number
		}
		
		if (codes.size() == 0)
			return "";
		
		return codes.getLast().getTag();
	}

	private String join(Iterable<?> cell) {
		return CStringUtils.join(Iterables.transform(cell,
				new Function<Object, String>() {
					public String apply(Object o) {
						if (o instanceof PrimaryDocument)
							return ToHtmlHelper.toA((PrimaryDocument) o, null);
						else
							return ToHtmlHelper.toFilterA(joinSkipLevels, o
									.toString());
					}
				}), "<br>");
	}


}