public class GraphColoringSeq {
	public static Node[] nodes;
	static int colorCount = 1;

	public static void main(String... args) throws Exception {
		// Start timing.
		long t1 = System.currentTimeMillis();
		nodes = Node.getInstance(args[0]);
		for (int i = 0; i < nodes.length; i++) {
			boolean flag = true;
			for (int j = 1; j <= colorCount; j++) {
				if (nodes[i].validColor(j)) {
					nodes[i].setColor(j);
					flag = false;
					j = colorCount + 1;
					System.out.println("Color of " + nodes[i].getId()
							+ " set to " + j);
				}
			}
			if (flag) {
				colorCount++;
				nodes[i].setColor(colorCount);
				System.out.println("Color of " + nodes[i].getId() + " set to "
						+ colorCount);
			}

		}

		for (int i = 0; i < nodes.length; i++) {
			System.out.println("Node No. :" + nodes[i].getId() + " Color: "
					+ nodes[i].getColor());
		}

		System.out.println("No. of Colors used: " + colorCount);
		// Stop timing.
		long t2 = System.currentTimeMillis();
		
		// Printing run time.
		System.out.println((t2 - t1) + " msec");

	}
}