public class GraphColoring {
	private Node[] nodes;
	private int colorCount = 1;

	public GraphColoring(Node[] nodes) {
		this.nodes = nodes;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public void permuteByDegree() {
		int tempDegree = 0;
		int tempIndex = 0;
		Node tempNode = null;
		for (int i = 0; i < nodes.length - 1; i++) {
			tempDegree = nodes[i].getDegree();
			tempIndex = i;
			for (int j = i + 1; j < nodes.length - 1; j++) {
				if (tempDegree < nodes[j].getDegree()) {
					tempDegree = nodes[j].getDegree();
					tempIndex = j;
				}
			}
			tempNode = nodes[i];
			nodes[i] = nodes[tempIndex];
			nodes[i] = tempNode;
		}
	}

	public void permuteByColor() {
		int tempColor = 0;
		int tempIndex = 0;
		Node tempNode = null;
		for (int i = 0; i < nodes.length - 1; i++) {
			tempColor = nodes[i].getColor();
			tempIndex = i;
			for (int j = i + 1; j < nodes.length - 1; j++) {
				if (tempColor < nodes[j].getColor()) {
					tempColor = nodes[j].getColor();
					tempIndex = j;
				}
			}
			tempNode = nodes[i];
			nodes[i] = nodes[tempIndex];
			nodes[i] = tempNode;
		}
	}

	public void color() {
		color(0, nodes.length - 1);
	}

	public void color(int begin, int end) {
		boolean flag = true;
		for (int i = begin; i <=end; i++) {
			flag = true;
			for (int j = 1; j <= colorCount; j++) {
				if (nodes[i].validColor(j)) {
					nodes[i].setColor(j);
					flag = false;
					j = colorCount + 1;
				}
			}
			if (flag) {
				colorCount++;
				nodes[i].setColor(colorCount);
			}
		}
	}

	public void print() {
		for (int i = 0; i < nodes.length; i++) {
			System.out.println("Node No. :" + nodes[i].getId() + " Color: "
					+ nodes[i].getColor());
		}

		System.out.println("No. of Colors used: " + colorCount);
	}

	public void printColor() {
		int colorCount = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getColor() > colorCount) {
				colorCount = nodes[i].getColor();
			}
			// System.out.println("Node No. :" + nodes[i].getId() + " Color: "
			// + nodes[i].getColor());
		}
		this.colorCount=colorCount;
		System.out.println("No. of Colors used: " + colorCount);
	}
}
