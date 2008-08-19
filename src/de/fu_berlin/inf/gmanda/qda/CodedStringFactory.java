package de.fu_berlin.inf.gmanda.qda;

import java.util.Iterator;

public class CodedStringFactory {
	
	static TagxON t = new TagxON();
	
	public static CodedString parse(String s){
		return t.parse(s);
	}

	public static Code parseOne(String s) {
		
		CodedString c = t.parse(s);
		
		Iterator<? extends Code> it = c.getAllCodes().iterator();
		
		if (it.hasNext())
			return it.next();
		else 
			return null;
	}

}
