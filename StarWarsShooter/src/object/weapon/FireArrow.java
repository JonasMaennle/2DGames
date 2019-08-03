package object.weapon;

import Enity.Entity;
import data.Handler;
import object.Tile;
import shader.Light;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import org.lwjgl.util.vector.Vector2f;

public class FireArrow extends Arrow{
	
	private Light light;
	private String direction;
	
	public FireArrow(float x, float y, float distanceX, float distanceY, float randSpeed, Entity entity, Handler handler, String direction)
	{
		super(x, y, distanceX, distanceY, randSpeed, entity, handler);
		this.direction = direction;
	}
	
	public void update() 
	{
		if(light != null)
		{
			if(direction.equals("left"))light.setLocation(new Vector2f(x + MOVEMENT_X + (1/angle)*1700, y + height + MOVEMENT_Y + angle/100f));
			if(direction.equals("right"))light.setLocation(new Vector2f(x + width + MOVEMENT_X - velY*speed*2, y + MOVEMENT_Y + height));
		}
		
		if(playSound)
		{
			sound.play();
			playSound = false;
		}
		
		velY += gravity;
			
		x += velX * speed;
		y += velY * speed;
			
		if(dynamic)super.calcAngle(destX, destY);
		
		if(stopped)
		{
			if(System.currentTimeMillis() - despawnTimer > 3000)
			{
				dead = true;
			}
		}
		
		for(Tile tile : handler.obstacleList)
		{
			if(!this.isStopped() && checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), this.getX(), this.getY(), this.getWidth(), this.getHeight()))
			{
				stop();
				if(light != null)lights.remove(light);
			}
		}
	}

	public void draw() 
	{
		drawQuadImageRot(image, x, y, width, height, angle);
	}
	
	public void removeLight()
	{
		if(light != null)
			lights.remove(light);
	}
	
	public void setLight(Light light)
	{
		this.light = light;
		lights.add(this.light);
	}
}
