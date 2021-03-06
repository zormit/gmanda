package de.fu_berlin.inf.gmanda.util;


public class StringJoiner implements Appendable, CharSequence {

	String delimiter;
	
	public StringJoiner(String delimiter){
		this.delimiter = delimiter;
	}
	
	boolean tail = false;
	
	StringBuilder sb = new StringBuilder();
	
	public String toString(){
		return sb.toString();
	}
	
	public StringJoiner appendNoJoin(CharSequence arg0){
		sb.append(arg0);
		return this;
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

	public char charAt(int index) {
		return sb.charAt(index);
	}

	public int length() {
		return sb.length();
	}

	public CharSequence subSequence(int start, int end) {
		return sb.subSequence(start, end);
	}
}
