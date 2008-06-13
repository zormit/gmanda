package de.fu_berlin.inf.gmanda.gui.search;

import java.awt.Dimension;

import javax.swing.JTextField;

import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.proxies.SearchStringProxy;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;
import de.fu_berlin.inf.gmanda.util.gui.ProxyTextComponentBridge;

public class SearchField extends JTextField {

	ProxyTextComponentBridge bridge;
	
	public SearchField(SearchStringProxy searchString, ProjectProxy projectProxy) {

		setPreferredSize(new Dimension(100, 20));

		bridge = new ProxyTextComponentBridge(this, searchString);
		
		projectProxy.add(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				SearchField.this.setEnabled(newValue != null);
			}
		});
	}
}
