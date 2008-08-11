package de.fu_berlin.inf.gmanda.proxies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.search.LuceneFacade;
import de.fu_berlin.inf.gmanda.qda.Code;
import de.fu_berlin.inf.gmanda.qda.CodedStringFactory;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;

/**
 * Manages the list of primary documents by which to filter in the
 * PrimaryDocumentTree.
 * 
 * To disable the filtering set this variable proxy to null.
 */
public class FilterProxy extends VariableProxy<Filter> {

	Project project = null;

	LuceneFacade luceneFacade;

	List<PrimaryDocument> rootFilterList;

	CommonService progress;

	public FilterProxy(FilterTextProxy filterTextProxy, final ProjectProxy projectProxy,
		final LuceneFacade lucene, final CommonService progress) {
		super(null);

		this.luceneFacade = lucene;
		this.progress = progress;

		// Reset if the project changes
		projectProxy.add(new VariableProxyListener<Project>() {

			public void setVariable(Project newValue) {
				if (newValue != project || newValue == null) {
					FilterProxy.this.setVariable(null);
				}
				project = newValue;
			}
		});

		filterTextProxy.add(new VariableProxyListener<String>() {
			public void setVariable(String newValue) {
				updateFilter(newValue);
			}
		});
	}

	ScheduledExecutorService executor;

	ScheduledFuture<?> setFilterFuture;

	public Collection<PrimaryDocument> getSetFromSearchString(Code c) {

		String nextSearchString = c.getTag();

		char operation = '+';
		
		if (nextSearchString.startsWith("^") || nextSearchString.startsWith("-")){
			operation = nextSearchString.charAt(0);
			nextSearchString = nextSearchString.substring(1);
		}

		// Search using Lucene if code is "search"
		if ("search".equals(nextSearchString) && c.getValue() != null
			&& c.getValue().trim().length() > 1) {

			String searchString = c.getValue().trim();
			if (searchString.startsWith("\""))
				searchString = searchString.substring(1);
			if (searchString.endsWith("\""))
				searchString = searchString.substring(0, searchString.length() - 1);

			Iterator<PrimaryDocument> it = luceneFacade.search(project, searchString);
			LinkedList<PrimaryDocument> result = new LinkedList<PrimaryDocument>();
			CollectionUtils.addAll(result, it);

			return result;
		} else {
			if ("*".equals(nextSearchString)) {
				
				Set<PrimaryDocument> result = new HashSet<PrimaryDocument>();
				for (PrimaryDocument doc : project.getCodeModel().getPrimaryDocuments()){
					if (doc.getParent() != null)
						result.add(doc);
				}
				return result;
			} else {
				List<PrimaryDocument> results = project.getCodeModel().getPrimaryDocuments(nextSearchString); 
				
				switch (operation){
				case '-':
					// Only return pds in which all segments include the search string.
					
					if (!nextSearchString.endsWith(".*"))
						nextSearchString = nextSearchString + ".*";
					
					List<PrimaryDocument> filteredResults = new ArrayList<PrimaryDocument>();
					for (PrimaryDocument result : results){
						if (CodedStringFactory.parse(result.getCode()).containsAny(nextSearchString))
							filteredResults.add(result);
					}
					return filteredResults;
					
				default: 
					return results;
				}
			}
		}
	}

	public void updateFilter(final String filterString) {

		// Lazyly create Thread-Pool Executor
		if (executor == null)
			executor = Executors.newSingleThreadScheduledExecutor();

		// Cancel previous filter operations
		if (setFilterFuture != null && !setFilterFuture.isCancelled())
			setFilterFuture.cancel(false);

		setFilterFuture = executor.schedule(new Runnable() {
			public void run() {

				try {
					IProgress pBar = progress.getProgressBar("Filtering", 100);
					try {

						if (project == null)
							return;

						if (filterString == null || filterString.trim().length() == 0) {
							FilterProxy.this.setVariable(null);
							return;
						}
						
						if (FilterProxy.this.getVariable() != null && 
							ObjectUtils.equals(FilterProxy.this.getVariable().filterText, filterString)){
							return;
						}

						Iterable<? extends Code> codesIterable = CodedStringFactory.parse(filterString).getAllCodes();

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

								if (nextSearchString.startsWith("^")
									|| nextSearchString.startsWith("-")) {
									newFilterList = new LinkedList<PrimaryDocument>(project
										.getCodeModel().getPrimaryDocuments());
									newFilterList.removeAll(getSetFromSearchString(c));
								} else {
									newFilterList = new LinkedList<PrimaryDocument>(
										getSetFromSearchString(c));
								}

								if (subProgress.isCanceled())
									return;
								subProgress.work(1);
								
								while (codes.hasNext()) {
									c = codes.next();
									nextSearchString = c.getTag();

									// Remove from hits if starting with - or ^
									if (nextSearchString.startsWith("^")
										|| nextSearchString.startsWith("-")) {
										newFilterList.removeAll(getSetFromSearchString(c));
									} else {
										newFilterList.retainAll(getSetFromSearchString(c));
									}

									if (subProgress.isCanceled())
										return;
									subProgress.work(1);
								}
							}
						} finally {
							subProgress.done();
						}

						if (pBar.isCanceled())
							return;

						Collections.sort(newFilterList);

						pBar.work(25);
						if (pBar.isCanceled())
							return;

						rootFilterList = newFilterList;

						Filter filter = new Filter();
						filter.filterResult = Collections.unmodifiableList(rootFilterList);
						filter.fresh = true;
						filter.filterText = filterString;
						
						FilterProxy.this.setVariable(filter);
					} finally {
						pBar.done();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
}
