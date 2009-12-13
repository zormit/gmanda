package de.fu_berlin.inf.gmanda.util;

import junit.framework.TestCase;

public class VelocityWhitespaceRepairTest extends TestCase {

	public void testVelocity1(){
		
		String in = 
			"  #foreach()\n" +
				"    Hello\n" +
				"  #end\n" +
				"Bye";
		
		String expected = "#foreach()\n" +
		"  Hello\n" +
		"#end\n" +
		"Bye";
 
		assertEquals(expected, new VelocityWhitespaceRepair().fixWhitespace(in));
		
	}
	
	public void testVelocity2(){
		
		String in = 
				"  #foreach()\n" +
				"    Greetings:\n" +
				"    #foreach()\n" +
				"      Hello\n" +
				"    #end\n" +
				"  #end\n" +
				"Bye";
		
		String expected = 
			"#foreach()\n" +
			"  Greetings:\n" +
			"#foreach()\n" +
			"  Hello\n" +
			"#end\n" +
			"#end\n" +
			"Bye";
 
		assertEquals(expected, new VelocityWhitespaceRepair().fixWhitespace(in));
	}
	
	public void testVelocity3(){
		
		String in = 
				"  #foreach()\n" +
				"    Greetings:\n" +
				"      #foreach()\n" +
				"        Hello\n" +
				"      #end\n" +
				"  #end\n" +
				"Bye";
		
		String expected = 
			"#foreach()\n" +
			"  Greetings:\n" +
			"#foreach()\n" +
			"    Hello\n" +
			"#end\n" +
			"#end\n" +
			"Bye";
 
		assertEquals(expected, new VelocityWhitespaceRepair().fixWhitespace(in));
	}
	
	public void testVelocity4(){
		
		String in = 
				"  #foreach()\n" +
				"    Greetings:\n" +
				"      #foreach()\n" +
				"        #myMacro()\n" +
				"      #end\n" +
				"  #end\n" +
				"Bye";
		
		String expected = 
			"#foreach()\n" +
			"  Greetings:\n" +
			"#foreach()\n" +
			"    #myMacro()\n" +
			"#end\n" +
			"#end\n" +
			"Bye";
 
		assertEquals(expected, new VelocityWhitespaceRepair().fixWhitespace(in));
	}
}
