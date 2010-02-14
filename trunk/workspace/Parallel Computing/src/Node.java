/**
 * Class Node provides elements of nodes and methods to access and set these elements.
 * 
 * @author Xi He, Jai Dayal, Kevin Pinto
 * @version 13-Feb-2010
 * 
 */
public class Node {
	//Array of neighbors.
	private Node[] neighbors;
	
	//Number of neighbors.
	private int numOfNeighbors=0;
	private int radio=0;
	
	//Node id.
	private int id;
	
	//Color integer value.
	private int color;
	
	//Status to set validity of node color.
	private boolean status;
	
	//Pointer to next colorClass.
	private int next = 0;
	
	/**
	 * Constructor accepting node id, color, status and initial number.
	 * 
	 * 
	 * @param id
	 * @param color
	 * @param status
	 * @param initialNum
	 */
	public Node(int id, int color, boolean status,int initialNum) {
		this.id = id;
		this.color = color;
		this.status = status;
		neighbors = new Node[initialNum];
		radio=initialNum/10;
	}
	
	/**
	 * Returns pointer to next color class.
	 * 
	 * @return pointer to next color class
	 */
	public int getNext() {
		return next;
	}

	/**
	 * Sets the value of next colorClass start index value.
	 * 
	 * @param next
	 */
	public void setNext(int next) {
		this.next = next;
	}

	

	/**
	 * Method to add a neighbor to node.
	 * 
	 * @param node object
	 */
	public void addNeighbor(Node n) {
		Node[] oldData;
		if(this.numOfNeighbors==neighbors.length){
		    oldData=neighbors;
			neighbors = (Node[])new Node[numOfNeighbors+radio];
		    System.arraycopy(oldData, 0, neighbors, 0, oldData.length);
		}
		neighbors[numOfNeighbors]=n;
		numOfNeighbors++;

	}

	/**
	 * Return validity of color.
	 * 
	 * @return validity of color
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Sets the validity of color.
	 * 
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	
	/**
	 * Set the node's color.
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * Get node's color.
	 * 
	 * @return node's color
	 */
	public int getColor() {
		return this.color;
	}

	/**
	 * Get Node's identification number.
	 * 
	 * @return node's identification
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Method to check if given color is valid for node by
	 * checking its neighboring nodes.
	 * 
	 * @param color to be checked.
	 * @return true or false
	 */
	public boolean validColor(int color) {
		for (int i = 0; i < numOfNeighbors; i++) {
			if (color == neighbors[i].getColor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the number of neighbors a node has.
	 * 
	 * @return number of neighbors
	 */
	public int getDegree() {
		return numOfNeighbors;
	}

}