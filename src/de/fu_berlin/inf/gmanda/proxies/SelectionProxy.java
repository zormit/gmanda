package de.fu_berlin.inf.gmanda.proxies;

import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.VariableProxy;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

public class SelectionProxy extends VariableProxy<Object> {
	
	@Override
	public boolean setVariable(Object variable) {
		if (variable != getVariable())
			return super.setVariable(variable);
		else 
			return true;
	}
	
	public SelectionProxy(ProjectProxy p){
		super(null);
		
		// Reset the selection if the project changes
		p.add(new VariableProxyListener<Project>(){
			Project previousProject = null;
			
			public void setVariable(Project newValue) {
				if (newValue != previousProject || newValue == null){
					SelectionProxy.this.setVariable(null);
				}
				previousProject = newValue;
			}
		});
	}
}
