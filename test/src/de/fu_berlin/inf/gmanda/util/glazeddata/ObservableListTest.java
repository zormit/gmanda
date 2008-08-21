package de.fu_berlin.inf.gmanda.util.glazeddata;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ObservableListTest {

	@Test
	public void testAddT() {
		ObservableList<String> l = new ObservableList<String>();
		
		final List<String> added = new LinkedList<String>();
		final List<String> removed = new LinkedList<String>();
		
		l.addListener(new MinimalListListenerAdapter<String>(new MinimalListListener<String>(){

			public void add(String s) {
				added.add(s);
			}

			public void remove(String s) {
				removed.add(s);
			}
			
		}));
		
		l.add("Hallo");
		l.add("Welt");
		
		l.remove(1);
		l.add(0, "Chaos");
		l.clear();
		
		assertEquals(0, l.size());
		
		assertEquals(3, added.size());
		assertEquals("Hallo", added.get(0));
		assertEquals("Welt", added.get(1));
		assertEquals("Chaos", added.get(2));
		
		assertEquals(3, removed.size());
		assertEquals("Welt", removed.get(0));
		assertEquals("Chaos", removed.get(1));
		assertEquals("Hallo", removed.get(2));
				
	}

}
