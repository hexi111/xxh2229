import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import edu.rit.pj.BarrierAction;
import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.reduction.IntegerOp;
import edu.rit.pj.reduction.SharedBooleanArray;
import edu.rit.pj.reduction.SharedIntegerArray;


public class GraphColorSmp
	{
	
	static SharedIntegerArray nodes_color;
	static Node[] nodes;
	static final int DEFAULT_COLOR = 0;
    static int numNodes;
    static SharedBooleanArray conflict_array;
    
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
				    				thr_nodes_color[nodes[i].getId()]= j;
				    				//System.out.println("thr_nodes["+i+"] color set to "+j);
				    				flag = false;
				    				j=colorCount+1;
				    			}
				    		}
				    		if(flag)
				    			{
				    			colorCount++;	
			    				thr_nodes_color[nodes[i].getId()]=colorCount;
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
	    	int colorNum=0;
	    	//Update colors in node objects.
	    	for(int i=0;i<numNodes+1;i++)
	    		{
	    		if(nodes_color.get(i)>colorNum)
	    			{
	    			colorNum=nodes_color.get(i);
	    			}
	    		nodes[i].setColor(nodes_color.get(i));
	    		}
	    	

			System.out.println("No. of Colors used in after phase 1: "+colorNum);
	    	
	    	//Sort the nodes array as per color.
	    	Arrays.sort(nodes);
	    	
	    	//Get starting and ending sequence of color class.
	    	int colorIndex[] =  new int[(colorNum*2)];
	    	int ka = 0;
	    	boolean flag=true;
	    	int i=nodes[0].getColor(); //Check if error shows up in logic.
	    	for(int j=0;j<=numNodes;j++)
	    		{
		    		if(nodes[j].getColor()==i&&flag)
		    			{
		    			colorIndex[ka] = j;ka++;
		    			flag=false;
		    			}
		    		if(nodes[j].getColor()==i-1&&!flag)
		    			{
		    			colorIndex[ka] = j-1;ka++;
		    			colorIndex[ka] = j;ka++;
		    			i=nodes[j].getColor();
		    			}
	    		}
	    	colorIndex[ka]=numNodes;
	    	
	    	
	    	/*Test print
	    	for(int k=0;k<numNodes+1;k++)
				{
	    		System.out.println("Index No. "+k+" Node No. :"+nodes[k].getId()+" Color: "+nodes[k].getColor());
				}
	    	for(int k=0;k<ka;k+=2)
	    		{
		    	System.out.println("Start index: "+colorIndex[k]+", End index: "+colorIndex[k+1]);	
	    		}
	    	Test print ends*/
	    	
	    	//Reset the all nodes color to zero.
	    	for(int k=0;k<numNodes+1;k++)
		    	{
		    		nodes[k].setColor(0); //USE default_color initialized above.
		    		nodes_color.set(k, 0);
		    		//System.out.println("Node No. :"+nodes[k].getId()+" Recolored to : "+nodes[k].getColor());
		    	}
	    	
	    	//Phase 2 of Algorithm.
	    	//Iteration over each color class.
	    	
	    	for(int k=0;k<ka;k+=2)
	    		{
	    		final int start = colorIndex[k]; //Not sure if this is right.
	    		final int end = colorIndex[k+1];
	    		
	    		//Process each color class in parallel.
	    		//colorGraph(colorIndex[k], colorIndex[k+1]);
	    	
	    		new ParallelTeam().execute (new ParallelRegion()
				{
				public void run() throws Exception
					{
					execute (start, end, new IntegerForLoop()
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
							for(int i=0;i<numNodes+1;i++)
								{
								thr_nodes_color[i] = nodes_color.get(i);
								}
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
					    				thr_nodes_color[nodes[i].getId()]= j;
					    				//System.out.println("thr_nodes["+i+"] color set to "+j);
					    				flag = false;
					    				j=colorCount+1;
					    			}
					    		}
					    		if(flag)
					    			{
					    			colorCount++;	
				    				thr_nodes_color[nodes[i].getId()]=colorCount;
					    			}
					    		}
							
							}
						
						public void finish()
							{
							//Reduce Method
							for(int i=0;i<numNodes+1;i++)
								{
								nodes_color.set(i, thr_nodes_color[i]);	
								}
							//nodes_color.reduce(thr_nodes_color, IntegerOp.SUM);
							}
						});
						}
					});
	    		
	    		}
	    		
	    	//Find conflicts in parallel.
	    	conflict_array = new SharedBooleanArray(numNodes+1);
    		
	    	for(int k=0;k<numNodes+1;k++)
	    		{
	    		conflict_array.set(k, false);
	    		}
	    	
	    	
	    	new ParallelTeam().execute (new ParallelRegion()
			{
			public void run() throws Exception
				{
				execute (0, numNodes, new IntegerForLoop()
					{
					
				// Per-thread Variables.
					int thr_nodes_color[] = new int[numNodes+1];
					
					// Extra padding.
					long p0, p1, p2, p3, p4, p5, p6, p7;
					long p8, p9, pa, pb, pc, pd, pe, pf;
					
					public void start()
					{
					for(int i=0;i<numNodes+1;i++)
						{
						thr_nodes_color[i] = nodes_color.get(i);
						}
					}
					
					public void run (int first, int last)
						{
						
						//colorGraph(first, last, thr_nodes);
						for(int i=first;i<=last;i++)
				    		{
							int color = nodes_color.get(nodes[i].getId());
							if(validColor(color, nodes[i], thr_nodes_color))
								{
								conflict_array.set(nodes[i].getId(), true);
								}
				    		
				    		}
						
						}
					
					});
					}
				});
    		
	    	
	    	
	    	
	    	//Resolve conflicts. If possible resolve for order of highest/ lowest degree.
	    	
			
	    	// Stop timing.
			time += System.currentTimeMillis();
			
			//Print new output.
			
			for(int k=0;k<numNodes+1;k++)
				{
				System.out.println("Node No. :"+k+" Color: "+nodes_color.get(k));
				}
			
			//Count the number of colors used.
			colorNum=0;
			for(int k=0;k<numNodes+1;k++)
	    		{
	    		if(nodes_color.get(k)>colorNum)
	    			{
	    			colorNum=nodes_color.get(k);
	    			}
	    		}
			
			System.out.println("No. of Colors used in after phase 2: "+colorNum);
			
			
			
			// Printing run time.
			System.out.println(time + " msec");
			
	    	
		
		
			}

	}
