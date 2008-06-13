package de.fu_berlin.inf.gmanda.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CStringWriter implements CStringBuilder {
	
	PrintWriter writer;
	
	public CStringWriter(String filename){
		try {
			writer = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			writer = null;
		}
	}
	
	public String empty = "                ";
	
	public class NestedStringBuilder implements CStringBuilder {

		public NestedStringBuilder(CStringBuilder nested, int indent){
			this.currentIndent = indent;
			this.nested = nested;
		}
		CStringBuilder nested;
		int currentIndent;
		
		public CStringBuilder indent(int i) {
			return new NestedStringBuilder(this, i);
		}

		public void writeln(String s) {
			nested.writeln(s, currentIndent);
			
		}

		public void writeln(String s, int indent) {
			nested.writeln(s, currentIndent + indent);
		}
	}
	
	public CStringBuilder indent(int i){
		return new NestedStringBuilder(this, i);
	}
	
	public String emptyString(int i){
		while (empty.length() < i){
			empty = empty + empty;
		}
		return empty.substring(0, i);
	}

	public void writeln(String string){
		writeln(string, 0);
	}
	
	public void writeln(String string, int indent){
		if (writer != null){
			if (indent > 0){
				string = emptyString(indent) + string.replaceAll("\n", "\n" + emptyString(indent));
			}
			writer.println(string);
		}
	}
	
	public void flush(){
		if (writer != null)
			writer.flush();
	}
	
	public void close(){
		if (writer != null)
			writer.close();
	}
	
}
