package edu.rit.intern;

import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class FindWords {
	private char[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z' };
	private int dimension;
	private char[][] letters;
	private long seed;
	private TST tree;
	private int maxLength;

	public void setTree(TST tree) {
		this.tree = tree;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public static void main(String argv[]) throws Exception {
		FindWords finder = new FindWords(20, 43599476343L);
		//finder.generateTable();
		BufferedReader fp=new BufferedReader(new FileReader(System
				.getProperty("user.dir")
				+ System.getProperty("file.separator") + "letters.txt"));
		String line;
		int num=0;
		while((line=fp.readLine())!=null){
			finder.letters[num]=line.toCharArray();
			num++;
		}
		
		TST tree = new TST();
		tree.buildTree("test1.txt");
		finder.setTree(tree);
		finder.find(0, 0).print();

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
			// System.out.println(i);
		}
		fp.close();
	}

	public Result find(int row_begin, int column_begin) {
		if((row_begin>this.dimension) ||(column_begin>this.dimension)){
			return new Result(0,0,0,0,0);
		}
		int row_len = 1, column_len = 1, area = 1;
		int row_len_result = 1, column_len_result = 1;
		while (column_len <= maxLength) {
			if (verify(row_begin, column_begin, row_len, column_len)) {
				column_len++;
			} else
				break;
		}
		column_len--;
		while (row_len <= maxLength) {
			if (verify(row_begin, column_begin, row_len, column_len)) {
				row_len++;
			} else
				break;
		}
		row_len--;
		area = row_len * column_len;
		row_len_result = row_len;
		column_len_result = column_len;
		while (column_len > 1) {
			column_len--;
			while (row_len <= maxLength) {
				if (verify(row_begin, column_begin, row_len + 1, column_len)) {
					row_len++;
				}
			}
			if (area < row_len * column_len) {
				area = row_len * column_len;
				row_len_result = row_len;
				column_len_result = column_len;
			}
		}
		Result res1=find(row_begin+1, column_begin);
		Result res2=find(row_begin, column_begin+1);
		if(res1.area>res2.area){
			if (res1.area>=area){
				return res1;
			}
			else return new Result(row_begin,row_len_result,column_begin,column_len_result,area);
		}
		else {
			if (res2.area>=area){
				return res1;
			}
			else return new Result(row_begin,row_len_result,column_begin,column_len_result,area);

		}
	}

	public boolean verify(int row_begin, int column_begin, int row_len,
			int column_len) {
		boolean flag = true;
		for (int i = row_begin; i <= (row_begin + row_len - 1); i++) {
			flag = flag
					|| tree.contains(letters[i], column_begin, (column_begin
							+ column_len - 1));
		}
		char[] temp = new char[row_len];
		for (int i = column_begin; i <= (column_begin + column_len - 1); i++) {
			for (int j = row_begin; j <= (row_begin + row_len - 1); j++) {
				temp[j - row_begin] = letters[j][i];
			}
			flag = flag
					|| tree
							.contains(temp, row_begin,
									(row_begin + row_len - 1));
		}
		return flag;
	}

	private class Result {
		int column_begin;
		int column_len;
		int row_begin;
		int row_len;
		int area;

		public Result(int row_begin, int row_len, int column_begin,
				int column_len, int area) {
			this.row_begin = row_begin;
			this.row_len = row_len;
			this.column_begin = column_begin;
			this.column_len = column_len;
			this.area = area;
		}
		public void print(){
			System.out.println("row_begin= "+row_begin);
			System.out.println("row_len= "+row_len);
			System.out.println("column_begin= "+column_begin);
			System.out.println("column_len= "+column_len);
			System.out.println("area= "+area);			
		}
	}
}
