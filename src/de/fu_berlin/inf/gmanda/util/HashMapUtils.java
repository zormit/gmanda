package de.fu_berlin.inf.gmanda.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HashMapUtils {

	public static HashMap<String, String> toHashMap(Properties properties) {
		HashMap<String, String> metadata = new HashMap<String, String>();
		if (properties != null)
			for (Map.Entry<Object, Object> s : properties.entrySet()) {
				metadata.put((String) s.getKey(), (String) s.getValue());
			}
		return metadata;
	}
	
	public static Properties toProperties(Map<String, String> source){
		Properties result = new Properties();
		if (source != null)
			result.putAll(source);
		return result;
	}
	
	public  static <T,S> void putList(Map<T, List<S>> map, T key, S value){
		putList(map, key, value, false); 
	}
	
	public  static <T,S> void putList(Map<T, List<S>> map, T key, S value, boolean duplicateCheck){
		if (map.containsKey(key)){
			if (value != null){
				List<S> list = map.get(key);
				if (!(duplicateCheck && list.contains(value)))
					list.add(value);
			}
		} else {
			List<S> list = new LinkedList<S>();
			if (value != null)
				list.add(value);
			map.put(key, list);
		}
	}

}
