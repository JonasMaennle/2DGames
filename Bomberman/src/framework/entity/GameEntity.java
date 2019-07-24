package framework.entity;

import java.awt.Rectangle;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

public interface GameEntity extends Serializable {
	
	public void update();
	public void draw();
	
	public float getX();
	public float getY();
	public int getWidth();
	public int getHeight();
	public void setWidth(int width);
	public void setHeight(int height);
	public void setX(float x);
	public void setY(float y);
	
	public Vector2f[] getVertices();
	
	public Rectangle getBounds();
	public Rectangle getTopBounds();
	public Rectangle getBottomBounds();
	public Rectangle getLeftBounds();
	public Rectangle getRightBounds();
}
