package path;

import java.util.LinkedList;
import static core.Constants.*;

public class Graph {
	
	private LinkedList<Node> nodes;
	private int[][] matrix;
	private long t1,t2;
	public static int count = 0;
	static int count2 = 0;
	
	public Graph()
	{
		nodes = new LinkedList<Node>();
	}
	
	public void addBorder(int from, int to)
	{
		matrix[from][to] = 1;
		matrix[to][from] = 1;
	}
	
	public void addNode(Node node)
	{
		nodes.add(node);
	}
	
	public int getNodeID(int x, int y)
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			if(x == nodes.get(i).getX() && y == nodes.get(i).getY()) return i;
		}
		return -1;
	}
	
	public Node getNode(int x, int y)
	{
		for(int i = 0; i < nodes.size(); i++)
		{
			if(x == nodes.get(i).getX() && y == nodes.get(i).getY()) return nodes.get(i);
		}
		return null;
	}
	
	public void createMatrix()
	{
		t1 = System.currentTimeMillis();
		matrix = new int[nodes.size()][nodes.size()];
		for(int i = 0; i < nodes.size(); i++)
		{
			// finde heraus ob es nachbar nodes gibt
			/*	80 / 45 grid
			 *  	 [y-1]
			 *  [x-1] [N] [x+1]
			 *  	 [y+1]
			 */
			if(-1 != getNodeID(nodes.get(i).getX()+1, nodes.get(i).getY())){
				addBorder(i, getNodeID(nodes.get(i).getX()+1, nodes.get(i).getY()));
			}
			if(-1 != getNodeID(nodes.get(i).getX()-1, nodes.get(i).getY())){
				addBorder(i, getNodeID(nodes.get(i).getX()-1, nodes.get(i).getY()));
			}
			if(-1 != getNodeID(nodes.get(i).getX(), nodes.get(i).getY()+1)){
				addBorder(i, getNodeID(nodes.get(i).getX(), nodes.get(i).getY()+1));
			}
			if(-1 != getNodeID(nodes.get(i).getX(), nodes.get(i).getY()-1)){
				addBorder(i, getNodeID(nodes.get(i).getX(), nodes.get(i).getY()-1));
			}

//			for(int j = 0; j < nodes.size(); j++)
//			{
//				int absx = nodes.get(i).getX() - nodes.get(j).getX();
//				int absy = nodes.get(i).getY() - nodes.get(j).getY();
//				int abs = absx * absx + absy * absy;
//				count++;
//				if(abs == 1)
//				{
//					addBorder(i, j);
//				}				
//			}
		}
		t2 = System.currentTimeMillis();
		System.out.printf("Matrix loading time: %dms\n",(t2 - t1));
	}
	
	private int getNextNode()
	{
		int id = -1;
		int value = Integer.MAX_VALUE;
		for(int i = 0; i < nodes.size(); i++)
		{
			if(!nodes.get(i).isVisited() && nodes.get(i).getValue() < value)
			{
				id = i;
				value = nodes.get(i).getValue();
			}
		}
		return id;
	}
	
	public LinkedList<Node> astar(int startnode, int endnode)
	{
		int id, newDis;
		int maxValue = Integer.MAX_VALUE - (int)Math.sqrt(WIDTH * WIDTH + HEIGHT * HEIGHT);
		
		for(int i = 0; i < nodes.size(); i++)
		{
			int absx = nodes.get(i).getX() - nodes.get(endnode).getX();
			int absy = nodes.get(i).getY() - nodes.get(endnode).getY();
			nodes.get(i).setHeuristic((int)Math.sqrt(absx * absx + absy * absy));
			nodes.get(i).setVisited(false);
			nodes.get(i).setRange(maxValue);
		}
		//System.out.println(nodes.get(startnode).getX() + " " + nodes.get(startnode).getY() );
		nodes.get(startnode).setRange(0);
		outer : for(int i = 0; i < nodes.size(); i++)
		{
			id = getNextNode();
			if(id == -1) break;
			nodes.get(id).setVisited(true);
			for(int ab = 0; ab < nodes.size(); ab++)
			{	
				//System.out.print(matrix[ab][i]);
				if(!nodes.get(ab).isVisited() && matrix[ab][id] > 0)
				{
					newDis = nodes.get(id).getRange() + matrix[ab][id];
					if(newDis < nodes.get(ab).getRange())
					{
						nodes.get(ab).setRange(newDis);
						nodes.get(ab).setCmgfrom(id);
						if(ab == endnode) break outer;
					}
				}
			}
		}
		
		LinkedList<Node> way = new LinkedList<Node>();
		id = endnode;
		way.add(nodes.get(id));
		
		while(id != startnode)
		{
			id = nodes.get(id).getCmgfrom();
			way.add(nodes.get(id));
			//System.out.println("id: " + id);
			//System.out.println("startnode: " + startnode);
		}	
		
		for(int i = 0; i < way.size(); i++){
			//System.out.println(way.get(i).getX() + "\t" + way.get(i).getY());
		}
		
		return way;
	}
	
	public int countNodes()
	{
		return nodes.size();
	}
	
	public LinkedList<Node> getNodes()
	{
		return nodes;
	}
}