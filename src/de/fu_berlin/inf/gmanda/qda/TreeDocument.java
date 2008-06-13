package de.fu_berlin.inf.gmanda.qda;

import java.util.Collection;

public interface TreeDocument {
	
	public Collection<TreeDocument> getChildren();
	
	public String getText();

}
