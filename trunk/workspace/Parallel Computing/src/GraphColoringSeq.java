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
		GraphColoring gc = new GraphColoring(args[0]);
		long t2= System.currentTimeMillis();
		System.out.println("I/O time "+(t2 - t1) + " msec");
		//gc.permuteByDegree();
		gc.color();
		gc.calculateColor();
		gc.printColor();
		long t3= System.currentTimeMillis();
		System.out.println("phase 1 "+(t3 - t2) + " msec");
		gc.permuteByColor();
		gc.cleanColor();
		gc.color();
		gc.calculateColor();
		gc.printColor();
		// Stop timing.
		long t4 = System.currentTimeMillis();
		System.out.println("phase 2 "+(t4 - t3) + " msec");
		// Printing run time.
		System.out.println("total running time (exclude I/O) "+(t4 - t2) + " msec");
		System.out.println("total running time "+(t4 - t1) + " msec");

	}
}