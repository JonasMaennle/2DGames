package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;

import static helpers.Artist.*;

public class Laser implements Entity{
	
	private Image image;
	private float x, y, width, height, angle;
	private float velX;
	
	public Laser(float x, float y, float width, float height, String direction, int speed, String color, float angle)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		if(color.equals("red"))
			this.image = quickLoaderImage("player/laser_small_red");
		else if(color.equals("green"))
			this.image = quickLoaderImage("player/laser_small_green");
		
		if(direction.equals("right"))
			velX = speed;
		else
			velX = -speed;
	}
	
	public void update()
	{
		x += velX;
	}
	
	public void draw()
	{
		//drawQuadImageRot(image, x, y, width, height, angle);
		drawQuadImage(image, x, y, width, height);
	}
	
	public boolean isOutOfMap()
	{
		if(x - width < getLeftBoarder() || x > getRightBoarder())
			return true;
		return false;
	}

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return (int) width;
	}

	@Override
	public int getHeight() 
	{
		return (int) height;
	}

	@Override
	public void setWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

}
