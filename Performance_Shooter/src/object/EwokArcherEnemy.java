package object;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import Enity.Entity;
import data.Handler;
import data.Tile;

public class EwokArcherEnemy extends Enemy{
	
	private Handler handler;
	private Entity currentEntity;
	private CopyOnWriteArrayList<Arrow> arrowList;
	private Animation walk_right, walk_left, bow_left, bow_right;
	private Image idle_left, idle_right, bow_left_idle, bow_right_idle;
	private String direction;
	private Arrow tempArrow;
	private boolean isShooting;

	public EwokArcherEnemy(float x, float y, int width, int height, boolean gravity, Handler handler) 
	{
		super(x, y, width, height);
		this.handler = handler;
		this.currentEntity = handler.getCurrentEntity();
		this.arrowList = new CopyOnWriteArrayList<>();
		if(gravity)
		{
			this.gravity = 4;
		}else{
			this.gravity = 0;
		}
		this.speed = 2;
		this.direction = "left";
		this.isShooting = false;
		this.health = width -8;
		
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		
		this.walk_left = new Animation(loadSpriteSheet("enemy/ewok_walk_left", 48, 80), 50);
		this.walk_right = new Animation(loadSpriteSheet("enemy/ewok_walk_right", 48, 80), 50);
		
		this.bow_left = new Animation(loadSpriteSheet("enemy/bow_left", 32, 32), 200);
		this.bow_right = new Animation(loadSpriteSheet("enemy/bow_right", 32, 32), 200);
		
		this.idle_left = quickLoaderImage("enemy/ewok_idle_left");
		this.idle_right = quickLoaderImage("enemy/ewok_idle_right");
		
		this.bow_left_idle = quickLoaderImage("enemy/bow_left_idle");
		this.bow_right_idle = quickLoaderImage("enemy/bow_right_idle");
	}
	
	public void update()
	{
		velY = gravity;
		currentEntity = handler.getCurrentEntity();
		
		if(handler.getCurrentEntity().getX() >= x)
		{
			direction = "right";
		}
		if(handler.getCurrentEntity().getX() < x)
		{
			direction = "left";
		}
		
		x += velX * speed;
		y += velY * speed;
		
		for(Arrow a : arrowList)
		{
			a.update();
			
			if(handler.getCurrentEntity().getBounds().intersects(a.getBounds()))
			{
				if(a.getVelX() != 0)handler.getCurrentEntity().damage(10);
				arrowList.remove(a);
			}
			
			if(a.isDead())
				arrowList.remove(a);
		}
		
		updateBounds();
		mapCollision();
	}

	public void draw()
	{
		//drawQuad(x, y, width, height);
		// draw ewok
		switch ((int)velX) {
		case -1:
			drawAnimation(walk_left, x, y, width, height);
			break;
		case 1:
			drawAnimation(walk_right, x, y, width, height);
			break;
		case 0:
			if(direction.equals("left"))
				drawQuadImage(idle_left, x, y, 64, 128);
			else
				drawQuadImage(idle_right, x, y, 64, 128);
			break;
		default:
			break;
		}
		

		// draw bow
		if(tempArrow != null)tempArrow.draw();
		
		if(currentEntity == null)
			return;
		if(y - handler.getCurrentEntity().getY() > - 400 && y - handler.getCurrentEntity().getY() < 400)
		{
			if(x > currentEntity.getX() && x - currentEntity.getX() < 700 || x < currentEntity.getX() && x - currentEntity.getX() > - 700)
			{
				if(direction.equals("left"))
				{
					drawAnimation(bow_left, x - 10, y + 10, 32, 48);

					if(bow_left.getFrame() == 0)
					{
						isShooting = false;
						tempArrow = new Arrow(x  - 22, y + 18, currentEntity.getX() - x, currentEntity.getY() - y, currentEntity, handler);
					}
					if(bow_left.getFrame() == 5 && !isShooting)
					{		
						isShooting = true;
						arrowList.add(tempArrow);
					}else{
						if(tempArrow != null)tempArrow.setX(tempArrow.getX() + bow_left.getFrame()*0.1f);
					}

				}else{
					drawAnimation(bow_right, x + 30, y + 10, 32, 48);

					if(bow_right.getFrame() == 0)
					{
						isShooting = false;
						tempArrow = new Arrow(x  + 44, y + 18, currentEntity.getX() - x, currentEntity.getY() - y, currentEntity, handler);
					}
					if(bow_right.getFrame() == 5 && !isShooting)
					{		
						isShooting = true;
						arrowList.add(tempArrow);
					}else{
						if(tempArrow != null)tempArrow.setX(tempArrow.getX() - bow_right.getFrame()*0.1f);
					}
				}
			}else{
				if(direction.equals("left"))
				{
					drawQuadImage(bow_left_idle, x - 10, y + 10, 32, 48);
				}else{
					drawQuadImage(bow_right_idle, x + 30, y + 10, 32, 48);
				}
			}
		}else{
			if(direction.equals("left"))
			{
				drawQuadImage(bow_left_idle, x - 10, y + 10, 32, 48);
			}else{
				drawQuadImage(bow_right_idle, x + 30, y + 10, 32, 48);
			}
		}
		
		// draw health bar
		drawQuadImage(healthBackground, x + 4, y - 5, width - 8, 6);
		drawQuadImage(healthForeground, x + 4, y - 5, health, 6);
		drawQuadImage(healthBorder, x + 4, y - 5, width - 8, 6);
		
		for(Arrow a : arrowList)
		{
			a.draw();
		}
	}
	
	private void mapCollision()
	{
		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);

			if(r.intersects(rectLeft))
			{
				direction = "right";
				velX = 1;
				x = (float) ((float) r.getX() + r.getWidth() + 1);
			}
			if(r.intersects(rectRight))
			{
				direction = "left";
				velX = -1;
				x = (float) (r.getX() - width - 1);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}
	
	@Override
	public void damage(float amount)
	{
		if(health > 0)
		{
			health -= amount;
			if(health <= 0)
			{
				for(Arrow a : arrowList)
				{
					projectileList.add(a);
				}
				handler.enemyList.remove(this); 
			}
		}
	}
//	
//	private void shoot()
//	{
//		arrowList.add(new Arrow(x  - 10, y + 20, currentEntity.getX() - x, currentEntity.getY() - y, currentEntity));
//	}
}
