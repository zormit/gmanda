package de.fu_berlin.inf.gmanda.qda;

import junit.framework.TestCase;

public class TagxOnTest extends TestCase {

	public void testParsing() {

		TagxON o = new TagxON();

		assertEquals("[hello : { world : how are you }]", o
			.parse("hello : { world : how are you }").toString());

		assertEquals("[hello : { world : how are you }, nice]", o.parse(
			"hello : { world : how are you }, nice").toString());

		assertEquals("[hello : { world : how are you }, nice, to : know { desc : you }]", o.parse(
			"hello : { world : how are you }, nice, to : know { desc : you }").toString());

		assertEquals("[hello : { world : how are you }, nice, to : know { desc : \" to know you so : } { well you\" }]", o.parse(
		"hello : { world : how are you }, nice, to : know { desc : \" to know you so : } { well you\" }").toString());

		assertEquals("[hello : { world : \" to know you so \\\": } { well you\" }]", o.parse(
		"hello : { world : \" to know you so \\\": } { well you\" }").toString());

		assertEquals("[hello : world]", o.parse(
		"hello ::: world }}} :: :,, , :: {").toString());

		
		
	}

}
