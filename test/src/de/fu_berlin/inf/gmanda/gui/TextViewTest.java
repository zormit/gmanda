package de.fu_berlin.inf.gmanda.gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TextViewTest {

	public void assertURL(String url){
		assertEquals("<a href=\"" + url + "\">" + url + "</a>", TextView.toHyperLink(url));
	}
	
	@Test
	public void testURLEscape() {

		assertEquals("<a href=\"http://www.hello.com/\">http://www.hello.com/</a>", TextView
			.toHyperLink("http://www.hello.com/"));

		assertURL("https://bugzilla.mozilla.org/buglist.cgi?short_desc_type=allwordssubstr&short_desc=remove+parameter&product=Bugzilla&resolution=---");
		
		assertEquals("<a href=\"https://ba.mo.org/bust.cgi?st_dc_te=ar&srt_dc=re+pter&prct=Bua&ron=---\">https://ba.mo.org/bust.cgi?st_dc_te=ar&srt_dc=re+pter&prct=Bua&ron=---</a>", TextView
			.toHyperLink("https://ba.mo.org/bust.cgi?st_dc_te=ar&srt_dc=re+pter&prct=Bua&ron=---"));

	}

	@Test
	public void testBoldAndItalic() {

		assertEquals(" hello ", TextView.toBoldAndItalic(" hello "));

		assertEquals(" *<b>hello</b>* ", TextView.toBoldAndItalic(" *hello* "));
		assertEquals(" _<u>hello</u>_ ", TextView.toBoldAndItalic(" _hello_ "));

		assertEquals(" *<b>hello</b>* *<b>hello</b>* ", TextView
			.toBoldAndItalic(" *hello* *hello* "));
		assertEquals(" _<u>hello</u>_ _<u>hello</u>_ ", TextView
			.toBoldAndItalic(" _hello_ _hello_ "));

		assertEquals(" _<u>*<b>hello</b>*</u>_ ", TextView.toBoldAndItalic(" _*hello*_ "));
		assertEquals(" *<b>_<u>hello</u>_</b>* ", TextView.toBoldAndItalic(" *_hello_* "));

		assertEquals(" *<b>h ello</b>* ", TextView.toBoldAndItalic(" *h ello* "));
		assertEquals(" _<u>h ello</u>_ ", TextView.toBoldAndItalic(" _h ello_ "));

		assertEquals(" *<b>h e llo</b>* ", TextView.toBoldAndItalic(" *h e llo* "));
		assertEquals(" _<u>h e llo</u>_ ", TextView.toBoldAndItalic(" _h e llo_ "));

		assertEquals(" *<b>h e l lo</b>* ", TextView.toBoldAndItalic(" *h e l lo* "));
		assertEquals(" _<u>h e l lo</u>_ ", TextView.toBoldAndItalic(" _h e l lo_ "));

		assertEquals(" *<b>h e l l o</b>* ", TextView.toBoldAndItalic(" *h e l l o* "));
		assertEquals(" _<u>h e l l o</u>_ ", TextView.toBoldAndItalic(" _h e l l o_ "));

		assertEquals(" *h e l l o a* ", TextView.toBoldAndItalic(" *h e l l o a* "));
		assertEquals(" _h e l l o a_ ", TextView.toBoldAndItalic(" _h e l l o a_ "));

		// New line handling
		assertEquals(" *hel\nlo* ", TextView.toBoldAndItalic(" *hel\nlo* "));
		assertEquals(" _hel\nlo_ ", TextView.toBoldAndItalic(" _hel\nlo_ "));

		assertEquals(
			" Once *<b>upon</b>* a _<u>time</u>_ in *<b>a</b>* *<b>beautiful</b>* _<u>town</u>_ there was _<u>a</u>_ _<u>little</u>_ _<u>castle</u>_ ",
			TextView
				.toBoldAndItalic(" Once *upon* a _time_ in *a* *beautiful* _town_ there was _a_ _little_ _castle_ "));

		assertEquals("Once *<b>upon</b>*\n" + " a \n" + "_<u>time</u>_ in \n"
			+ "*<b>a</b>* *<b>beautiful</b>* \n" + "_<u>town</u>_\n" + " there was \n"
			+ "_<u>a</u>_\n" + "_<u>little</u>_\n" + "*<b>castle</b>*\n ", TextView
			.toBoldAndItalic("Once *upon*\n" + " a \n" + "_time_ in \n" + "*a* *beautiful* \n"
				+ "_town_\n" + " there was \n" + "_a_\n" + "_little_\n" + "*castle*\n "));
	}

}
