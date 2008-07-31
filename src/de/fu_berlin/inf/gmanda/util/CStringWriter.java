package de.fu_berlin.inf.gmanda.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.io.output.NullOutputStream;

public class CStringWriter implements CStringBuilder {
	
	PrintWriter writer;
	
	String filename;
	
	public PrintWriter getWriter(){
		if (writer == null){
			if (filename != null){
				try {
					return writer = new PrintWriter(filename);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			writer = new PrintWriter(new NullOutputStream());
		}
		
		return writer;
	}
	
	public CStringWriter(String filename){
		this.filename = filename;
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
		if (indent > 0){
			string = emptyString(indent) + string.replaceAll("\n", "\n" + emptyString(indent));
		}
		getWriter().println(string);
	}
	
	public void flush(){
		// Do not create writer unnecessary
		if (writer != null)
			writer.flush();
	}
	
	public void close(){
		// Do not create writer unnecessary
		if (writer != null)
			writer.close();
	}
	
}
