package de.fu_berlin.inf.gmanda.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;

public class CUtils {
	
	public static <T> LinkedList<T> first(Iterable<T> it, int max){
		
		LinkedList<T> result = new LinkedList<T>();
		
		int i = 0;
		for (T t : it){
			if (i >= max)
				return result;
			result.add(t);
			i++;
		}
		return result;
	}
	
	public static String getStackTrace(Throwable t){
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}