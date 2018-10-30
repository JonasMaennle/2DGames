package object;

import static helpers.Artist.drawQuadTex;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;

import data.Entity;

public class Cloud implements Entity{
	
	private float x, y;
	private int width, height;
	private Random rand;
	private Texture texture;
	private float speed;

	public Cloud(float x, float y)
	{
		this.rand = new Random();
		this.x = x;
		this.y = y + rand.nextInt(150);
		this.width = width + rand.nextInt(100) + 320;
		this.height = height + 160;
		this.texture = quickLoad("Cloud");
		this.speed = rand.nextFloat() + 0.1f;
	}
	
	public void update() 
	{
		x -= (speed * 0.5f);
	}

	public void draw() 
	{
		drawQuadTex(texture, x, y, width, height);
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
		return width;
	}

	@Override
	public int getHeight() 
	{
		return height;
	}

	@Override
	public void setWidth(int width) 
	{
		this.width = width;
	}

	@Override
	public void setHeight(int height) 
	{
		this.height = height;
	}

	@Override
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) 
	{
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

}
