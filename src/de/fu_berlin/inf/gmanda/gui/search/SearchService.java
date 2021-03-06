package de.fu_berlin.inf.gmanda.gui.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.picocontainer.annotations.Inject;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Nullable;
import de.fu_berlin.inf.gmanda.util.StringJoiner;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Service to search within all primary documents those that either contain a
 * text (using LuceneService) or are tagged (using CodeModel);
 * 
 * For Lucene use the following syntax:
 * 
 * search: "<term to search for"
 * 
 * for Metadata use:
 * 
 * search: { from: "Bogdan", OR from: "Aleksandr" }
 * 
 * for Date based filtering use:
 * 
 * date: <2007-04-01T12:00:00
 * 
 * or
 * 
 * date: >2007-05-01
 */
public class SearchService {

	private static final Logger log = Logger.getLogger(SearchService.class);

	@Inject
	LuceneFacade luceneFacade;

	@Inject
	CommonService commonService;

	@Inject
	SearchStringProxy searchStringProxy;

	/**
	 * Given a Project and a string to filter for, this method returns a list of
	 * all those primary documents which are selected by the filterString
	 */
	public List<PrimaryDocument> filter(final String filterString,
			@Nullable IProgress pBar, Project project) {
		Iterable<? extends Code> codesIterable = CodedStringFactory.parse(
				filterString).getAllCodes();

		if (pBar == null)
			pBar = commonService.getProgressBar("Filtering");

		IProgress subProgress = pBar.getSub(100);

		TreeSet<PrimaryDocument> newFilterList;

		try {
			subProgress
					.setScale(CollectionUtils.size(codesIterable.iterator()));
			subProgress.start();

			Iterator<? extends Code> codes = codesIterable.iterator();

			if (!codes.hasNext()) {
				newFilterList = new TreeSet<PrimaryDocument>();
				CollectionUtils.addAll(newFilterList, PrimaryDocument
						.getTreeWalker(project.getPrimaryDocuments())
						.iterator());
			} else {
				Code c = codes.next();
				String nextSearchString = c.getTag();

				if (nextSearchString.startsWith("^")
						|| nextSearchString.startsWith("-")) {
					newFilterList = new TreeSet<PrimaryDocument>();
					CollectionUtils.addAll(newFilterList, PrimaryDocument
							.getTreeWalker(project.getPrimaryDocuments())
							.iterator());
					newFilterList.removeAll(getSetFromSearchString(c, project));
				} else {
					newFilterList = new TreeSet<PrimaryDocument>(
							getSetFromSearchString(c, project));
				}

				if (subProgress.isCanceled())
					return null;

				subProgress.work(1);

				while (codes.hasNext()) {
					c = codes.next();
					nextSearchString = c.getTag();

					// Remove from hits if starting with - or ^
					if (nextSearchString.startsWith("^")
							|| nextSearchString.startsWith("-")) {
						newFilterList.removeAll(getSetFromSearchString(c,
								project));
					} else {
						if (nextSearchString.startsWith("|")) {

							Collection<PrimaryDocument> nextSet = getSetFromSearchString(
									c, project);

							nextSet.removeAll(newFilterList);

							newFilterList.addAll(nextSet);
						} else {
							newFilterList.retainAll(getSetFromSearchString(c,
									project));
						}
					}

					if (subProgress.isCanceled())
						return null;
					subProgress.work(1);
				}
			}
		} finally {
			subProgress.done();
		}

		if (pBar.isCanceled())
			return null;

		return new ArrayList<PrimaryDocument>(newFilterList);
	}

	public static String cleanStringForSearch(String s) {

		s = s.trim();

		if (s.startsWith("\"") && s.endsWith("\"")) {

			// Remove leading/trailing double quotes
			s = s.substring(1, s.length() - 1);

			// Unescape
			s = StringEscapeUtils.unescapeJava(s);
		}

		return s;
	}

	public Collection<PrimaryDocument> getSetFromSearchString(Code c,
			Project project) {

		String nextSearchString = c.getTag();

		char operation = '+';

		if (nextSearchString.startsWith("^")
				|| nextSearchString.startsWith("-")
				|| nextSearchString.startsWith("|")) {
			operation = nextSearchString.charAt(0);
			nextSearchString = nextSearchString.substring(1);
		}

		if ("date".equals(nextSearchString) && c.getValue() != null) {

			String searchString = c.getValue().trim();

			if (searchString.startsWith("<") || searchString.startsWith(">")) {
				operation = searchString.charAt(0);
				searchString = searchString.substring(1);
			}

			DateTime compareAgainst;
			try {
				compareAgainst = new DateTime(searchString);
			} catch (IllegalArgumentException e) {
				log.error("Could not parse DateTime");
				return Collections.emptyList();
			}

			LinkedList<PrimaryDocument> result = new LinkedList<PrimaryDocument>();

			int n = 0;
			int i = 0;

			for (PrimaryDocument doc : PrimaryDocument.getTreeWalker(project
					.getPrimaryDocuments())) {

				n++;

				String date = doc.getMetaData("date");
				if (date != null) {
					DateTime time;
					try {
						time = new DateTime(date);
					} catch (IllegalArgumentException e) {
						continue;
					}
					if (operation == '<') {
						if (time.isBefore(compareAgainst)) {
							result.add(doc);
							i++;
						}
					}
					if (operation == '>') {
						if (time.isAfter(compareAgainst)) {
							result.add(doc);
							i++;
						}
					}

				}
			}
			System.out.println(n + " " + i);
			return result;

		} else

		// Search using Lucene if code is "search"
		if ("search".equals(nextSearchString) && c.getValue() != null
				&& c.getValue().trim().length() > 1) {

			String searchString;

			List<? extends Code> props = c.getProperties();

			if (props.size() == 1 && props.get(0).getTag().equals("desc")) {
				searchString = cleanStringForSearch(c.getValue());
			} else {
				StringJoiner sb = new StringJoiner(" ");
				for (Code sub : props) {
					sb.append(cleanStringForSearch(sub.toString()));
				}
				searchString = sb.toString();
			}

			searchStringProxy.setVariable(searchString);

			Iterator<PrimaryDocument> it = luceneFacade.search(project,
					searchString);
			LinkedList<PrimaryDocument> result = new LinkedList<PrimaryDocument>();
			CollectionUtils.addAll(result, it);

			return result;
		} else {
			if ("*".equals(nextSearchString)) {

				Set<PrimaryDocument> result = new HashSet<PrimaryDocument>();
				for (PrimaryDocument doc : project.getCodeModel()
						.getPrimaryDocuments()) {
					if (doc.getParent() != null)
						result.add(doc);
				}
				return result;
			} else {
				List<PrimaryDocument> results = project.getCodeModel()
						.getPrimaryDocuments(nextSearchString);

				switch (operation) {
				case '-':
					// Only return pds in which all segments include the search
					// string.

					if (!nextSearchString.endsWith(".*"))
						nextSearchString = nextSearchString + ".*";

					List<PrimaryDocument> filteredResults = new ArrayList<PrimaryDocument>();
					for (PrimaryDocument result : results) {
						if (CodedStringFactory.parse(result.getCodeAsString())
								.containsAny(nextSearchString))
							filteredResults.add(result);
					}
					return filteredResults;

				default:
					return results;
				}
			}
		}
	}

}
