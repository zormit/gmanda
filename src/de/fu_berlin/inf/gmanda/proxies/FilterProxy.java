package de.fu_berlin.inf.gmanda.proxies;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ObjectUtils;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.gui.search.SearchService;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.DeferredVariableProxyListener;
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

	SearchService searchService;

	CommonService progress;
	
	Project project = null;
	
	List<PrimaryDocument> rootFilterList;

	public FilterProxy(FilterTextProxy filterTextProxy, final ProjectProxy projectProxy,
		final SearchService searchService, final CommonService progress) {
		super(null);

		this.searchService = searchService;
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

		filterTextProxy.add(DeferredVariableProxyListener.defer(new VariableProxyListener<String>() {
			public void setVariable(String newValue) {
				updateFilter(newValue);
			}
		}, 1, TimeUnit.SECONDS));
	}

	public void updateFilter(final String filterString) {

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

			rootFilterList = searchService.filter(filterString, pBar, project);

			Filter filter = new Filter();
			filter.filterResult = Collections.unmodifiableList(rootFilterList);
			filter.fresh = true;
			filter.filterText = filterString;
			
			FilterProxy.this.setVariable(filter);
		} finally {
			pBar.done();
		}

	}

}