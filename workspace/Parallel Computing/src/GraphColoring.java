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
	private Node[] nodes;
	private int colorCount = 1;
	private int currentColor;
	private int currentIndex;
	private int loop;
	private int num;
	private int conflict=0;

	public GraphColoring(Node[] nodes) {
		this.nodes = nodes;
		this.num = nodes.length - 1;
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
		for (int i = 0; i <=nodes.length - 1; i++) {
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

	public boolean moreColorClass() {
		return (currentIndex <= nodes.length-1);
	}

	public int nextColorClass() {
		int index = nodes[currentIndex].getNext();
		if(index==nodes.length-1){
			currentIndex=nodes.length;
		}else {
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
	public int getConflict(){
		return this.conflict;
	}
}
