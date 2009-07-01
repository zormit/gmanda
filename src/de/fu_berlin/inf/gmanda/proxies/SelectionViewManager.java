package de.fu_berlin.inf.gmanda.proxies;

import org.apache.commons.lang.ObjectUtils;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTreeFilterTextField.FilterKind;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.glazeddata.ObservableList;

/**
 * The SelectionViewManager is in charge of keeping a list of Primary Documents that
 * the user looked at, so that the user can go back and forth easily.
 * 
 * Each selected documented is stored with the filter text and filter kind. 
 */
public class SelectionViewManager {
	
	public class View {
		Object selection;
		Filter filter;
		FilterKind filterKind;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((filter == null) ? 0 : filter.hashCode());
			result = prime * result
					+ ((filterKind == null) ? 0 : filterKind.hashCode());
			result = prime * result
					+ ((selection == null) ? 0 : selection.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			View other = (View) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (filter == null) {
				if (other.filter != null)
					return false;
			} else if (!filter.equals(other.filter))
				return false;
			if (filterKind == null) {
				if (other.filterKind != null)
					return false;
			} else if (!filterKind.equals(other.filterKind))
				return false;
			if (selection == null) {
				if (other.selection != null)
					return false;
			} else if (!selection.equals(other.selection))
				return false;
			return true;
		}

		private SelectionViewManager getOuterType() {
			return SelectionViewManager.this;
		}
	}
	
	public ObservableList<View> previousViews = new ObservableList<View>();
	
	View current;
	
	public ObservableList<View> nextViews = new ObservableList<View>();
	
	SelectionProxy selection;
	FilterProxy filter;
	FilterTextProxy filterText;
	FilterKindProxy filterKind;
	
	public SelectionViewManager(ProjectProxy project, 
		SelectionProxy selection, 
		FilterProxy filter, 
		FilterTextProxy filterText,
		FilterKindProxy filterKind){

		this.selection = selection;
		this.filter = filter;
		this.filterKind = filterKind;
		this.filterText = filterText;
		
		project.add(new VariableProxyListener<Project>(){
			Project oldValue = null;
			public void setVariable(Project newValue) {
				if (!ObjectUtils.equals(newValue, oldValue)){
					oldValue = newValue;
					nextViews.clear();
					previousViews.clear();
					current = null;
				}
			}
		});

		VariableProxyListener<Object> change = new VariableProxyListener<Object>(){

			public void setVariable(Object newValue) {
				updateView();
			}
		};
		
		filter.add(change);
		selection.add(change);
		filterKind.add(change);
	}
	protected void updateView() {
		 
		View newView = new View();
		newView.selection = selection.getVariable();
		newView.filter = filter.getVariable();
		newView.filterKind = filterKind.getVariable();
		
		if (current != null && receiving && !current.equals(newView)){
			previousViews.add(current);
		}
		current = newView;
	}
	
	boolean receiving = true;
	
	public static String getFilterText(View view){
		if (view == null)
			return null;
		if (view.filter == null)
			return null;
		return view.filter.filterText;
	}
	
	/**
	 * Moves backwards in the history until the next email is found that has a different filter string
	 */
	public void rewind(){

		String filterText = getFilterText(current);
		
		while (previousViews.size() > 0 && ObjectUtils.equals(filterText, getFilterText(current))){
			back();
		}
	}
	
	/**
	 * Moves to the previous view in the history
	 * 
	 * For instance if the user visited A, B, C and is currently at D, then calling back() will now show C.
	 * 
	 */
	public void back(){
		
		if (previousViews.size() == 0)
			return;
		
		View previous = previousViews.remove(previousViews.size() - 1);
		nextViews.add(current);
		
		setView(previous);
		
	}
	
	public void forward(){
		
		if (nextViews.size() == 0)
			return;
		
		View next = nextViews.remove(nextViews.size() - 1);
		previousViews.add(current);
		
		setView(next);
	}
	
	public void setView(View toSet) {
		receiving = false;
		
		if (toSet.filter != null)
			toSet.filter.fresh = false;
		
		if (!ObjectUtils.equals(toSet.filter, filter.getVariable())){
			filter.setVariable(toSet.filter);
		}
		
		if (!ObjectUtils.equals(toSet.filterKind, filterKind.getVariable())){
			filterKind.setVariable(toSet.filterKind);
		}
		
		if (!ObjectUtils.equals(toSet.selection, selection.getVariable())){
			selection.setVariable(toSet.selection);
		}
		
		receiving = true;
	}
}
