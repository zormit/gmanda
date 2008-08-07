package de.fu_berlin.inf.gmanda.util;

import junit.framework.TestCase;

public class StringUtilsTest extends TestCase {
	
	public void testSkipAhead(){
		
		assertEquals("|hello world|", StringUtils.skipAhead('|', '-', "hello |hello world| world".toCharArray(), 6));
		
		assertEquals("|hello world world", StringUtils.skipAhead('|', '-', "hello |hello world world".toCharArray(), 6));
		
		assertEquals("|hello-| world|", StringUtils.skipAhead('|', '-', "hello |hello-| world| world".toCharArray(), 6));
	}

}
