import java.io.BufferedReader;
import java.io.FileReader;

public class GraphColoringSeq
{
    public static Node[] nodes;
    static final int DEFAULT_COLOR = 0;
    static int colorCount = 1;

    public GraphColoringSeq(int numNodes)
    {
	nodes = new Node[numNodes+1];
	nodes[0] = new Node(0, GraphColoringSeq.DEFAULT_COLOR);
	for(int i = 1; i<=numNodes; i++)
	    {
		nodes[i] = new Node(i, GraphColoringSeq.DEFAULT_COLOR);
	    }
    }

    public void colorGraph(int startNode)
    {
	
    }

    public static void main(String...args) throws Exception
    {
	int numNodes = 0;
	GraphColoringSeq gc;
	BufferedReader reader = new BufferedReader
	    (new FileReader(args[0]));
	String line = "";
	while((line = reader.readLine()) != null)
	    {
		if(line.charAt(0) == 'p')
		    {
			String[] values = line.split(" ");
			numNodes = Integer.parseInt(values[2]);		       	    
			gc = new GraphColoringSeq(numNodes);
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
	
	
	for(int i=1;i<=numNodes;i++)
		{
		boolean flag = true;
		for(int j=1;j<=colorCount;j++)
			{
			if(nodes[i].validColor(j)){
				nodes[i].setColor(j);
				flag = false;
				j=colorCount+1;
				System.out.println("Color of "+nodes[i].getId()+" set to "+j);
			}
		}
		if(flag)
			{
			colorCount++;	
			nodes[i].setColor(colorCount);
			System.out.println("Color of "+nodes[i].getId()+" set to "+colorCount);
			}
		
		}
	
	for(int i=1;i<=numNodes;i++)
		{
			System.out.println("Node No. :"+nodes[i].getId()+" Color: "+nodes[i].getColor());
		}
	
	System.out.println("No. of Colors used: "+colorCount);
	
    }
}