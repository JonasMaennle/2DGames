package object.weapon;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;
import static helpers.Graphics.*;

public class MapCollectable implements Entity{

	private int x, y, width, height;
	private Image left;
	private String name;
	
	public MapCollectable(int x, int y, int width, int height, String name, Image left)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		
		this.left = left;
	}
	
	public void update() 
	{
		
	}

	@Override
	public void draw() 
	{
		drawQuadImage(left, x, y, width, height);
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
	public float getVelX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVelY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOutOfMap() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
