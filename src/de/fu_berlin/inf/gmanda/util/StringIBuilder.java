package de.fu_berlin.inf.gmanda.util;

public class StringIBuilder {

	public StringBuilder sb = new StringBuilder();

	public int i = 0;

	public StringIBuilder indent(int plus) {
		i = i + plus;
		if (i > 100)
			i = 100;
		
		return this;
	}

	public StringIBuilder unindent(int minus) {
		i = i - minus;
		if (i < 0)
			i = 0;
		
		return this;
	}

	String spaces = "                                                                                                    \n";

	public StringIBuilder append(String s) {
		if (i > 0 && s != null)
			sb.append(s.replace("\n", spaces.substring(100 - i, 101)));
		else
			sb.append(s);

		return this;
	}
	
	public StringIBuilder append(char c){
		sb.append(c);
		return this;
	}
	
	public String toString(){
		return sb.toString();
	}
}
