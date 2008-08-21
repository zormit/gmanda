package de.fu_berlin.inf.gmanda.qda;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.fu_berlin.inf.gmanda.qda.TagxON.TagxONCodedString;

public class TagxOnTest {

	public static String parseAndJoin(String s) {

		TagxON o = new TagxON();

		return StringUtils.join(o.parse(s).tags, ", ");
	}

	@Test
	public void testParsing() {

		assertEquals("hello: {world: how are you}", parseAndJoin("hello : { world : how are you }"));

		// Parse no value
		assertEquals("hello: {world: how are you}, nice",
			parseAndJoin("hello : { world : how are you }, nice"));

		// Parse value and composite
		assertEquals("hello: {world: how are you}, nice, to: {desc: know, desc: you}",
			parseAndJoin("hello : { world : how are you }, nice, to : know { desc : you }"));

		// Escaping
		assertEquals(
			"hello: {world: how are you}, nice, to: {desc: know, desc: \" to know you so : } { well you\"}",
			parseAndJoin("hello : { world : how are you }, nice, to : know { desc : \" to know you so : } { well you\" }"));

		// Escape the escape
		assertEquals("hello: {world: \" to know you so \\\": } { well you\"}",
			parseAndJoin("hello : { world : \" to know you so \\\": } { well you\" }"));

		// Be robust against spurious symbols
		assertEquals("hello: world", parseAndJoin("hello ::: world }}} :: :,, , :: {"));

		// Missing colon
		assertEquals(
			"activity.propose#intention: {value: \"thoughts\", value: \"volunteers\"}, argumentation.humor",
			parseAndJoin("activity.propose#intention { value: \"thoughts\", value: \"volunteers\"}, argumentation.humor"));

		// Additional comma
		assertEquals(
			"activity.propose#intention: {value: \"thoughts\", value: \"volunteers\"}, argumentation.humor",
			parseAndJoin("activity.propose#intention { value: \"thoughts\", value: \"volunteers\",}, argumentation.humor"));

		// Missing comma after brace
		assertEquals(
			"activity.propose#intention: {value: \"thoughts\", value: \"volunteers\"}, argumentation.humor",
			parseAndJoin("activity.propose#intention { value: \"thoughts\", value: \"volunteers\"} argumentation.humor"));

		
		
	}

	@Test
	public void testFormat() {
		String s = "activity.announce: {def:\"The moment of the official announcement of a innovation is a central an\n"
			+ "dedicated moment in the introduction of the innovation. This email lead to\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "this idea because of the sentence 'Officially announce the release cycle\n"
			+ "thingy'.\"\n," + "quote:      \"hello\"," + "why    : \"does\"}";

		TagxONCodedString c = new TagxON().parse(s);

		assertEquals("activity.announce: {\n" + "  def:\n"
			+ "    \"The moment of the official announcement of a innovation is a central an\n"
			+ "    dedicated moment in the introduction of the innovation. This email lead to\n"
			+ "\n"
			+ "    this idea because of the sentence 'Officially announce the release cycle\n"
			+ "    thingy'.\",\n" + "  quote: \"hello\",\n" + "  why: \"does\"\n" + "}", c.format());

	}
}