package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;

import static helpers.Graphics.*;
import static helpers.Setup.*;

public class Laser implements Entity{
	
	private Image image;
	private float x, y, width, height, angle, destX, destY, speed;
	private float velX, velY;
	private String color;
	
	public Laser(float x, float y, float width, float height, String direction, int speed, String color, float angle)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.speed = speed;
		if(color.equals("red"))
			this.image = quickLoaderImage("player/laser_small_red");
		else if(color.equals("green"))
			this.image = quickLoaderImage("player/laser_small_green");
		
		if(direction.equals("right"))
			velX = 1;
		else
			velX = -1;
	}
	
	public Laser(float x, float y, float destX, float destY, float width, float height, int speed, String color, float angle)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.speed = speed;
		this.destX = destX;
		this.destY = destY;
		this.color = color;
		if(color.equals("red"))
			this.image = quickLoaderImage("player/laser_small_red");
		else if(color.equals("green"))
			this.image = quickLoaderImage("player/laser_small_green");
		
		calculateDirection();
	}
	
	public void update()
	{
		y += velY * speed;
		x += velX * speed;;
	}
	
	
	
	public void draw()
	{
		drawQuadImageRot(image, x, y, width, height, angle);
		//drawQuadImage(image, x, y, width, height);
	}
	
	public boolean isOutOfMap()
	{
		if(x - width < getLeftBoarder()-500 || x > getRightBoarder()+500)
			return true;
		return false;
	}
	
	private void calculateDirection()
	{
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(destX - x);
		float yDistanceFromTarget = Math.abs(destY - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		velX = xPercentOfMovement;
		velY = totalAllowedMovement - xPercentOfMovement;
		
		// set direction based on position of target relative to tower
		if(color.equals("red"))
		{
			if(destY > y)
				velY *= -1;
		}
		if(color.equals("green"))
		{
			if(destY < y)
				velY *= -1;
		}
		
		if(destX < x)
			velX *= -1;	
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

	@Override
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}
}
