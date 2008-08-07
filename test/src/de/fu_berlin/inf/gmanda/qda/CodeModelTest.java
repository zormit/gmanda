package de.fu_berlin.inf.gmanda.qda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.jvyaml.YAML;

import de.fu_berlin.inf.gmanda.gui.manager.CommonService;
import de.fu_berlin.inf.gmanda.imports.MyXStream;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.StringUtils;

public class CodeModelTest extends TestCase {

	public void testYAML() throws IOException {

		ProjectLoader pl = new ProjectLoader(new MyXStream(), new CommonService(new ForegroundWindowProxy()));
		
		Project p = pl.load(new File("test/data/testing.gmp"), null);//"D:\\\\svn\\\\sci\\\\ChrisDiss\\\\qda\\\\innovation.gmp"), null);		
		
		BufferedReader bf = new BufferedReader(new FileReader("test.tmp"));
		
		String s;
		StringBuilder sb = new StringBuilder();
		while ((s = bf.readLine())!= null){
			sb.append(s).append("\n");
		}
		// CodedString cs = CodedFactory.toCoded(sb.toString());
		
		
		
		for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(p.getPrimaryDocuments())){
		
			sb = new StringBuilder();
			
			CodedString cs = CodedStringFactory.parse(pd.getCode());
			
			for (Code c : cs.getAllCodes()){
				sb.append(c.getCode());
			
				if (c.hasValue()){
					sb.append(" : { ");
					
					String value = c.getValue().trim();
					
					if (value.startsWith("\""))
						value = value.substring(1);
					
					if (value.endsWith("\""))
						value = value.substring(0, value.length() - 1);
					
					String[] aaa = value.split(":\\s+");
					
					int i = 0;
					 
					Set<String> allowed = new HashSet<String>();
					
					allowed.add("memo");
					allowed.add("date");
					allowed.add("desc");
					allowed.add("summary");
					allowed.add("milestone");
					allowed.add("def");
					allowed.add("value");
					allowed.add("vdef");
					allowed.add("quote");
					allowed.add("title");
					allowed.add("ref");
					allowed.add("cause");
					
					List<String> pairs = new LinkedList<String>();
					
					String lastKey = "desc";
					StringBuilder currentValue = new StringBuilder();
					
					while (i < aaa.length){
						String key = aaa[i].trim();
						
						String[] b = key.split("\\s");
						key = b[b.length - 1];
							
						if (allowed.contains(key)){
							
							if (currentValue.length() > 0){
								pairs.add(lastKey + ": \"" + currentValue.toString() + "\"");
								currentValue = new StringBuilder();
							}
							lastKey = key;
							
						} else {
							currentValue.append(aaa[i]);
						}
						i++;
					}
					
					if (currentValue.length() > 0){
						pairs.add(lastKey + ": \"" + currentValue.toString() + "\"");
					}
					
					sb.append(StringUtils.join(pairs, ", "));
					
					sb.append(" } ");
				} 
				sb.append("\n");
			}
			
			String result = sb.toString().replaceAll("\n{2,}", "\n\n");
			
			System.out.println(result);
//			
			Object o = YAML.load(result);
			
			System.out.println(YAML.dump(o));
		}

	}

	public void testRename() {
		{
			CodedString c = CodedStringFactory.parse("offtest.offtest=Hello, offtest.bla=Bye");
			c.rename("offtest", "ontest");
			assertEquals("ontest.ontest=Hello, ontest.bla=Bye", c.toString());
		}
		{
			CodedString c = CodedStringFactory.parse("offtest.offtest=Hello, offtest.bla=Bye");
			c.rename("offtest.offtest", "ontest");
			assertEquals("ontest=Hello, offtest.bla=Bye", c.toString());
		}
		{
			CodedString c = CodedStringFactory.parse(
				"offtest.offtest.offtest2=Hello, offtest.offtest.bla=Bye");
			c.rename("offtest.offtest.offtest2", "ontest");
			assertEquals("ontest=Hello, offtest.offtest.bla=Bye", c.toString());
		}
	}

	public void testWhiteSpace() {
		{
			CodedString c = CodedStringFactory.parse(
				"     offtest.offtest=\"Hello     \"    ,\n \n     offtest.bla=Bye     ");
			c.rename("offtest", "ontest");
			assertEquals("     ontest.ontest=\"Hello     \"    ,\n \n     ontest.bla=Bye     ", c
				.toString());
		}
	}

	public void testEscape() {
		CodedString c = CodedStringFactory.parse("\"hello,world\"");
		assertEquals(1, c.getAllVariations().size());
		CodedString c2 = CodedStringFactory.parse("hello,world");
		assertEquals(2, c2.getAllVariations().size());
		CodedString c3 = CodedStringFactory.parse("\"hello,world\",bye");
		assertEquals(2, c3.getAllVariations().size());
		CodedString c4 = CodedStringFactory.parse(
			"\"hello,world\",bye;\"oh my, god; could, this\" actually work \",\"");
		assertEquals(3, c4.getAllVariations().size());
		{
			// Error cases
			CodedString c5 = CodedStringFactory.parse(
				"\"hello,world\",bye;\"oh my, god; could, this\" actually work \",");
			assertEquals(3, c5.getAllVariations().size());
		}

	}

}
