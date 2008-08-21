package de.fu_berlin.inf.gmanda.util.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TreeWalkerTest {

	public class Node {
		List<Node> children = new LinkedList<Node>();
		
		String s;
		
		public Node(Node parent, String s){
			this.s = s;
			if (parent != null)
				parent.children.add(this);
		}
		
		public String toString(){
			return s;
		}
	}
	
	Node root;
	
	@SuppressWarnings("unused")
	@Before
	public void setUp(){
		root = new Node(null, "1");
		
		Node child11  = new Node(root, "2");
		Node child111 = new Node(child11, "3");
		Node child112 = new Node(child11, "4");
		
		Node child12 = new Node(root, "5");
		Node child121 = new Node(child12, "6");
		Node child122 = new Node(child12, "7");
		
	}
	
	@Test
	public void testIterator() {
		
		int i = 0;
		
		for (Node n : new TreeWalker<Node>(root, new TreeMaker<Node>(){
			public TreeStructure<Node> toStructure(final Node t) {
				return new TreeStructure<Node>(){

					public Node get() {
						return t;
					}

					public Collection<Node> getChildren() {
						return t.children;
					}
					
				};
			}
		})){
			i++;
			assertEquals(String.valueOf(i), n.toString());
		};
		
		assertEquals(7, i);
	}

	@Ignore
	@Test
	public void testVisitCollectionOfTTreeVisitorOfTTreeMakerOfT() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testVisitTTreeVisitorOfTTreeMakerOfT() {
		fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	public void testFindCollectionOfTTreeAcceptorOfTTreeMakerOfT() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testFindTTreeAcceptorOfTTreeMakerOfT() {
		fail("Not yet implemented");
	}

}
