import edu.rit.pj.Comm;
/**
 * Class GraphColoringSeq is sequential program to vertex color a graph.
 * It was designed and built as per requirements of Parallel Computing -1
 * Team Project in Department of Computer Science at RIT.
 * 
 * <P>
 * Usage: java GraphColoringSeq <I>input</I>
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
public class GraphColoringSeq {
	
	//ColorCount.
	static int colorCount = 1;

	/**
	 * Main Program
	 * 
	 * @param input file name
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		
		// Start timing.
		long t1 = System.currentTimeMillis();
		
		//Initialize middleware.
		Comm.init (args);
		
		//Make instance of GraphColoring class to perform necessary operation on graph.
		GraphColoring gc = new GraphColoring(args[0]);
		
		// Start timing for computation.
		long t2 = System.currentTimeMillis();
		
		//Color the graph.
		gc.color();
		
		//Calculate the number of colors used so far.
		gc.calculateColor();
		
		//Reorder the array of nodes as per color in reverse order.
		gc.permuteByColor();
		
		//Reset all nodes color.
		gc.cleanColor();
		
		//Color the graph.
		gc.color();
		
		//Calculate the number of colors used so far.
		gc.calculateColor();
		
		// Stop timing.
		long t3 = System.currentTimeMillis();
		
		//Print the number of colors used to color the graph.
		gc.printColor();
		
		// Printing run time.
		System.out.println("Computation time= "+ (t3-t2) + " msec");
		System.out.println("Total time= "+(t3-t1) + " msec");

	}
}