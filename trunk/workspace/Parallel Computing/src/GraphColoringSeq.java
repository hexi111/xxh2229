public class GraphColoringSeq {
	public static Node[] nodes;
	static int colorCount = 1;

	public static void main(String... args) throws Exception {
		// Start timing.
		long t1 = System.currentTimeMillis();
		nodes = Node.getInstance(args[0]);
		GraphColoring gc=new GraphColoring(nodes);
		gc.color();
		gc.print();
		// Stop timing.
		long t2 = System.currentTimeMillis();
		
		// Printing run time.
		System.out.println((t2 - t1) + " msec");

	}
}