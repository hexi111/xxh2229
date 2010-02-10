//******************************************************************************
//
// File:    GraphColoringSmp.java
//
// Team Project: Parallel  graph coloring
// Team name: Beowulf
// Team member: Jai Dayal, Kevin Pinto, Xi He
//
//******************************************************************************
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.Comm;

/**
 * Parallel program for graph coloring
 *
 */
public class GraphColoringSmp {

	static Node[] nodes;
	static GraphColoring gc;
	static int start;
	static int end;
	static int limit = 100;
	static long tx;

	/**
	 * Main Program
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		// Start timing.
		long t1 = System.currentTimeMillis();

		// Initialize middleware.
		Comm.init (args);

		// Check usage and try catch block needs to be implemented.
		nodes = Node.getInstance(args[0]);
		tx=System.currentTimeMillis();
		System.out.println(0+" "+0+" "+(tx-t1));
		gc = new GraphColoring(nodes);
		//gc.permuteByDegree();
		// Phase 1
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, gc.getNum(), new IntegerForLoop() {

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {

						gc.color(first, last);
						System.out.println(first+" "+last+" "+(System.currentTimeMillis()-tx));

					}
				});
			}
		});
		long t2 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 1 " + (t2 - t1) + " msec");

		// Phase 2
		// Sorting
		gc.permuteByColor();
		// Identify different color classes
		gc.colorClassSetup();
		// Clear the color in each node
		gc.cleanColor();

		start = 0;
		// Iterate each color class and sort the vertices in it.
		while (gc.moreColorClass()) {
			end=gc.nextColorClass();
			// We don't always color in parallel. When the number of vertices is
			// small, there is no point of coloring the vertices in parallel.
			if (end - start > limit) {
				new ParallelTeam().execute(new ParallelRegion() {
					public void run() throws Exception {
						execute(start, end, new IntegerForLoop() {

							// Extra padding.
							long p0, p1, p2, p3, p4, p5, p6, p7;
							long p8, p9, pa, pb, pc, pd, pe, pf;

							public void run(int first, int last) {
								gc.color(first, last);
							}

						});
					}
				});
			} else {
				gc.color(start, end);
			}
			start=end+1;
		}

		long t3 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 2 " + (t3 - t2) + " msec");

		// Phase 3
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, gc.getNum(), new IntegerForLoop() {

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {
						gc.findConflict(first, last);

					}

				});
			}
		});
		long t4 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 3 " + (t4 - t3) + " msec");

		// Phase 4
		gc.resolveConflict();

		// Stop timing.
		long t5 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 4 " + (t5 - t4) + " msec");

		gc.printColor();

		// Printing run time.
		System.out.println("total running time " + (t5 - t1) + " msec");

	}

}
