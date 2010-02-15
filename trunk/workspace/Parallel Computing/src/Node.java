//******************************************************************************
//
// File:    Node.java
//
// Team Project: Parallel graph coloring
// Team name: Beowulf
// Team member: Jai Dayal, Kevin Pinto, Xi He
//
//******************************************************************************

/**
 * Class Node provides the functionality for the manipulation of vertices in a
 * graph.
 * 
 */
public class Node {
	private Node[] neighbors;
	private int numOfNeighbors=0;
	private int radio=0;
	private int id;
	private int color;
	private boolean status;
	private int next = 0;

	
	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public Node(int id, int color, boolean status,int initialNum) {
		this.id = id;
		this.color = color;
		this.status = status;
		neighbors = new Node[initialNum];
		radio=initialNum/10;
		// System.out.printf("Created node: %d color: %d\n",id, color);
	}

	public synchronized void addNeighbor(Node n) {
		Node[] oldData;
		if(this.numOfNeighbors==neighbors.length){
		    oldData=neighbors;
			neighbors = (Node[])new Node[numOfNeighbors+radio];
		    System.arraycopy(oldData, 0, neighbors, 0, oldData.length);
		}
		neighbors[numOfNeighbors]=n;
		numOfNeighbors++;

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
		for (int i = 0; i < numOfNeighbors; i++) {
			if (color == neighbors[i].getColor()) {
				return false;
			}
		}
		return true;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDegree() {
		return numOfNeighbors;
	}

}
