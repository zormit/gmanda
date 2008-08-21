package de.fu_berlin.inf.gmanda.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilsTest {
	
	@Test
	public void testSkipAhead(){
		
		assertEquals("|hello world|", CStringUtils.skipAhead('|', '-', "hello |hello world| world".toCharArray(), 6));
		
		assertEquals("|hello world world", CStringUtils.skipAhead('|', '-', "hello |hello world world".toCharArray(), 6));
		
		assertEquals("|hello-| world|", CStringUtils.skipAhead('|', '-', "hello |hello-| world| world".toCharArray(), 6));
	}
	
	@Test
	public void testWrap(){
		
		assertEquals("hallo", CStringUtils.wrap("hallo", 0, 0, 80));
		
		assertEquals("hallo", CStringUtils.wrap("hallo", 0, 2, 80));
		
		// Simple wrap case
		assertEquals("hallo\n  welt", CStringUtils.wrap("hallo welt", 0, 2, 5));
		
		{// Test collapsing of new lines
		
			// Don't split single wraps
			assertEquals("hallo welt", CStringUtils.wrap("hallo\nwelt", 0, 0, 80));
		
			// Split on double newlines and more
			assertEquals("hallo\n\nwelt\n\nhow", CStringUtils.wrap("hallo\n\nwelt\n\n\nhow", 0, 0, 80));
		
			// Don't forget to indent
			assertEquals("hallo\n\n  welt\n\n  how", CStringUtils.wrap("hallo\n\nwelt\n\n\nhow", 0, 2, 80));
		}
		
		{// Test starting indentation
			assertEquals("hallo welt", CStringUtils.wrap("hallo welt", 2, 4, 12));
			
			assertEquals("hallo\n    welt", CStringUtils.wrap("hallo welt", 2, 4, 11));
		}
		
		{// Failure case 
			assertEquals(
				"\"The moment of the official announcement of a innovation is a central an\n" +
				"    dedicated moment in the introduction of the innovation. This email lead to\n" +
				"\n" +
				"    this idea because of the sentence 'Officially announce the release cycle\n" +
				"    thingy'.\"", CStringUtils.wrap("\"The moment of the official announcement of a innovation is a central an\n" +
						"dedicated moment in the introduction of the innovation. This email lead to\n" +
						"\n" +
						"\n" +
						"\n" +
						"this idea because of the sentence 'Officially announce the release cycle\n" +
						"thingy'.\"", 4, 4, 80));
		}
	}

}
