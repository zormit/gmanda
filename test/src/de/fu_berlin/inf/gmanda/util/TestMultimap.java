package de.fu_berlin.inf.gmanda.util;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class TestMultimap extends TestCase{
	
	public void testMultimap(){
		
		Multimap<String, String> test = new HashMultimap<String, String>();
		
		test.put("hallo", "1");
		test.put("hallo", "2");
		
		assertEquals(2, test.get("hallo").size());
		
		List<String> s = Collections.emptyList();
		test.putAll("bye", s);
		
		assertFalse(test.containsKey("bye"));
		
	}

}
