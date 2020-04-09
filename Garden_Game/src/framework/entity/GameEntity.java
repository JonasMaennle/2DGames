package framework.entity;

import java.awt.*;

import org.lwjgl.util.vector.Vector2f;

public interface GameEntity {
	
	public void update();
	public void draw();
	
	public float getX();
	public float getY();
	public int getWidth();
	public int getHeight();
	
	public Vector2f[] getVertices();
	
	public Polygon getBounds();
	public Rectangle getTopBounds();
	public Rectangle getBottomBounds();
	public Rectangle getLeftBounds();
	public Rectangle getRightBounds();
}
