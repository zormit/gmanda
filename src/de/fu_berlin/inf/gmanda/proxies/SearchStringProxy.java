package de.fu_berlin.inf.gmanda.proxies;

import de.fu_berlin.inf.gmanda.util.VariableProxy;

/**
 * The current string used for searching.
 * 
 * Will be null if no search is active.
 */
public class SearchStringProxy extends VariableProxy<String> {
	public SearchStringProxy() {
		super("");
	}
}
