package pathfinding;

public class Node {
	
	private int x;
	private int y;
	private int heuristic;
	private int range;
	private int cmgfrom;
	private boolean visited;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}

	// copy constructor
	public Node(Node copy) {
	    this.x = copy.getX();
	    this.y = copy.getY();
	    this.heuristic = copy.getHeuristic();
	    this.range = copy.getRange();
	    this.cmgfrom = copy.getCmgfrom();
	    this.visited = copy.isVisited();
    }
	
	public int getX() {
		return x;
	}

	public String toString() {
		return "Node x: " + x + ", y: " + y;
	}

	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getHeuristic() {
		return heuristic;
	}
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getCmgfrom() {
		return cmgfrom;
	}
	public void setCmgfrom(int cmgfrom) {
		this.cmgfrom = cmgfrom;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public int getValue(){
		return range + heuristic;
	}
}
