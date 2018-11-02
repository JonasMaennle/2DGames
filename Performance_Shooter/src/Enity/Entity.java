package Enity;

import org.lwjgl.util.vector.Vector2f;

public interface Entity {
	
	public float getX();
	public float getY();
	public int getWidth();
	public int getHeight();
	public void setWidth(int width);
	public void setHeight(int height);
	public void setX(float x);
	public void setY(float y);
	public void update();
	public void draw();
	public Vector2f[] getVertices();
}
