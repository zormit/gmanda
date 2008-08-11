package de.fu_berlin.inf.gmanda.qda;

public class CodedStringFactory {
	
	static TagxON t = new TagxON();
	
	public static CodedString parse(String s){
		return t.parse(s);
	}

}
