package object;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.util.concurrent.CopyOnWriteArrayList;

import Enity.Entity;
import data.Handler;
import data.Tile;

public class EwokArcherEnemy extends Enemy{
	
	private Handler handler;
	private Entity currentEntity;
	private CopyOnWriteArrayList<Arrow> arrowList;
	private long time;

	public EwokArcherEnemy(float x, float y, int width, int height, Handler handler) 
	{
		super(x, y, width, height);
		this.handler = handler;
		this.currentEntity = handler.getCurrentEntity();
		this.arrowList = new CopyOnWriteArrayList<>();
		this.time = System.currentTimeMillis();
	}
	
	public void update()
	{
		currentEntity = handler.getCurrentEntity();
		
		// Shoot if possible
		if(System.currentTimeMillis() - time > 2000)
		{
			time = System.currentTimeMillis();
			
			if(x > currentEntity.getX() && x - currentEntity.getX() < 700)
			{
				// shoot if facing entity
				if(velX >= 0 && currentEntity.getX() > x)
					shoot();
				if(velX <= 0 && currentEntity.getX() < x)
					shoot();
			}

			if(x < currentEntity.getX() && x - currentEntity.getX() > - 700)
			{
				// shoot if facing entity
				if(velX >= 0 && currentEntity.getX() > x)
					shoot();
				if(velX <= 0 && currentEntity.getX() < x)
					shoot();
			}
		}
		
		for(Arrow a : arrowList)
		{
			a.update();
			
			for(Tile tile : handler.obstacleList)
			{
				if(!a.isStopped() && checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), a.getX(), a.getY(), a.getWidth(), a.getHeight()))
				{
					a.stop();
				}
			}
			
			if(handler.getCurrentEntity().getBounds().intersects(a.getBounds()))
			{
				if(a.getVelX() != 0)handler.getCurrentEntity().damage(10);
				arrowList.remove(a);
			}
			
			if(a.isDead())
				arrowList.remove(a);
		}
	}

	public void draw()
	{
		drawQuad(x, y, width, height);
		
		for(Arrow a : arrowList)
		{
			a.draw();
		}
	}
	
	private void shoot()
	{
		arrowList.add(new Arrow(x + width/2, y + 10, currentEntity.getX() - x, currentEntity.getY() - y, currentEntity));
	}
}
