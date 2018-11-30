package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;
import shader.Light;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;

public class Laser implements Entity{
	
	private Image image;
	private float x, y, width, height, angle, destX, destY, speed;
	private float velX, velY;
	private String color;
	private Light light;
	
	public Laser(float x, float y, float width, float height, String direction, int speed, String color, float angle)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		this.speed = speed;
		
		
		if(color.equals("red"))
		{
			this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 5, 0, 0, 1f);
			this.image = quickLoaderImage("player/laser_small_red");
		}else if(color.equals("green"))
		{
			this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 0, 5, 0, 1f);
			this.image = quickLoaderImage("player/laser_small_green");
		}

		
		if(direction.equals("right"))
			velX = 1;
		else
			velX = -1;
		
		lights.add(light);
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
		{
			this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 5, 0, 0, 4f);
			this.image = quickLoaderImage("player/laser_small_red");
		}else if(color.equals("green"))
		{
			this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 0, 5, 0, 4f);
			this.image = quickLoaderImage("player/laser_small_green");
		}
		lights.add(light);
		//System.out.println(light.getLocation().x);
		//System.out.println(x);
		calculateDirection();
	}
	
	public void update()
	{
		y += velY * speed;
		x += velX * speed;;
		
		light.setLocation(new Vector2f(x+ MOVEMENT_X + width/2, y + MOVEMENT_Y + 4));
	}
	
	public void draw()
	{
		drawQuadImageRot(image, x, y, width, height, angle);
	}
	
	public boolean isOutOfMap()
	{
		if(x - width < getLeftBorder()-500 || x > getRightBorder()+500)
		{
			return true;
		}

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
			if(destY < y)
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
	
	public void removeLight()
	{
		lights.remove(light);
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

	@Override
	public void damage(int amount) {
		// TODO Auto-generated method stub
		
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}
}
