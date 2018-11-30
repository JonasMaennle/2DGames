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
			
			
			shoot();
		}
		
		
		for(Arrow a : arrowList)
		{
			a.update();
			if(checkCollision(a.getX(), a.getY(), a.getWidth(), a.getHeight(), currentEntity.getX(), currentEntity.getY(), currentEntity.getWidth(), currentEntity.getHeight()));
			{
				// damage player
			}
			
			for(Tile t : handler.obstacleList)
			{
				if(checkCollision(a.getX(), a.getY(), a.getWidth(), a.getHeight(), t.getX(), t.getY(), t.getWidth(), t.getHeight()));
				{
					a.stop();
				}
			}
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
		arrowList.add(new Arrow(x, y, currentEntity.getX() - x, currentEntity.getY() - y, currentEntity));
	}
}
