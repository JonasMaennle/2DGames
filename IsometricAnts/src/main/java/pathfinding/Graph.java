package pathfinding;

import core.Boot;

import java.util.LinkedList;

public class Graph {
	
	private LinkedList<Node> nodes;
	private int[][] matrix;
	private long t1,t2;

	private int heuristicDirectMove = 2;
	private int heuristicDiagonalMove = 3;
	
	public Graph(){
		nodes = new LinkedList<Node>();
	}
	
	public void addBorder(int from, int to, int heuristic){
		matrix[from][to] = heuristic;
		matrix[to][from] = heuristic;
	}
	
	public void addNode(Node node){
		nodes.add(node);
	}

	public void removeNode(Node node){
		nodes.remove(node);
	}
	
	public int getNodeID(int x, int y){
		for(int i = 0; i < nodes.size(); i++){
			if(x == nodes.get(i).getX() && y == nodes.get(i).getY()) return i;
		}
		return -1;
	}
	
	public Node getNode(int x, int y){
		for(int i = 0; i < nodes.size(); i++){
			if(x == nodes.get(i).getX() && y == nodes.get(i).getY()) return nodes.get(i);
		}
		return null;
	}
	
	public void createMatrix(){
		t1 = System.currentTimeMillis();
		matrix = new int[nodes.size()][nodes.size()];
		for(int i = 0; i < nodes.size(); i++){

            // core
			int rightID =   getNodeID(nodes.get(i).getX() + 1, nodes.get(i).getY());
			int leftID =    getNodeID(nodes.get(i).getX() - 1, nodes.get(i).getY());
			int bottomID =  getNodeID(nodes.get(i).getX(), nodes.get(i).getY() + 1);
			int topID =     getNodeID(nodes.get(i).getX(), nodes.get(i).getY() - 1);

			if(rightID != -1)
			    addBorder(i, rightID, heuristicDirectMove);
            if(leftID != -1)
                addBorder(i, leftID, heuristicDirectMove);
            if(topID != -1)
                addBorder(i, topID, heuristicDirectMove);
            if(bottomID != -1)
                addBorder(i, bottomID, heuristicDirectMove);


            // corners
            int rightBottomID = getNodeID(nodes.get(i).getX() + 1, nodes.get(i).getY() + 1);
            int rightTopID =    getNodeID(nodes.get(i).getX() + 1, nodes.get(i).getY() - 1);
            int leftBottomID =  getNodeID(nodes.get(i).getX() - 1, nodes.get(i).getY() + 1);
            int leftTopID =     getNodeID(nodes.get(i).getX() - 1, nodes.get(i).getY() - 1);

            if(rightID != -1 && bottomID != -1 && rightBottomID != -1)
                addBorder(i, rightBottomID, heuristicDiagonalMove);
            if(rightID != -1 && topID != -1 && rightTopID != -1)
                addBorder(i, rightTopID, heuristicDiagonalMove);
            if(leftID != -1 && bottomID != -1 && leftBottomID != -1)
                addBorder(i, leftBottomID, heuristicDiagonalMove);
            if(leftID != -1 && topID != -1 && leftTopID != -1)
                addBorder(i, leftTopID, heuristicDiagonalMove);


		}
		t2 = System.currentTimeMillis();
		System.out.printf("Nodes: " + nodes.size() + "\tMatrix loading time: %dms\n",(t2 - t1));
	}
	
	private int getNextNode(){
		int id = -1;
		int value = Integer.MAX_VALUE;
		// suche nach node mit geringstem nächten wert (abstand zur startnode)
		for(int i = 0; i < nodes.size(); i++){
			if(!nodes.get(i).isVisited() && nodes.get(i).getValue() < value){
				id = i;
				value = nodes.get(i).getValue();
			}
		}
		return id;
	}
	
	public synchronized LinkedList<Node> astar(int startnode, int endnode){
		int id, newDis;
		int maxValue = Integer.MAX_VALUE - (int)Math.sqrt(Boot.INSTANCE.getScreenWidth() * Boot.INSTANCE.getScreenWidth() + Boot.INSTANCE.getScreenHeight() * Boot.INSTANCE.getScreenHeight());

		// set max range (screen width and height to every node)
		for(int i = 0; i < nodes.size(); i++){
			try {
				int absx = nodes.get(i).getX() - nodes.get(endnode).getX();
				int absy = nodes.get(i).getY() - nodes.get(endnode).getY();
				nodes.get(i).setHeuristic((int)Math.sqrt(absx * absx + absy * absy));
				nodes.get(i).setVisited(false);
				nodes.get(i).setRange(maxValue);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("ERROR in PathfindingThread: " + startnode + "   " + endnode);
				return new LinkedList<Node>();
			}
		}
		//System.out.println(nodes.get(startnode).getX() + " " + nodes.get(startnode).getY() );
		nodes.get(startnode).setRange(0);
		for(int i = 0; i < nodes.size(); i++)
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
						if(ab == endnode) break;
					}
				}
			}
		}
		
		LinkedList<Node> way = new LinkedList<Node>();
		id = endnode;
		way.add(nodes.get(id));
		
		while(id != startnode){
			id = nodes.get(id).getCmgfrom();
			way.add(nodes.get(id));
		}
		return way;
	}
}