package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;
import static helpers.Graphics.*;
import static helpers.Setup.getLeftBorder;
import static helpers.Setup.getRightBorder;

public class Arrow implements Entity{

	private float x, y, velX, velY, speed, gravity;
	private int width, height;
	private Image image_left, image_right; 
	
	public Arrow(float x, float y, float distanceX, float distanceY)
	{
		this.x = x;
		this.y = y;
		
		this.speed = 3.0f;
		this.width = 32;
		this.height = 16;
		
		this.image_left = quickLoaderImage("enemy/Ewok_Arrow_left");
		this.image_right = quickLoaderImage("enemy/Ewok_Arrow_right");
		
		
		System.out.println(distanceY); // 2 = 704 * x
		this.velX = distanceX * 0.004f;
		this.velY = -2;
		this.gravity = 0.04f;
	}
	
	public void update() 
	{
		velY += gravity;
		
		x += velX * speed;
		y += velY * speed;
	}

	public void draw() 
	{
		drawQuadImage(image_right, x, y, width, height);
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}

	@Override
	public void damage(int amount) {
		// TODO Auto-generated method stub
		
	}

	public boolean isOutOfMap()
	{
		if(x - width < getLeftBorder()-100 || x > getRightBorder()+100)
			return true;
		return false;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}
}
