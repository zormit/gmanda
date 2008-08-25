package de.fu_berlin.inf.gmanda.qda;

import java.util.Iterator;

import de.fu_berlin.inf.gmanda.qda.tagxon.TagxON;

public class CodedStringFactory {
	
	static TagxON t = new TagxON();
	
	public static CodedString parse(String s){
		return t.parseCodedString(s);
	}

	public static Code parseOne(String s) {
		
		CodedString c = t.parseCodedString(s);
		
		Iterator<? extends Code> it = c.getAllCodes().iterator();
		
		if (it.hasNext())
			return it.next();
		else 
			return null;
	}

}
