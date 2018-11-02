package object;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Artist.*;

import data.Entity;

public class Cloud implements Entity{
	
	private float x, y;
	private int width, height;
	private Image image;

	public Cloud(float x, float y)
	{
		this.x = x;
		this.y = y;
		this.width = 800;
		this.height = 800;
		this.image = quickLoaderImage("Cloud");
	}
	
	public void update() 
	{
		if(x < Display.getX() - image.getWidth())
		{
			x = Display.getX() + WIDTH;
		}else{
			x -= 0.3;
		}
	}

	public void draw() 
	{
		drawQuadImageStatic(image, x, y, width, height);
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
