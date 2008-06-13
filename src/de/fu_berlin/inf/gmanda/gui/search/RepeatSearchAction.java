/**
 * 
 */
package de.fu_berlin.inf.gmanda.gui.search;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.fu_berlin.inf.gmanda.gui.PrimaryDocumentTree;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;

public class RepeatSearchAction extends AbstractAction {

	protected SearchStringProxy string;
	protected SearchField searchField;
	protected PrimaryDocumentTree tree;

	public RepeatSearchAction(SearchField searchField, SearchStringProxy string, PrimaryDocumentTree tree) {
		this.string = string;
		this.searchField = searchField;
		this.tree = tree;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (!searchField.hasFocus()) {
			searchField.requestFocusInWindow();
		}
	}
}