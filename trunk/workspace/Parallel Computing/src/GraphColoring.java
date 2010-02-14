import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Class GraphColoring encapsulates the operations of vertex coloring.
 * 
 * @author Xi He, Jai Dayal, Kevin Pinto
 * @version 13-Feb-2010
 */
public class GraphColoring {
	
	//Array of nodes.
	private Node[] nodes;
	
	//Colors to be used.
	private int colorCount = 1;
	
	//Present color being used.
	private int currentColor;
	
	//Current node being worked on.
	private int currentIndex;
	
	//
	private int loop;
	
	//Number of nodes in a graph.
	private int num;
	
	//Number of conflicts.
	private int conflict=0;

	/**
	 * Constructor for creating graph. Accepts input file name.
	 * 
	 * @param input file name
	 * @throws File not found exception
	 */
	public GraphColoring(String filename) throws Exception {
		
		this.nodes = this.createGraph(filename);
		this.num = nodes.length - 1;
	}

	/**
	 * Creates a graph from provided input file as argument.
	 * 
	 * @param filename from which a graph can be read into memory
	 * @return
	 * @throws File not found exception
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
				edges=Integer.parseInt(values[3]);
				radio=edges / (2*numNodes);
				for (int i = 0; i < numNodes; i++) {
					nodes[i] = new Node(i + 1, 0, true,radio);
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

	/**
	 * Get number of nodes in graph.
	 * 
	 * @return number of nodes in graph
	 */
	public int getNum() {
		return num;
	}

	/**
	 * Get all nodes in the graph stored in an array.
	 * 
	 * @return array of nodes.
	 */
	public Node[] getNodes() {
		return nodes;
	}


	/**
	 * Sort array of nodes by color in reverse order.
	 */
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

	/**
	 * Color the entire array using FF algorithm.
	 */
	public void color() {
		color(0, nodes.length - 1);
	}

	/**
	 * Color a part of array using FF algorithm from
	 * begin index value to end index value.
	 * 
	 * @param begin
	 * @param end
	 */
	public void color(int begin, int end) {
		for (int i = begin; i <= end; i++) {
			int color = 1;
			while (!nodes[i].validColor(color)) {
				color++;
			}
			nodes[i].setColor(color);
			}
	}

	/**
	 * Reset all node's color in array to zero.
	 */
	public void cleanColor() {
		for (int i = 0; i <=nodes.length - 1; i++) {
			nodes[i].setColor(0);
		}
	}

	/**
	 * Find the number of colors used so far.
	 */
	public void calculateColor() {
		int colorCount = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getColor() > colorCount) {
				colorCount = nodes[i].getColor();
			}
		}
		this.colorCount = colorCount;
	}

	/**
	 * Print the number of colors used to color the graph.
	 */
	public void printColor() {
		System.out.println("No. of Colors used: " + colorCount);
	}

	/**
	 * Find the starting and ending index values of color class.
	 */
	public void colorClassSetup() {
		currentColor = nodes[0].getColor();
		currentIndex = 0;
		loop = 1;
		while ((currentIndex <= nodes.length - 1) && (loop <= nodes.length -1)) {
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
				currentColor=nodes[currentIndex].getColor();
				loop = currentIndex + 1;
			} else if (currentIndex == nodes.length - 1) {
				nodes[currentIndex].setNext(nodes.length - 1);
				currentIndex = nodes.length;
			}
		}
		currentIndex = 0;
	}

	/**
	 * Used to check if index points within array size.
	 * 
	 * @return boolean value if index point within the array of nodes
	 */
	public boolean moreColorClass() {
		return (currentIndex <= nodes.length-1);
	}

	/**
	 * Get index value of next color class.
	 * 
	 * @return index value of next color class
	 */
	public int nextColorClass() {
		int index = nodes[currentIndex].getNext();
		if(index==nodes.length-1){
			currentIndex=nodes.length;
		}else {
		currentIndex = index + 1;
		}
		return index;
	}

	/**
	 * Find conflict in part of array starting from start index value and 
	 * ending with end index value.
	 * 
	 * @param start
	 * @param end
	 */
	public void findConflict(int start, int end) {
		for (int i = start; i <= end; i++) {
			if (!nodes[i].validColor(nodes[i].getColor())) {
				nodes[i].setStatus(false);
			}

		}
	}

	/**
	 * Resolve conflict by applying FF algorithm only to those node whose colors are invalid.
	 */
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
	
	/**
	 * Get number of conflicts.
	 * 
	 * @return number of conflicts
	 */
	public int getConflict(){
		return this.conflict;
	}
}