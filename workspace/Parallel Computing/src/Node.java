import java.util.ArrayList;

public class Node implements Comparable<Node>, Cloneable
{
    private ArrayList<Node> neighbors;
    private int id;
    private int color;
  
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
    
    public Node(int id, int color)
    {
	this.id = id;
	this.color = color;
	neighbors = new ArrayList<Node>();
	//System.out.printf("Created node: %d color: %d\n",id, color);
    }

    public void addNeighbor(Node n)
    {
	if(!neighbors.contains(n))
	    {
		neighbors.add(n);
		//System.out.println("Node: " + this.id + " added neighbor " 
			//	   + n.getId());
	    }
    }

    public int getColor()
    {
	return this.color;
    }
    
    public int getId()
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
    
    public int getNeighbour(int index){
    	return neighbors.get(index).getId();
        	
    }
    
    /**
     * Reverse ordering comparison based on colors.
     */
    public int compareTo(Node o1)
    {
    	Node b = (Node)o1;
    	if(this.getColor() < b.getColor())
    	{
    		return 1;
    	}
    	else if(this.getColor() > b.getColor())
    	{
    		return -1;
    	}
    	else
    	{
    		return 0;
    	}
    }

}
