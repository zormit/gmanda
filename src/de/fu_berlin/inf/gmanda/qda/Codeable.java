package de.fu_berlin.inf.gmanda.qda;

import de.fu_berlin.inf.gmanda.util.StateChangeNotifier;

public interface Codeable {
	
	public String getCode();
	
	public void setCode(String newCode);
	
	public StateChangeNotifier<Codeable> getCodeChangeNotifier();

}
