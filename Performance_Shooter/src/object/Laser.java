package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;

import static helpers.Artist.*;

public class Laser implements Entity{
	
	private Image image;
	private float x, y;
	private float velX;
	
	public Laser(float x, float y, String direction, int speed)
	{
		this.x = x;
		this.y = y;
		this.image = quickLoaderImage("player/laser_small");
		
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
		drawQuadImage(image, x, y, 30, 6);
	}

	@Override
	public float getX() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
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
