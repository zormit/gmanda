package de.fu_berlin.inf.gmanda.util.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class TreeWalker<T> implements Iterable<T> {
 
	TreeMaker<T> maker;
	Iterable<? extends T> root;

	public TreeWalker(T root, TreeMaker<T> maker) {
		if (root == null || maker == null)
			throw new IllegalArgumentException();

		this.root = Collections.singletonList(root);
		this.maker = maker;
	}

	public TreeWalker(Iterable<? extends T> root, TreeMaker<T> maker) {
		if (root == null || maker == null)
			throw new IllegalArgumentException();

		this.root = root;
		this.maker = maker;
	}

	public class TreeIterator implements Iterator<T> {

		List<Iterator<? extends T>> queue = new LinkedList<Iterator<? extends T>>();

		TreeIterator() {
			queue.add(root.iterator());
		}

		public boolean hasNext() {
			return queue.size() > 0 && queue.get(0).hasNext();
		}

		public T next() {
			if (queue.size() == 0)
				throw new NoSuchElementException();

			Iterator<? extends T> it = queue.get(0);

			T toReturn = it.next();

			// Now reposition iterator
			TreeStructure<? extends T> node = maker.toStructure(toReturn);

			// Add children if any exist
			Iterable<? extends T> children = node.getChildren();
			if (children != null) {
				Iterator<? extends T> nextIt = children.iterator();
				if (nextIt.hasNext()) {
					queue.add(0, nextIt);
					it = nextIt;
				}
			}

			while (!it.hasNext()) {
				if (queue.size() < 2)
					break;

				queue.remove(0);

				it = queue.get(0);
			}
			return toReturn;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public Iterator<T> iterator() {
		return new TreeIterator();
	};

	public int size() {
		int size = 0;
		for (@SuppressWarnings("unused")
		T t : this) {
			size++;
		}
		return size;
	}

	public T find(TreeAcceptor<T> acceptor) {
		for (T t : this) {
			if (acceptor.accept(t))
				return t;
		}
		return null;
	}

	public void visit(TreeVisitor<T> visitor) {
		for (T t : this) {
			visitor.accept(t);
		}
	}

	public static <T> void visit(Iterable<T> list, TreeVisitor<T> visitor, TreeMaker<T> maker) {
		for (T child : list) {
			visit(child, visitor, maker);
		}
	}

	public static <T> void visit(T root, TreeVisitor<T> visitor, TreeMaker<T> maker) {
		TreeStructure<T> rootStructure = maker.toStructure(root);

		visitor.accept(rootStructure.get());

		visit(rootStructure.getChildren(), visitor, maker);
	}

	public static <T> T find(Collection<T> list, TreeAcceptor<T> acceptor, TreeMaker<T> maker) {

		for (T child : list) {
			T result = find(child, acceptor, maker);
			if (result != null)
				return result;
		}
		return null;
	}

	public static <T> T find(T root, TreeAcceptor<T> acceptor, TreeMaker<T> maker) {

		for (T t : new TreeWalker<T>(root, maker)) {
			if (acceptor.accept(t))
				return t;
		}
		return null;
	}

}
