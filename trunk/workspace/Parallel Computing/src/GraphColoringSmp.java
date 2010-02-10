import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;

public class GraphColoringSmp {

	static Node[] nodes;
	static GraphColoring gc;
	static int start;
	static int end;
	static int limit = 100;

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
		// Comm.init (args);

		// Check usage and try catch block needs to be implemented.
		nodes = Node.getInstance(args[0]);
		gc = new GraphColoring(nodes);
		// Phase 1
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, nodes.length - 1, new IntegerForLoop() {

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {

						gc.color(first, last);

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
		for (int i = 0; i < nodes.length - 1; i++) {
			nodes[i].setColor(0);
		}

		int tempColor = nodes[0].getColor();
		int loop = 0;
		start = 0;
		// Iterate each color class and sort the vertices in it.
		while (loop <= nodes.length - 1) {
			if ((nodes[loop].getColor() == tempColor)
					&& (loop != nodes.length - 1)) {
				loop++;
				continue;
			}
			end = loop;
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
			if (loop != (nodes.length - 1)) {
				start = end + 1;
				tempColor = nodes[start].getColor();
			}
			loop++;
		}

		long t3 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 2 " + (t3 - t2) + " msec");

		// Phase 3
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, nodes.length - 1, new IntegerForLoop() {

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {
						for (int i = first; i <= last; i++) {
							if (!nodes[i].validColor(nodes[i].getColor())) {
								nodes[i].setStatus(false);
							}

						}

					}

				});
			}
		});
		long t4 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 3 " + (t4 - t3) + " msec");

		// Phase 4
		int colorCount = 1;
		for (int i = 1; i < nodes.length; i++) {
			if (!nodes[i].getStatus()) {
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

		// Stop timing.
		long t5 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 4 " + (t5 - t4) + " msec");

		System.out
				.println("No. of Colors used in after phase 4: " + colorCount);

		// Printing run time.
		System.out.println("total running time " + (t5 - t1) + " msec");

	}

}
