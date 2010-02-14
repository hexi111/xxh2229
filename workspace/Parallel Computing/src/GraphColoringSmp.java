import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.Comm;

/**
 * Class GraphColoringSmp is SMP parallel program to vertex color a graph.
 * It was designed and built as per requirements of Parallel Computing -1
 * Team Project in Department of Computer Science at RIT.
 * 
 * <P>
 * Usage: java -Dpj.nt=<I>K</I> GraphColoringSmp <I>input</I>
 * <BR><I>K</I> = Number of Parallel threads
 * <BR><I>input</I> = input file name in DIMACS format.
 * <P>
 * 
 * The program's algorithm is based on Culberson's lemma and Scalable parallel 
 * graph coloring algorithms by  A. H. Gebremedhin and F. Manne.
 * 
 * Instructor: Prof. Alan Kaminsky
 * 
 * @author Xi He, Jai Dayal, Kevin Pinto
 * @version 13-Feb-2010
 * 
 */
public class GraphColoringSmp {

	//Instance of GraphColoring sequence.
	static GraphColoring gc;
	
	static int start;
	static int end;
	
	//Limiting factor for running coloring in parallel.
	static int limit = 100;

	/**
	 * Main Program
	 * 
	 * @param input file name
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		// Start timing.
		long time = -System.currentTimeMillis();

		// Initialize middleware.
		Comm.init (args);

		//Instantiate graph coloring class to perform operations over graph.
		gc = new GraphColoring(args[0]);
		
		
	// Phase 1 -  FF algorithm over entire array.
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, gc.getNum(), new IntegerForLoop() {

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {
						gc.color(first, last);
					}
				});
			}
		});
		
		//Calculate number of colors used so far.
		gc.calculateColor();
		
	//Phase 2
		//Sort array of nodes as per color in reverse order.
		gc.permuteByColor();
		
		// Identify different color classes start and end index values.
		gc.colorClassSetup();
		
		// Reset colors of all node to zero.
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
		
		//Find the number of colors used so far.
		gc.calculateColor();
		
	// Phase 3 - Find the conflicts in array of nodes in parallel.
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

	// Phase 4 - Resolve conflict sequentially.
		gc.resolveConflict();
		
		
		// Stop timing.
		time += System.currentTimeMillis();
		
		//Print the number of colors used to color the graph.
		gc.printColor();
		
		// Printing run time.
		System.out.println(time + " msec");

	}

}