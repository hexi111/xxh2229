//******************************************************************************
//
// File:    GraphColoringSeq.java
//
// Team Project: Parallel graph coloring
// Team name: Beowulf
// Team member: Jai Dayal, Kevin Pinto, Xi He
//
//******************************************************************************
import edu.rit.pj.Comm;
/**
 * Sequential program for graph coloring.
 *
 */
public class GraphColoringSeq {
	public static Node[] nodes;
	static int colorCount = 1;

	public static void main(String... args) throws Exception {
		// Start timing.
		long t1 = System.currentTimeMillis();
		Comm.init (args);
		nodes = Node.getInstance(args[0]);
		long tx= System.currentTimeMillis();
		System.out.println((tx - t1) + " msec");
		GraphColoring gc=new GraphColoring(nodes);
		//gc.permuteByDegree();
		gc.color();
		gc.permuteByColor();
		gc.color();
		gc.printColor();
		// Stop timing.
		long t2 = System.currentTimeMillis();
		
		// Printing run time.
		System.out.println((t2 - t1) + " msec");

	}
}