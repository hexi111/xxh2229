package edu.rit.intern;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.LinkedList;

/**
 * This class is the implementation of Ternary Search Trie.
 * 
 * @author Xi He
 * @version 04/01/2010
 * 
 */
public class TST {
	private Node root;
	// The maximum length of words
	private int depth;

	public int getDepth() {
		return depth;
	}

	private class Node {
		char c;
		Node l = null, m = null, r = null;
		boolean end = false;

		public Node(char c) {
			this.c = c;
		}
	}

	public void contains(char[] chs, boolean[] result, int begin, int end) {
		contains(root, chs, result, begin, end, begin);
	}

	public void contains(Node x, char[] chs, boolean[] result, int begin,
			int end, int point) {
		if (x == null) {
			for (int i = point; i <= end; i++) {
				result[i - begin] = false;
			}
			return;
		}
		char c = chs[point - begin];
		if (c < x.c)
			contains(x.l, chs, result, begin, end, point);
		else if (c > x.c)
			contains(x.r, chs, result, begin, end, point);
		else if (point < end ) {
			result[point - begin] = x.end;
			contains(x.m, chs, result, begin, end, point + 1);
		} else
			result[point-begin] = x.end;
	}

	public void add(String s) {
		root = add(root, s, 0);
	}

	public Node add(Node x, String s, int i) {
		char c = s.charAt(i);
		if (x == null) {
			x = new Node(c);
		}
		if (c == x.c) {
			if (s.length() - i == 1)
				x.end = true;
			else
				x.m = add(x.m, s, i + 1);
		} else if (x.c > c) {
			x.l = add(x.l, s, i);
		} else if (x.c < c) {
			x.r = add(x.r, s, i);

		}
		return x;
	}

	public void buildTree(String filename) throws FileNotFoundException,
			IOException {
		BufferedReader fp = new BufferedReader(new FileReader(System
				.getProperty("user.dir")
				+ System.getProperty("file.separator") + filename));
		String line;
		while ((line = fp.readLine()) != null) {
			int char_depth = line.length();
			if (char_depth > this.depth)
				this.depth = char_depth;
			add(line);
		}

	}

	public void test() {
		LinkedList<Node> ll = new LinkedList<Node>();
		ll.add(root);
		ll.add(null);
		Node temp;
		while (!ll.isEmpty()) {
			while ((temp = ll.poll()) != null) {
				System.out.print(temp.c);
				if (temp.l != null)
					ll.add(temp.l);
				if (temp.m != null)
					ll.add(temp.m);
				if (temp.r != null)
					ll.add(temp.r);
			}
			if (!ll.isEmpty())
				ll.add(null);
			System.out.println("");

		}

	}

	/**
	 * Just for test
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {
		TST tree = new TST();
		tree.buildTree("test1.txt");
		tree.test();
	}

}
