package de.fu_berlin.inf.gmanda.qda;

import junit.framework.TestCase;

public class CodeModelTest extends TestCase {

	public void testRename() {
		{
			CodedString c = new CodedString("offtest.offtest=Hello, offtest.bla=Bye");
			c.rename("offtest", "ontest");
			assertEquals("ontest.ontest=Hello, ontest.bla=Bye", c.toString());
		}
		{
			CodedString c = new CodedString("offtest.offtest=Hello, offtest.bla=Bye");
			c.rename("offtest.offtest", "ontest");
			assertEquals("ontest=Hello, offtest.bla=Bye", c.toString());
		}
		{
			CodedString c = new CodedString(
				"offtest.offtest.offtest2=Hello, offtest.offtest.bla=Bye");
			c.rename("offtest.offtest.offtest2", "ontest");
			assertEquals("ontest=Hello, offtest.offtest.bla=Bye", c.toString());
		}
	}

	public void testWhiteSpace() {
		{
			CodedString c = new CodedString("     offtest.offtest=\"Hello     \"    ,\n \n     offtest.bla=Bye     ");
			c.rename("offtest", "ontest");
			assertEquals("     ontest.ontest=\"Hello     \"    ,\n \n     ontest.bla=Bye     ", c.toString());
		}
	}

	public void testEscape() {
		CodedString c = new CodedString("\"hello,world\"");
		assertEquals(1, c.getAllVariations().size());
		CodedString c2 = new CodedString("hello,world");
		assertEquals(2, c2.getAllVariations().size());
		CodedString c3 = new CodedString("\"hello,world\",bye");
		assertEquals(2, c3.getAllVariations().size());
		CodedString c4 = new CodedString(
			"\"hello,world\",bye;\"oh my, god; could, this\" actually work \",\"");
		assertEquals(3, c4.getAllVariations().size());
		{
			// Error cases
			CodedString c5 = new CodedString(
				"\"hello,world\",bye;\"oh my, god; could, this\" actually work \",");
			assertEquals(3, c5.getAllVariations().size());
		}

	}

}
