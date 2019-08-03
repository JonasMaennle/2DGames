package com.jonas.neon.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Animation;
import com.jonas.neon.window.Camera;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.HUD;
import com.jonas.neon.window.Handler;

public class AT_ST_Walker extends GameObject{
	
	private Texture tex = Game.getInstance();
	private Handler handler;
	private Block b;
	private Collectable c;
	@SuppressWarnings("unused")
	private Camera cam;
	
	private float width = 150, height = 250;
	
	private Animation idle_left;
	private Animation idle_right;
	
	private Animation walk_left;
	private Animation walk_right;
	
	private float gravity = 0.5f;
	private final float MAX_SPEED = 10;
	public static int facing = 1;
	public static boolean isMoving;
	public static boolean enabled = false;
	private Player player;
	public static float angle = 0.0f;
	public static float velAngle = 0.0f;
	public static float health = 120;
	public static boolean toggleEntry;
	
	private boolean movement = false;

	public AT_ST_Walker(float x, float y, Handler handler, Camera cam, ObjectId id) 
	{
		super(x, y, id);
		this.handler = handler;
		this.cam = cam;
		
		idle_left = new Animation(8, tex.at_st[4], tex.at_st[5], tex.at_st[6], tex.at_st[7]);
		idle_right = new Animation(8, tex.at_st[0], tex.at_st[1], tex.at_st[2], tex.at_st[3]);
		
		walk_left = new Animation(4, tex.at_st[8], tex.at_st[9], tex.at_st[10], tex.at_st[11], tex.at_st[12], tex.at_st[13], tex.at_st[14], tex.at_st[15], tex.at_st[16], tex.at_st[17], tex.at_st[18], tex.at_st[19]);
		walk_right = new Animation(4, tex.at_st[20], tex.at_st[21], tex.at_st[22], tex.at_st[23], tex.at_st[24], tex.at_st[25], tex.at_st[26], tex.at_st[27], tex.at_st[28], tex.at_st[29], tex.at_st[30], tex.at_st[31]);
	}

	public void tick(LinkedList<GameObject> object) 
	{
		x += velX;
		y += velY;
		
		if(enabled)
		{
			// super sexy smooth movement
			if(handler.isLeft())
			{
				velX = -3;
				movement = true;
			}else if(!handler.isLeft() && !handler.isRight()){
				velX = 0;
			}
			
			if(handler.isRight())
			{
				velX = 3;
				movement = true;
			}else if(!handler.isRight() && !handler.isLeft()){
				velX = 0;
			}
			
			if(velX < 0) 
			{
				facing = -1;
			}
			if(velX > 0)
			{
				facing = 1;	
			}
			if(angle < 0.014 && angle > -0.014)
			{
				angle += velAngle;
			}else if(angle > 0.014){
				angle -= 0.001;
			}else if(angle < -0.014)
			{
				angle += 0.001;
			}
			// check hp status
			if(health <= 0)
			{
				respawn();
			}
			
			
		}else{
			velX = 0;
		}
		
		if(falling || jumping)
		{
			velY += gravity;
			
			if(velY > MAX_SPEED)
			{
				velY = MAX_SPEED;
			}
		}
		
		// falling to death
		if(y > Game.HEIGHT + 500)
		{
			respawn();
		}
		
		collision(object);
		walk_left.runAnimation();
		walk_right.runAnimation();
		idle_left.runAnimation();
		idle_right.runAnimation();
	}
	
	private void collision(LinkedList<GameObject> object)
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ObjectId.Block)
			{
				b = (Block) tempObject;
				
				// lava collision //
				if(b.getType() == 2)
				{
					if(getBounds().intersects(b.getBounds()))
					{
						
					}		
				}
									
				// map collision //
				
				// top collision
				if(getBoundsTop().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() - 160;
					velX = 0;
				}
				// bottom collision
				if(getBoundsBottom().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() - height;
					velY = 0;
					falling = false;
					jumping = false;

				}else{
					falling = true;
				}
				
				// right collision
				if(getBoundsRight().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() - width + 20;
					movement = false;
				}
				
				// left collision
				if(getBoundsLeft().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX();
					movement = false;
				}
				
			}else if(tempObject.getID() == ObjectId.Flag) // flag
			{
				// switch level
				if(getBounds().intersects(tempObject.getBounds()))
				{
					Game.LEVEL++;
					handler.switchLevel();
				}				
			}else if(tempObject.getID() == ObjectId.Enemy) // enemy
			{				
				if(getBounds().intersects(tempObject.getBounds()))
				{
					// lose health
				}
			}else if(tempObject.getID() == ObjectId.Collectable) // all collectables
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					c = (Collectable) tempObject;
					if(c.getType() == 0)
					{
						handler.removeObject(tempObject);
						HUD.bullets += 10;
					}else if(c.getType() == 1)
					{
						handler.removeObject(tempObject);
						HUD.health++;
					}
					
				}
			}else if(tempObject.getID() == ObjectId.Enemy_Bullet) // Enemy bullet
			{
				// top collision
				if(getBoundsTop().intersects(tempObject.getBounds()))
				{
					handler.removeObject(tempObject);
				}
				// bottom collision
				if(getBoundsBottom().intersects(tempObject.getBounds()))
				{
					handler.removeObject(tempObject);
				}
			}else if(tempObject.getID() == ObjectId.Player)
			{
				player = (Player) tempObject;
				
				if(getBoundsBottom().intersects(player.getBigBounds()) && toggleEntry)
				{
					handler.removeObject(tempObject);
					for(int x = 0; x < handler.object.size(); x++)
					{
						 tempObject = handler.object.get(x);
						if(tempObject.getID() == ObjectId.Weapon)
						{
							handler.removeObject(tempObject);
						}
						enabled = true;
					}
				}
			}
		}
	}

	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		
		if(velX != 0)
		{
			if(facing == 1)
			{
				//g.drawImage(tex.weapon[3],(int)x + 125, (int)y + 72,48 , 32, null);
				AffineTransform at_right = new AffineTransform();
				at_right.translate((int)x + 120, (int)y + 80);
				at_right.rotate(Math.toDegrees(-angle));
				at_right.translate(-1, -16);				
				g2d.drawImage(tex.weapon[3], at_right, null);
				
				walk_right.drawAnimation(g, (int)x, (int)y, 150, 250);
				
			}else if(facing == -1)
			{
				//g.drawImage(tex.weapon[4],(int)x - 10, (int)y + 72,48 , 32, null);	
				AffineTransform at_left = new AffineTransform();
				at_left.translate((int)x + 35, (int)y + 80);
				at_left.rotate(Math.toDegrees(angle));
				at_left.translate(-48, -16);				
				g2d.drawImage(tex.weapon[4], at_left, null);
				
				walk_left.drawAnimation(g, (int)x, (int)y, 150, 250);
			}
		}else{
			if(facing == 1)
			{		
				AffineTransform at_right = new AffineTransform();
				at_right.translate((int)x + 120, (int)y + 80);
				at_right.rotate(Math.toDegrees(-angle));
				at_right.translate(-1, -16);				
				g2d.drawImage(tex.weapon[3], at_right, null);
				// g.drawImage(tex.weapon[3],(int)x + 125, (int)y + 72,48 , 32, null);
				idle_right.drawAnimation(g, (int)x, (int)y, 150, 250);
				
			}else if(facing == -1)
			{
				AffineTransform at_left = new AffineTransform();
				at_left.translate((int)x + 35, (int)y + 80);
				at_left.rotate(Math.toDegrees(angle));
				at_left.translate(-48, -16);				
				g2d.drawImage(tex.weapon[4], at_left, null);
		        //g.drawImage(tex.weapon[4],(int)x - 10, (int)y + 72, 48 , 32, null);	
				idle_left.drawAnimation(g, (int)x, (int)y, 150, 250);
			}
		}

		if(enabled)
		{
			g.setColor(Color.green);
			g.fillRect((int)x + 15, (int)y-20, (int)health, 10);
			g.setColor(Color.black);
			g.drawRect((int)x + 15, (int)y-20, 120, 10);
		}	
		// Bounds
//		g.setColor(Color.blue);
//		g2d.draw(getBoundsTop());
//		g2d.draw(getBoundsBottom());
//		g.setColor(Color.red);
//		g2d.draw(getBoundsRight());
//		g2d.draw(getBoundsLeft());
	}
	
	public void respawn()
	{
		enabled = false;
		handler.switchLevel();	
		HUD.health--;
	}

	public Rectangle getBoundsTop() 
	{
		return new Rectangle((int)x + 10, (int)y, 150, 120);
	}
	
	public Rectangle getBoundsBottom() 
	{
		return new Rectangle((int)x + 50, (int)y+240, 70, 10);
	}
	
	public Rectangle getBoundsRight() 
	{
		return new Rectangle((int)x + 120, (int)y+120, 10, 120);
	}
	
	public Rectangle getBoundsLeft() 
	{
		return new Rectangle((int)x + 30, (int)y+120, 10, 120);
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 150, 250);
	}
	
	public boolean getMovement() {
		return movement;
	}
	
//	private void dropPlayer()
//	{
//		handler.addObject(new Player((int)this.getX() + 30, (int)this.getY() + 120, ObjectId.Player, handler, cam));
//		handler.addObject(new Weapon(0, 0, handler, ObjectId.Weapon));
//		AT_ST_Walker.enabled = false;
//	}	
}
