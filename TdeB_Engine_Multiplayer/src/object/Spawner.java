package object;

import java.io.Serializable;

public class Spawner implements Serializable{
	
	private static final long serialVersionUID = -3123394438513881537L;
	private int x, y;
	
	public Spawner(int x, int y) {
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
}
