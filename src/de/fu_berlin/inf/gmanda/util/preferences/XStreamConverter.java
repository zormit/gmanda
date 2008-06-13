/**
 * 
 */
package de.fu_berlin.inf.gmanda.util.preferences;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import de.fu_berlin.inf.gmanda.util.StringUtils.FromConverter;
import de.fu_berlin.inf.gmanda.util.StringUtils.StringConverter;

public class XStreamConverter<T> implements StringConverter<T>, FromConverter<T> {
	
	SingleValueConverter svc;
	
	public XStreamConverter(SingleValueConverter c){
		svc = c;
	}
	
	public String toString(T t){
		return svc.toString(t);
	}
	
	@SuppressWarnings("unchecked")
	public T fromString(String s) {
		return (T)svc.fromString(s);
	}
}