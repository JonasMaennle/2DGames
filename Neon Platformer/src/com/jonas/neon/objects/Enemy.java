package com.jonas.neon.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Animation;
import com.jonas.neon.window.Camera;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.Handler;

public class Enemy extends GameObject{
	
	Texture tex = Game.getInstance();
	private Camera cam;
	private Handler handler;
	private Player player;
	private AT_ST_Walker walker;
	
	private int type;
	private float gravity;
	private float velX, velY;
	private float maxX, minX;
	private float speed;
	private float health;
	public boolean hit = false;
	private long hitTime;
	private long currentTime;
	private int facing;
	private long t1, t2;
	private float height = 80;
	
	private Animation enemyWalkRight;
	private Animation enemyWalkLeft;
	private Animation enemyDead;
	private Animation enemyHitLeft;
	private Animation enemyHitRight;
	
	public Enemy(float x, float y,int type,int range, Handler handler, Camera cam, ObjectId id) {
		super(x, y, id);
		this.handler = handler;
		this.type = type;
		this.cam = cam;
		gravity = 1;
		velY = 5;
		velX = 1;
		maxX = (x + (range * 32));
		minX = (x - (range * 32));
		speed = (float) ((Math.random() * 3) + 0.2);
		health = 100;
		falling = true;
		
		enemyWalkLeft = new Animation(5, tex.enemy[0],tex.enemy[1],tex.enemy[2],tex.enemy[3]);
		enemyWalkRight = new Animation(5, tex.enemy[10],tex.enemy[9],tex.enemy[8],tex.enemy[7]);
		enemyDead = new Animation(10, tex.enemy[5]);
		enemyHitLeft = new Animation(5, tex.enemy[4],tex.enemy[4],tex.enemy[4],tex.enemy[4],tex.enemy[4]);
		enemyHitRight = new Animation(5, tex.enemy[6],tex.enemy[6],tex.enemy[6],tex.enemy[6],tex.enemy[6]);	
	}

	public void tick(LinkedList<GameObject> object) 
	{
		// movement for Gungans
		if(type == 0)
		{
			if(falling)
			{
				velY = 3;
			}else if(!falling){
				velY = 0;
				System.out.println("hier");
			}
			
			y += velY * gravity;
			
			if(x <= minX)
			{
				velX = 1;
				facing = 1;
			}else if(x >= maxX)
			{
				velX = -1;
				facing = -1;
			}
			
			x += velX * speed;
			
			shoot();
		}
		collision(object);
		enemyDead.runAnimation();
		enemyHitLeft.runAnimation();
		enemyHitRight.runAnimation();
		enemyWalkLeft.runAnimation();
		enemyWalkRight.runAnimation();
	}

	public void render(Graphics g) 
	{
		// hit animation
		if(velX > 0)
		{
			if(hit)
			{
				enemyHitRight.drawAnimation(g, (int)x, (int)y, 48, 80);
			}else{
				enemyWalkRight.drawAnimation(g, (int)x, (int)y, 48, 80);
			}
			
		}else
		{
			if(hit)
			{
				enemyHitLeft.drawAnimation(g, (int)x, (int)y, 48, 80);
			}else{
				enemyWalkLeft.drawAnimation(g, (int)x, (int)y, 48, 80);
			}		
		}
		
		currentTime = System.currentTimeMillis();
		if(currentTime - hitTime > 150)
		{
			hit = false;
		}	
		
		// health bar
		if(health >= 100)
		{
			g.setColor(Color.green);
		}else if(health >= 75)
		{
			g.setColor(new Color(241,244,66));
		}else if (health >= 50)
		{
			g.setColor(new Color(244,191,66));
		}else if(health >= 25)
		{
			g.setColor(new Color(244,80,66));
		}
		
		g.fillRect((int)x, (int)y - 15, (int)(health / 2.5), 7);
		g.setColor(Color.black);
		g.drawRect((int)x, (int)y - 15, 40, 7);
		
		//Graphics2D g2d = (Graphics2D) g;
		// draw bullet
		//g.setColor(Color.blue);
		//g2d.draw(getBoundsLeft());
		//g2d.draw(getBoundsRight());
		//g2d.draw(getTextBulletBounds());
	}
	
	private void collision(LinkedList<GameObject> object)
	{
		if(type == 0)
		{
			for(int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
				
				if(tempObject.getID() == ObjectId.Block)
				{
					// bottom collision
					if(getBounds().intersects(tempObject.getBounds()))
					{
						y = tempObject.getY() - height;
						velY = 0;
						falling = false;
					}else{
						falling = true;
					}
					// right collision
					if(getBoundsRight().intersects(tempObject.getBounds()))
					{
						velX *= -1;
					}
					// left collision
					if(getBoundsLeft().intersects(tempObject.getBounds()))
					{
						velX *= -1;
					}
				}
			}
		}	
	}
	
	@SuppressWarnings("static-access")
	private void shoot()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
				
			// shoot the player
			if(tempObject.getID() == ObjectId.Player)
			{
				player = (Player) tempObject;
				t1 = System.currentTimeMillis();
				if(facing == 1 && player.getX() > x && player.getY() - y > -60 && player.getY() - y < 60)
				{
					if(player.getX() - x < 600 && shootingCooldown(500))
					{
						t2 = System.currentTimeMillis();
						handler.addObject(new Enemy_Bullet(x, y+30, 1, handler, cam, ObjectId.Enemy_Bullet));
					}
				}else if(facing == -1 && player.getX() < x && player.getY() - y > -60 && player.getY() - y < 60)
				{
					if(player.getX() - x > -600 && shootingCooldown(500))
					{
						t2 = System.currentTimeMillis();
						handler.addObject(new Enemy_Bullet(x, y+30, -1, handler, cam, ObjectId.Enemy_Bullet));
					}
				}
			}
			
			// shoot the at_st
			if(tempObject.getID() == ObjectId.AT_ST)
			{
				walker = (AT_ST_Walker) tempObject;
				t1 = System.currentTimeMillis();
				if(walker.enabled)
				{
					if(facing == 1 && walker.getX() > x && walker.getY() - y > -220 && walker.getY() - y < 50)
					{
						if(walker.getX()-50 - x < 700 && shootingCooldown(500))
						{
							t2 = System.currentTimeMillis();
							handler.addObject(new Enemy_Bullet(x, y+30, 1, handler, cam, ObjectId.Enemy_Bullet));
						}
					}else if(facing == -1 && walker.getX() < x && walker.getY() - y > -220 && walker.getY() - y < 50)
					{
						if(walker.getX()+50 - x > -700 && shootingCooldown(500))
						{
							t2 = System.currentTimeMillis();
							handler.addObject(new Enemy_Bullet(x, y+30, -1, handler, cam, ObjectId.Enemy_Bullet));
						}
					}
				}		
			}
		}	
	}
	
	private boolean shootingCooldown(int value)
	{
		if(t1 - t2 > value)
		{
			return true;
		}else{
			return false;
		}
	}

	public long getHitTime() {
		return hitTime;
	}

	public void setHitTime(long hitTime) {
		this.hitTime = hitTime;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	public Rectangle getBoundsRight() 
	{
		return new Rectangle((int)x+40, (int)y, 10, 75);
	}
	
	public Rectangle getBoundsLeft() 
	{
		return new Rectangle((int)x - 10, (int)y, 10, 75);
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 40, 80);
	}
}
