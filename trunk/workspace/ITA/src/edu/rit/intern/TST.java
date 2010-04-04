package edu.rit.intern;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.LinkedList;

/**
 * Ternary Search Trie is
 * 
 * @author Xi He
 * @version 04/01/2010
 * 
 */
public class TST {
	private Node root;
	private int depth;

	private class Node {
		char c;
		Node l = null, m = null, r = null;
		boolean end = false;

		public Node(char c) {
			this.c = c;
		}
	}

	public boolean contains(String s) {
		if (s.length() == 0)
			return false;
		return contains(root, s, 0);
	}

	public boolean contains(Node x, String s, int i) {
		if (x == null)
			return false;
		char c = s.charAt(i);
		if (c < x.c)
			return contains(x.l, s, i);
		else if (c > x.c)
			return contains(x.r, s, i);
		else if (i < s.length() - 1)
			return contains(x.m, s, i + 1);
		else
			return x.end;
	}

	public boolean contains(char[] chs, int begin, int end) {
		if (end == begin)
			return false;
		return contains(root, chs, begin, end, begin);
	}

	public boolean contains(Node x, char[] chs, int begin, int end, int point) {
		if (x == null)
			return false;
		char c = chs[point];
		if (c < x.c)
			return contains(x.l, chs, begin, end, point);
		else if (c > x.c)
			return contains(x.r, chs, begin, end, point);
		else if (point < (end - begin + 1) - 1)
			return contains(x.m, chs, begin, end, point + 1);
		else
			return x.end;
	}

	public void contains(char[] chs, boolean [] result, int begin, int end) {
		contains(root, chs, result,begin, end,0);
	}

	public void contains(Node x, char[] chs, boolean[] result, int begin,
			int end, int point) {
		if (x == null) {
			for (int i = point; i <= end; i++) {
				result[i] = false;
			}
		}
		char c = chs[point];
		if (c < x.c)
			contains(x.l, chs, result, begin, end, point);
		else if (c > x.c)
			contains(x.r, chs, result, begin, end, point);
		else if (point < (end - begin + 1) - 1) {
			result[point] = result[point] || x.end;
			contains(x.m, chs, result, begin, end, point + 1);
		} else
			result[end] =result[point]|| x.end;
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

	public static void main(String argv[]) throws Exception {
		TST tree = new TST();
		tree.buildTree("test1.txt");
		tree.test();
	}

}
