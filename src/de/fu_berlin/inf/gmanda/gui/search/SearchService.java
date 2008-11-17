package de.fu_berlin.inf.gmanda.gui.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.picocontainer.annotations.Inject;

import com.google.common.base.Nullable;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.StringJoiner;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Service to search within all primary documents those that either contain a
 * text (using LuceneService) or are tagged (using CodeModel);
 */
public class SearchService {

	@Inject
	LuceneFacade luceneFacade;
	
	@Inject
	CommonService commonService;
	
	@Inject
	SearchStringProxy searchStringProxy;

	public List<PrimaryDocument> filter(final String filterString, @Nullable
		IProgress pBar, Project project) {
		Iterable<? extends Code> codesIterable = CodedStringFactory.parse(filterString)
			.getAllCodes();

		if (pBar == null)
			pBar = commonService.getProgressBar("Filtering");
		
		IProgress subProgress = pBar.getSub(50);

		List<PrimaryDocument> newFilterList;

		try {
			subProgress.setScale(CollectionUtils.size(codesIterable.iterator()));
			subProgress.start();

			Iterator<? extends Code> codes = codesIterable.iterator();

			if (!codes.hasNext()) {
				newFilterList = Collections.emptyList();
			} else {
				Code c = codes.next();
				String nextSearchString = c.getTag();

				if (nextSearchString.startsWith("^") || nextSearchString.startsWith("-")) {
					newFilterList = new LinkedList<PrimaryDocument>(project.getCodeModel()
						.getPrimaryDocuments());
					newFilterList.removeAll(getSetFromSearchString(c, project));
				} else {
					newFilterList = new LinkedList<PrimaryDocument>(getSetFromSearchString(c,
						project));
				}

				if (subProgress.isCanceled())
					return null;

				subProgress.work(1);

				while (codes.hasNext()) {
					c = codes.next();
					nextSearchString = c.getTag();

					// Remove from hits if starting with - or ^
					if (nextSearchString.startsWith("^") || nextSearchString.startsWith("-")) {
						newFilterList.removeAll(getSetFromSearchString(c, project));
					} else {
						newFilterList.retainAll(getSetFromSearchString(c, project));
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

		Collections.sort(newFilterList);

		pBar.work(25);
		if (pBar.isCanceled())
			return null;

		return newFilterList;
	}
	
	public static String cleanStringForSearch(String s){
		
		s = s.trim();
		
		if (s.startsWith("\"") && s.endsWith("\"")){

			// Remove leading/trailing double quotes
			s = s.substring(1, s.length() - 1);
			
			// Unescape 			
			s = StringEscapeUtils.unescapeJava(s);
		}
		
		return s;
	}

	public Collection<PrimaryDocument> getSetFromSearchString(Code c, Project project) {

		String nextSearchString = c.getTag();

		char operation = '+';

		if (nextSearchString.startsWith("^") || nextSearchString.startsWith("-")) {
			operation = nextSearchString.charAt(0);
			nextSearchString = nextSearchString.substring(1);
		}

		// Search using Lucene if code is "search"
		if ("search".equals(nextSearchString) && c.getValue() != null
			&& c.getValue().trim().length() > 1) {
			
			String searchString;
			
			List<? extends Code> props = c.getProperties();
			
			if (props.size() == 1 && props.get(0).getTag().equals("desc")){
				searchString = cleanStringForSearch(c.getValue());
			} else {
				StringJoiner sb = new StringJoiner(" ");
				for (Code sub : props){
					sb.append(cleanStringForSearch(sub.toString()));
				}
				searchString = sb.toString();
			}

			searchStringProxy.setVariable(searchString);
			
			Iterator<PrimaryDocument> it = luceneFacade.search(project, searchString);
			LinkedList<PrimaryDocument> result = new LinkedList<PrimaryDocument>();
			CollectionUtils.addAll(result, it);

			return result;
		} else {
			if ("*".equals(nextSearchString)) {

				Set<PrimaryDocument> result = new HashSet<PrimaryDocument>();
				for (PrimaryDocument doc : project.getCodeModel().getPrimaryDocuments()) {
					if (doc.getParent() != null)
						result.add(doc);
				}
				return result;
			} else {
				List<PrimaryDocument> results = project.getCodeModel().getPrimaryDocuments(
					nextSearchString);

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
