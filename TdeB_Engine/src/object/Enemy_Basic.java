package object;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Entity.GameEntity;

import static core.Constants.MOVEMENT_X;
import static core.Constants.MOVEMENT_Y;
import static helper.Graphics.*;

public class Enemy_Basic implements GameEntity{

	private float x, y;
	private int width, height;
	private Image image;
	
	public Enemy_Basic(float x, float y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = quickLoaderImage("Enemy/Enemy_tmp");
	}
	
	public void update() {
		//System.out.println("enemy x: " + x + " enemy y: " + y);
	}

	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}

	@Override
	public float getX() {
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
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2f[] getVertices() {
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}
}
