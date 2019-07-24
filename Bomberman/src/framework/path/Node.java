package framework.path;

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
	
	public int getX() {
		return x;
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
