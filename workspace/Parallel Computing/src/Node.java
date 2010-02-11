//******************************************************************************
//
// File:    Node.java
//
// Team Project: Parallel graph coloring
// Team name: Beowulf
// Team member: Jai Dayal, Kevin Pinto, Xi He
//
//******************************************************************************
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class Node provides the functionality for the manipulation of vertices in a
 * graph.
 * 
 */
public class Node {
	private ArrayList<Node> neighbors;
	private int id;
	private int color;
	private boolean status;
	private static final int DEFAULT_COLOR = 0;
	private int next = 0;

	/**
	 * @param filename
	 *            from which a graph can be read into memory
	 * @return
	 * @throws Exception
	 */
	public static Node[] getInstance(String filename) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		int numNodes;
		String line = "";
		Node[] nodes = null;
		while ((line = reader.readLine()) != null) {
			if (line.charAt(0) == 'p') {
				String[] values = line.split(" ");
				numNodes = Integer.parseInt(values[2]);
				nodes = new Node[numNodes];
				for (int i = 0; i < numNodes; i++) {
					nodes[i] = new Node(i + 1, DEFAULT_COLOR, true);
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

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public Node(int id, int color, boolean status) {
		this.id = id;
		this.color = color;
		this.status = status;
		neighbors = new ArrayList<Node>();
		// System.out.printf("Created node: %d color: %d\n",id, color);
	}

	public void addNeighbor(Node n) {
		if (!neighbors.contains(n)) {
			neighbors.add(n);
			// System.out.println("Node: " + this.id + " added neighbor "
			// + n.getId());
		}
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getColor() {
		return this.color;
	}

	public int getId() {
		return this.id;
	}

	public boolean validColor(int color) {
		for (int i = 0; i < neighbors.size(); i++) {
			if (color == neighbors.get(i).getColor()) {
				return false;
			}
		}
		return true;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDegree() {
		return neighbors.size();
	}

	public int getNeighbour(int index) {
		return neighbors.get(index).getId();

	}
}
