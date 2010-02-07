import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import edu.rit.pj.BarrierAction;
import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.reduction.IntegerOp;
import edu.rit.pj.reduction.SharedIntegerArray;


public class GraphColorSmp
	{
	
	static SharedIntegerArray nodes_color;
	static Node[] nodes;
	static final int DEFAULT_COLOR = 0;
    static int numNodes;
    static ArrayList<Integer> neighbours;
    
    
    /**
     * Prevent Construction.
     */
    public void GraphColorSmp()
    	{
    	
    	}
    
    public static boolean validColor(int color, Node n, int color_array[])
	    {
		for(int i = 0; i<n.getDegree();i++)
		    {
			if(color == color_array[n.getNeighbour(i)])
			    {
				return false;
			    }
		    }
		return true;
	    }
    
    /**
     * Main Program
     * 
     * @param args
     * @throws Exception
     */
    public static void main
		(String args[])
		throws Exception
		{
    	
    	// Start timing.
		long time = -System.currentTimeMillis();
		
		// Initialize middleware.
		Comm.init (args);
		
		//Check usage and try catch block needs to be implemented.
		
		BufferedReader reader = new BufferedReader
    	(new FileReader(args[0]));

		String line = "";
	
		//Read input file.
		while((line = reader.readLine()) != null)
	    	{
			if(line.charAt(0) == 'p')
			    {
				String[] values = line.split(" ");
				numNodes = Integer.parseInt(values[2]);		       	    
				//gc = new GraphColoringSmp(numNodes);
				
				//Creating nodes. //Ignore color for now.
				nodes = new Node[numNodes+1];
				for(int i=0;i<numNodes+1;i++)
					{
					nodes[i] = new Node(i, GraphColoringSeq.DEFAULT_COLOR);
					}
				
				//Set default colors.
			    nodes_color = new SharedIntegerArray(numNodes+1);
			    
			    }
	
			//e 1 2
			else if(line.charAt(0) == 'e')
			    {
				String[] values = line.split(" ");
				int n1 = Integer.parseInt(values[1]);
				int n2 = Integer.parseInt(values[2]);
				Node N1 = nodes[n1];
				Node N2 = nodes[n2];
				N1.addNeighbor(N2);
				N2.addNeighbor(N1);
			    }
			else{}
	    	}
    	
			//First coloring using First Fit Heuristic on Nodes sorted by degree.
	    	//Phase 1: (Have to make a function for coloring procedure.)
	    	new ParallelTeam().execute (new ParallelRegion()
			{
			public void run() throws Exception
				{
				execute (0, numNodes, new IntegerForLoop()
					{
					
				// Per-thread Variables.
					
					//Temporary arrangements.
					int colorCount;
					boolean flag;
		    		
					
					// Per Thread storage for counting number of zeroes.
					int[] thr_nodes_color = new int[numNodes+1];
					
					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;
					
					// Set up per-thread variables.
					public void start()
						{
						colorCount = 1;
						
						}
					
					public void run (int first, int last)
						{
						
						//colorGraph(first, last, thr_nodes);
						for(int i=first;i<=last;i++)
				    		{
							flag = true;
							
							for(int j=1;j<=colorCount;j++)
				    			{
				    			if(validColor(j, nodes[i], thr_nodes_color)){
				    				thr_nodes_color[i]= j;
				    				//System.out.println("thr_nodes["+i+"] color set to "+j);
				    				flag = false;
				    				j=colorCount+1;
				    			}
				    		}
				    		if(flag)
				    			{
				    			colorCount++;	
			    				thr_nodes_color[i]=colorCount;
				    			}
				    		}
						
						}
					
					public void finish()
						{
						//Reduce Method
						nodes_color.reduce(thr_nodes_color, IntegerOp.SUM);
						}
					});
				}
			});
		
	    	
	    	//Sort the array using colors in reverse order of its occurrence.
	    	
	    	
	    	
	    	
			
	    	// Stop timing.
			time += System.currentTimeMillis();
			
			//Print new output.
			
			for(int k=0;k<numNodes+1;k++)
				{
				System.out.println("Node No. :"+k+" Color: "+nodes_color.get(k));
				}
			
			//System.out.println("No. of Colors used in after phase 2: "+colorCount);
			
			
			// Printing run time.
			System.out.println(time + " msec");
			
	    	
		
		
			}

	}
