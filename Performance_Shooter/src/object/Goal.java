package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Artist.*;

import Enity.Entity;
import data.Handler;

public class Goal implements Entity{

	private Image image;
	private Handler handler;
	private float x, y, width, height;
	
	public Goal(float x, float y, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		this.image = quickLoaderImage("objects/goal");
		this.handler = handler;
	}

	@Override
	public void update() 
	{
		if(checkCollision(handler.player.getX(), handler.player.getY(), handler.player.getWidth(), handler.player.getHeight(), x, y, width, height))
		{
			// Switch to next Level
			handler.getStatemanager().loadLevel();
		}
	}

	@Override
	public void draw() 
	{
		drawQuadImage(image, x, y, width, height);
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
