package de.fu_berlin.inf.gmanda.proxies;

import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class FilterTextProxy extends VariableProxy<String> {
	
	public FilterTextProxy(ProjectProxy p){
		super(null);
		
		// Reset if the project changes
		p.add(new VariableProxyListener<Project>(){
			Project previousProject = null;
			
			public void setVariable(Project newValue) {
				if (newValue != previousProject || newValue == null){
					FilterTextProxy.this.setVariable(null);
				}
				previousProject = newValue;
			}
		});
	}
}
