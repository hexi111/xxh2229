import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;

//******************************************************************************
//
// File:    GraphColoring.java
//
// Team Project: Parallel graph coloring
// Team name: Beowulf
// Team member: Jai Dayal, Kevin Pinto, Xi He
//
//******************************************************************************
/**
 * Class GraphColoring encapsulates the operations of vertex coloring.
 * 
 */
public class GraphColoring {

	private int colorCount = 1;
	private int currentColor;
	private int currentIndex;
	private int loop;
	private int num;
	private int conflict = 0;
	private Node[] test;

	private static Node[] nodes;
	private static String name;
	private static long partitionSize;
	private static long[] partition;

	public GraphColoring(String filename) throws Exception {

		nodes = this.createGraph(filename);
		this.num = nodes.length - 1;
	}

	public GraphColoring(String filename, boolean ifParallel) throws Exception {

		createGraph(filename, ifParallel);
		test=nodes;
		this.num = nodes.length - 1;
	}

	public void createGraph(String filename, boolean ifParallel)
			throws Exception {
		name = filename;
		int edges;
		int radio;
		long readChar = 0;
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		while (line.charAt(0) != 'p') {
			readChar = readChar + line.length();
			readChar++;
			line = reader.readLine();
		}
		readChar = readChar + line.length();
		readChar++;
		String[] values = line.split(" ");
		int numNodes = Integer.parseInt(values[2]);
		nodes = new Node[numNodes];
		edges = Integer.parseInt(values[3]);
		radio = edges / (2 * numNodes);
		for (int i = 0; i < numNodes; i++) {
			nodes[i] = new Node(i + 1, 0, true, radio);
		}
		File fd = new File(filename);
		long len = fd.length();
		long fileSize = len - readChar;
		ParallelTeam team = new ParallelTeam();
		int numofProcessors = team.getThreadCount();
		partitionSize = fileSize / numofProcessors;
		partition = new long[numofProcessors + 1];
		partition[0] = readChar-1;
		for (int i = 1; i < numofProcessors; i++) {
			partition[i] = reader.skip(partitionSize);
			int temp = reader.read();
			partition[i]++;
			while (temp != 'e') {
				temp = reader.read();
				partition[i]++;
			}
			//partition[i]--;
			partition[i] = partition[i] + partition[i - 1];
		}
		partition[0]++;
		partition[numofProcessors] = len;
		team.execute(new ParallelRegion() {
			public void run() throws Exception {
				BufferedReader reader = new BufferedReader(new FileReader(name));
				int i = getThreadIndex();
				long nbytes = partition[i];
				// System.out.println(i+":"+nbytes);
				while (nbytes > 0L) {
					nbytes -= reader.skip(nbytes);
				}
				//System.out.println(i+":"+reader.readLine());
				long size = partition[i + 1] - partition[i]+1;
				long j = 0;
				while (j < size-2) {
					String line = reader.readLine();
					j = j + line.length() + 1;
					String[] values = line.split(" ");
					int n1 = Integer.parseInt(values[1]);
					int n2 = Integer.parseInt(values[2]);
					Node N1 = nodes[n1 - 1];
					Node N2 = nodes[n2 - 1];
					N1.addNeighbor(N2);
					N2.addNeighbor(N1);
				}

			}
		});
	}

	/**
	 * @param filename
	 *            from which a graph can be read into memory
	 * @return
	 * @throws Exception
	 */
	public Node[] createGraph(String filename) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		int numNodes;
		int edges;
		int radio;
		String line = "";
		Node[] nodes = null;
		while ((line = reader.readLine()) != null) {
			if (line.charAt(0) == 'p') {
				String[] values = line.split(" ");
				numNodes = Integer.parseInt(values[2]);
				nodes = new Node[numNodes];
				edges = Integer.parseInt(values[3]);
				radio = edges / (2 * numNodes);
				for (int i = 0; i < numNodes; i++) {
					nodes[i] = new Node(i + 1, 0, true, radio);
				}
			}

			// e 1 2
			else if (line.charAt(0) == 'e') {
				String[] values = line.split(" ");
				int n1 = Integer.parseInt(values[1]);
				int n2 = Integer.parseInt(values[2]);
				Node N1 = nodes[n1 - 1];
				Node N2 = nodes[n2 - 1];
				N1.addNeighbor(N2);
				N2.addNeighbor(N1);
			}

		}
		return nodes;
	}

	public int getNum() {
		return num;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void permuteByDegree() {
		int tempDegree = 0;
		int tempIndex = 0;
		Node tempNode = null;
		for (int i = 0; i <= nodes.length - 1; i++) {
			tempDegree = nodes[i].getDegree();
			tempIndex = i;
			for (int j = i + 1; j <= nodes.length - 1; j++) {
				if (tempDegree < nodes[j].getDegree()) {
					tempDegree = nodes[j].getDegree();
					tempIndex = j;
				}
			}
			tempNode = nodes[i];
			nodes[i] = nodes[tempIndex];
			nodes[tempIndex] = tempNode;
		}
	}

	public void permuteByColor() {
		int tempColor = 0;
		int tempIndex = 0;
		Node tempNode = null;
		for (int i = 0; i <= nodes.length - 1; i++) {
			tempColor = nodes[i].getColor();
			tempIndex = i;
			for (int j = i + 1; j <= nodes.length - 1; j++) {
				if (tempColor < nodes[j].getColor()) {
					tempColor = nodes[j].getColor();
					tempIndex = j;
				}
			}
			tempNode = nodes[i];
			nodes[i] = nodes[tempIndex];
			nodes[tempIndex] = tempNode;
		}
	}

	public void color() {
		color(0, nodes.length - 1);
	}

	public void color(int begin, int end) {
		for (int i = begin; i <= end; i++) {
			int color = 1;
			while (!nodes[i].validColor(color)) {
				color++;
			}
			nodes[i].setColor(color);
		}
	}

	public void cleanColor() {
		for (int i = 0; i <= nodes.length - 1; i++) {
			nodes[i].setColor(0);
		}
	}

	public void print() {
		for (int i = 0; i < nodes.length; i++) {
			System.out.println("Node No. :" + nodes[i].getId() + " Color: "
					+ nodes[i].getColor());
		}

		System.out.println("No. of Colors used: " + colorCount);
	}

	public void calculateColor() {
		int colorCount = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getColor() > colorCount) {
				colorCount = nodes[i].getColor();
			}
			// System.out.println("Node No. :" + nodes[i].getId() + " Color: "
			// + nodes[i].getColor());
		}
		this.colorCount = colorCount;
	}

	public void printColor() {
		System.out.println("No. of Colors used: " + colorCount);
	}

	public void colorClassSetup() {
		currentColor = nodes[0].getColor();
		currentIndex = 0;
		loop = 1;
		while ((currentIndex <= nodes.length - 1) && (loop <= nodes.length - 1)) {
			if ((nodes[loop].getColor() == currentColor)
					& (loop != nodes.length - 1)) {
				loop++;
				continue;
			} else if ((nodes[loop].getColor() == currentColor)
					& (loop == nodes.length - 1)) {
				nodes[currentIndex].setNext(nodes.length - 1);
				currentIndex = nodes.length;
			} else if (nodes[loop].getColor() != currentColor) {
				nodes[currentIndex].setNext(loop - 1);
				currentIndex = loop;
				currentColor = nodes[currentIndex].getColor();
				loop = currentIndex + 1;
			} else if (currentIndex == nodes.length - 1) {
				nodes[currentIndex].setNext(nodes.length - 1);
				currentIndex = nodes.length;
			}
		}
		currentIndex = 0;
	}

	public boolean moreColorClass() {
		return (currentIndex <= nodes.length - 1);
	}

	public int nextColorClass() {
		int index = nodes[currentIndex].getNext();
		if (index == nodes.length - 1) {
			currentIndex = nodes.length;
		} else {
			currentIndex = index + 1;
		}
		return index;
	}

	public void findConflict(int start, int end) {
		for (int i = start; i <= end; i++) {
			if (!nodes[i].validColor(nodes[i].getColor())) {
				nodes[i].setStatus(false);
			}

		}
	}

	public void resolveConflict() {
		int colorCount = 1;
		for (int i = 0; i < nodes.length; i++) {
			if (!nodes[i].getStatus()) {
				conflict++;
				int color = 1;
				while (!nodes[i].validColor(color)) {
					color++;
				}
				nodes[i].setColor(color);
			}
			if (nodes[i].getColor() > colorCount) {
				colorCount = nodes[i].getColor();
			}
		}
		this.colorCount = colorCount;
	}

	public int getConflict() {
		return this.conflict;
	}
}
