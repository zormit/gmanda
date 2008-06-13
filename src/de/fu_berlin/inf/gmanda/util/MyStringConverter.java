package de.fu_berlin.inf.gmanda.util;

import org.apache.commons.lang.StringEscapeUtils;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Escape all tabs, newlines, etc.
 */
public class MyStringConverter extends AbstractSingleValueConverter {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class clazz) {
		return String.class.equals(clazz);
	}

	@Override
	public Object fromString(String arg0) {
		return StringEscapeUtils.unescapeJava(arg0);
	}
	
	@Override
	public String toString(Object o){
		return StringEscapeUtils.escapeJava((String)o);
	}
}
