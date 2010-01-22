package de.fu_berlin.inf.gmanda.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class TestMultimap {
	
	@Test
	public void testMultimap(){
		
		Multimap<String, String> test = HashMultimap.create();
		
		test.put("hallo", "1");
		test.put("hallo", "2");
		
		assertEquals(2, test.get("hallo").size());
		
		List<String> s = Collections.emptyList();
		test.putAll("bye", s);
		
		assertFalse(test.containsKey("bye"));
		
	}

}
