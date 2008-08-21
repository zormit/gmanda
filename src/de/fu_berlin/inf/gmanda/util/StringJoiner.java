package de.fu_berlin.inf.gmanda.util;


public class StringJoiner implements Appendable {

	String delimiter;
	
	public StringJoiner(String delimiter){
		this.delimiter = delimiter;
	}
	
	boolean tail = false;
	
	StringBuilder sb = new StringBuilder();
	
	public String toString(){
		return sb.toString();
	}
	
	public StringJoiner append(CharSequence arg0) {
		if (tail){
			sb.append(delimiter);
			sb.append(arg0);
		} else {
			sb.append(arg0);
			tail = true;
		}
		
		return this;
	}

	public StringJoiner append(char arg0) {
		if (tail){
			sb.append(delimiter);
			sb.append(arg0);
		} else {
			sb.append(arg0);
			tail = true;
		}
		
		return this;
	}

	public StringJoiner append(CharSequence arg0, int arg1, int arg2) {
		if (tail){
			sb.append(delimiter);
			sb.append(arg0);
		} else {
			sb.append(arg0);
			tail = true;
		}
		return this;
	}
	
	
	

}
