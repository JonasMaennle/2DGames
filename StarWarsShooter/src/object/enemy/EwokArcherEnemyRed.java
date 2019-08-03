package object.enemy;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import data.Handler;
import object.weapon.Arrow;
import object.weapon.FireArrow;
import shader.Light;


public class EwokArcherEnemyRed extends EwokArcherEnemy{

	private FireArrow tempArrow;
	private CopyOnWriteArrayList<FireArrow> arrowList;
	
	public EwokArcherEnemyRed(float x, float y, int width, int height, boolean gravity, Handler handler) 
	{
		super(x, y, width, height, gravity, handler);
		
		this.arrowList = new CopyOnWriteArrayList<>();
		
		this.walk_left = new Animation(loadSpriteSheet("enemy/ewok_walk_left_red", 48, 80), 50);
		this.walk_right = new Animation(loadSpriteSheet("enemy/ewok_walk_right_red", 48, 80), 50);
		
		this.idle_left = quickLoaderImage("enemy/ewok_idle_left_red");
		this.idle_right = quickLoaderImage("enemy/ewok_idle_right_red");
	}
	
	public void update()
	{
		velY = gravity;
		
		destX = handler.getCurrentEntity().getX() - x;
		destY = handler.getCurrentEntity().getY() - y;
		
		if(gravity == 0)
			randSpeed = rand.nextFloat()+1;
		else
			randSpeed = rand.nextFloat();
		
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
		
		for(FireArrow a : arrowList)
		{
			a.update();
			
			if(handler.getCurrentEntity().getBounds().intersects(a.getBounds()))
			{
				destX = a.getDistanceX();
				destY = a.getDistanceY();
				randSpeed = a.getRandSpeed();
				
				if(a.getVelX() != 0)handler.getCurrentEntity().damage(25);
				a.removeLight();
				arrowList.remove(a);

			}
			
			if(a.isDead())
			{
				a.removeLight();
				arrowList.remove(a);
			}

		}
		
		updateBounds();
		super.mapCollision();
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
		
		if(y - handler.getCurrentEntity().getY() > - 400 && y - handler.getCurrentEntity().getY() < 400)
		{
			if(x > handler.getCurrentEntity().getX() && x - handler.getCurrentEntity().getX() < 800 || x < handler.getCurrentEntity().getX() && x - handler.getCurrentEntity().getX() > - 800)
			{
				if(!sound.playing())
					sound.play();
				
				if(direction.equals("left"))
				{
					drawAnimation(bow_left, x - 10, y + 10, 32, 48);

					if(bow_left.getFrame() == 0)
					{
						isShooting = false;
						tempArrow = new FireArrow(x  - 22, y + 18, destX, destY, randSpeed, handler.getCurrentEntity(), handler, "left");
					}
					if(bow_left.getFrame() == 5 && !isShooting)
					{		
						isShooting = true;
						tempArrow.setLight(new Light(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y), 5, 1, 0, 0.9f));
						arrowList.add(tempArrow);
					}else{
						if(tempArrow != null)tempArrow.setX(tempArrow.getX() + bow_left.getFrame()*0.1f);
					}

				}else{
					drawAnimation(bow_right, x + 30, y + 10, 32, 48);

					if(bow_right.getFrame() == 0)
					{
						isShooting = false;
						tempArrow = new FireArrow(x  + 44, y + 18, destX, destY, randSpeed, handler.getCurrentEntity(), handler, "right");
					}
					if(bow_right.getFrame() == 5 && !isShooting)
					{		
						isShooting = true;
						tempArrow.setLight(new Light(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y), 5, 1, 0, 0.9f));
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
	
	@Override
	public void damage(float amount)
	{
		if(health > 0)
		{
			health -= amount;
			if(health <= 0)
			{
				sound.stop();
				for(FireArrow a : arrowList)
				{
					a.removeLight();
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
