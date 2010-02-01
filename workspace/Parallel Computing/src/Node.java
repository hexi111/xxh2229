import java.util.ArrayList;

public class Node
{
    private ArrayList<Node> neighbors;
    private long id;
    private int color;

    public Node(long id, int color)
    {
	this.id = id;
	this.color = color;
	neighbors = new ArrayList<Node>();
	System.out.printf("Created node: %d color: %d\n",id, color);
    }

    public void addNeighbor(Node n)
    {
	if(!neighbors.contains(n))
	    {
		neighbors.add(n);
		System.out.println("Node: " + this.id + " added neighbor " 
				   + n.getId());
	    }
    }

    public int getColor()
    {
	return this.color;
    }
    
    public long getId()
    {
	return this.id;
    }

    public boolean validColor(int color)
    {
	for(int i = 0; i<neighbors.size();i++)
	    {
		if(color == neighbors.get(i).getColor())
		    {
			return false;
		    }
	    }
	return true;
    }

    public void setColor(int color)
    {
	this.color = color;
    }

    public int getDegree()
    {
	return neighbors.size();
    }
}