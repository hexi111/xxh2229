import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;

public class GraphColorSmp {

	static Node[] nodes;
	static int start;
	static int end;
	static int limit=100;

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

		// First coloring using First Fit Heuristic on Nodes sorted by degree.
		// Phase 1: (Have to make a function for coloring procedure.)
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, nodes.length - 1, new IntegerForLoop() {

					// Per-thread Variables.
					int colorCount = 1;
					boolean flag;

					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;

					public void run(int first, int last) {

						for (int i = first; i <= last; i++) {
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
				});
			}
		});
		long t2 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 1 "+(t2 - t1) + " msec");
		
		// Phase 2
		int tempColor = 0;
		int tempIndex = 0;
		Node tempNode = null;
		// Sorting
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
		for (int i = 0; i < nodes.length - 1; i++) {
			nodes[i].setColor(0);
		}

		tempColor = nodes[0].getColor();
		int loop = 0;
		start = 0;
		while (loop <= nodes.length - 1) {
			if ((nodes[loop].getColor() == tempColor)
					&& (loop != nodes.length - 1)) {
				loop++;
				continue;
			}
			end = loop;
			if(end-start>limit){
			new ParallelTeam().execute(new ParallelRegion() {
				public void run() throws Exception {
					execute(start, end, new IntegerForLoop() {

						// Per-thread Variables.
						int colorCount;
						boolean flag;

						// Extra padding.
						long p0, p1, p2, p3, p4, p5, p6, p7;
						long p8, p9, pa, pb, pc, pd, pe, pf;

						public void run(int first, int last) {
							for (int i = first; i <= last; i++) {
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

					});
				}
			});
			}
			else{
				boolean flag;
				int colorCount=1;
				for (int i = start; i <= end; i++) {
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
			if (loop != (nodes.length - 1)) {
				start = end + 1;
				tempColor = nodes[start].getColor();
			}
				loop++;
		}

		long t3 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 2 "+(t3 - t2) + " msec");
		
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
		System.out.println("Phase 3 "+(t4 - t3) + " msec");
		
		
		// Phase 4
		int numOfColor = 1;
		for (int i = 1; i < nodes.length; i++) {
			if (!nodes[i].getStatus()) {
				int color = 1;
				while (!nodes[i].validColor(color)) {
					color++;
				}
				nodes[i].setColor(color);
			}
			if (nodes[i].getColor() > numOfColor) {
				numOfColor = nodes[i].getColor();
			}
		}


		// Stop timing.
		long t5 = System.currentTimeMillis();
		// Printing run time.
		System.out.println("Phase 4 "+(t5 - t4) + " msec");
		
		System.out
		.println("No. of Colors used in after phase 4: " + numOfColor);
		
		// Printing run time.
		System.out.println("total running time "+(t5 - t1) + " msec");

	}

}
