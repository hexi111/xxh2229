package edu.rit.intern;

import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.FileReader;
//import java.io.BufferedReader;

/**
 * In the program, we generate a random letter table and try to find out the
 * largest possible rectangle in the letter table. We also use Ternary Search
 * Trie({@linkplain TST}) to store the word dictionary. In our search algorithm,
 * we will examine every rectangle starting from every location in the letter
 * table and with the length and width of the maximum.
 * 
 * @author Xi He
 * @version 04/06/2010
 * 
 */
public class FindWords {
	private char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };
	// The size of the letter table
	private int dimension;
	// The letter table
	private char[][] letters;
	// Seed for generating random number
	private long seed;
	// The instance of Ternary Search Trie
	private TST tree;
	// The maximum length of words
	private int maxLength;
	
	// Temporary variable
	private char[] letters_column;
	private boolean result[][];
	private boolean result1[][];
	private boolean result2[][];
	private boolean result_column[];
	
	private int row_1 = 0;
	private int row_2 = 0;
	private int column_1 = 0;
	private int column_2 = 0;
	private int area = 0;

	public void setTree(TST tree) {
		this.tree = tree;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public static void main(String argv[]) throws Exception {
		FindWords finder = new FindWords(300, 43599476343L);
		finder.generateTable();
//		BufferedReader fp = new BufferedReader(new FileReader(System
//				.getProperty("user.dir")
//				+ System.getProperty("file.separator") + "letters.txt"));
//		String line;
//		int num = 0;
//		while ((line = fp.readLine()) != null) {
//			finder.letters[num] = line.toCharArray();
//			num++;
//		}

		TST tree = new TST();
		tree.buildTree("WORD.LST");
		finder.setTree(tree);
		finder.setMaxLength(tree.getDepth());
		finder.search();
	}

	public FindWords(int dimension, long seed) {
		this.dimension = dimension;
		this.seed = seed;
		letters = new char[dimension][dimension];
	}

	public void generateTable() throws IOException {
		Random prng = new Random(seed);
		BufferedWriter fp = new BufferedWriter(new FileWriter(System
				.getProperty("user.dir")
				+ System.getProperty("file.separator") + "letters.txt"));
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				letters[i][j] = alphabet[(int) (prng.nextDouble() * 26)];
				fp.write(letters[i][j]);
			}
			fp.newLine();
		}
		fp.close();
	}

	public void search() {
		result = new boolean[this.maxLength][this.maxLength];
		result1 = new boolean[this.maxLength][this.maxLength];
		result2 = new boolean[this.maxLength][this.maxLength];
		letters_column = new char[this.maxLength];
		result_column = new boolean[this.maxLength];

		for (int i = 0; i < this.dimension; i++) {
			for (int j = 0; j < this.dimension; j++) {
				for (int i1 = 0; i1 < this.maxLength; i1++) {
					for (int j1 = 0; j1 < this.maxLength; j1++) {
						result[i1][j1] = false;
						result1[i1][j1] = false;
						result2[i1][j1] = false;
					}
				}
				search(i, j, i + this.maxLength - 1 <= this.dimension - 1 ? i
						+ this.maxLength - 1 : this.dimension - 1, j
						+ this.maxLength - 1 <= this.dimension - 1 ? j
						+ this.maxLength - 1 : this.dimension - 1);
			}
		}
		System.out.println("row from " + (this.row_1+1) + " to " + (this.row_2+1));
		System.out.println("column from " + (this.column_1+1) + " to "
				+ (this.column_2+1));
		//System.out.println(area);
		for (int i = row_1; i <= row_2; i++) {
			for (int j = column_1; j <= column_2; j++) {
				System.out.print(letters[i][j] + ",");
			}
			System.out.print("\n");
		}
	}

	public void search(int row_begin, int column_begin, int row_end,
			int column_end) {
		// System.out.println(row_begin + "," + column_begin + "," + row_end
		// + "," + column_end);
		for (int i = row_begin; i <= row_end; i++) {
			for (int j = column_begin; j <= column_end; j++) {
				letters_column[j - column_begin] = letters[i][j];
			}
			tree.contains(letters_column, result1[i - row_begin], column_begin,
					column_end);
		}
		for (int i = column_begin; i <= column_end; i++) {
			for (int j = row_begin; j <= row_end; j++) {
				letters_column[j - row_begin] = letters[j][i];
				result_column[j - row_begin] = result2[j - row_begin][i
						- column_begin];
			}
			for (int k = 0; k < this.maxLength; k++) {
				result_column[k] = true;
			}
			tree.contains(letters_column, result_column, row_begin, row_end);
			for (int j = row_begin; j <= row_end; j++) {
				result2[j - row_begin][i - column_begin] = result_column[j
						- row_begin];
			}
		}
		for (int i = 0; i < this.maxLength; i++) {
			for (int j = 0; j < this.maxLength; j++) {
				result[i][j] = result1[i][j] && result2[i][j];
				if (result[i][j] && ((i + 1) * (j + 1) > area)) {
					boolean temp_column = true;
					for (int k = 0; k < i; k++) {
						temp_column = temp_column && result1[k][j];
					}
					for (int k = 0; k < j; k++) {
						temp_column = temp_column && result2[i][k];
					}
					if (temp_column) {
						area = (i + 1) * (j + 1);
						row_1 = row_begin;
						column_1 = column_begin;
						row_2 = row_begin + i;
						column_2 = column_begin + j;
					}
				}
			}
		}
	}
}
