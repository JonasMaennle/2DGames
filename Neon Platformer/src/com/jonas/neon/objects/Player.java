package com.jonas.neon.objects;

import java.awt.Color;
import java.awt.Graphics;
// import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Animation;
import com.jonas.neon.window.Camera;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.HUD;
import com.jonas.neon.window.Handler;

public class Player extends GameObject{
	
	private float width = 48, height = 96;
	
	private float gravity = 0.5f;
	private final float MAX_SPEED = 10;
	
	private Handler handler;
	@SuppressWarnings("unused")
	private Camera cam;
	private int spawnX, spawnY;
	private boolean isMoving = false;
	private boolean movement = false;
	
	Texture tex = Game.getInstance();
	private Block b;
	private Collectable c;
	
	private Animation playerWalkRight;
	private Animation playerWalkLeft;
	private Animation playerJumpLeft;
	private Animation playerJumpRight;
	
	public static int facing = 1;
	// 1 = right, -1 = left

	public Player(float x, float y, ObjectId id, Handler handler, Camera cam) 
	{
		super(x, y, id);
		this.handler = handler;
		this.cam = cam;
		
		spawnX = (int) x;
		spawnY = (int) y;
		
		playerWalkRight = new Animation(6, tex.player[8],tex.player[9],tex.player[10],tex.player[11],tex.player[12],tex.player[13],tex.player[14],tex.player[15]);
		playerWalkLeft = new Animation(6, tex.player[0],tex.player[1],tex.player[2],tex.player[3],tex.player[4],tex.player[5],tex.player[6],tex.player[7]);
		playerJumpLeft = new Animation(10, tex.player[16]);
		playerJumpRight = new Animation(10, tex.player[17]);
	}

	public void tick(LinkedList<GameObject> object) 
	{	
		x += velX;
		y += velY;

		// super sexy smooth movement
		if(handler.isLeft())
		{
			velX = -5;
			movement = true;
		}else if(!handler.isLeft() && !handler.isRight()){
			velX = 0;
		}
		
		if(handler.isRight())
		{
			velX = 5;
			movement = true;
		}else if(!handler.isRight() && !handler.isLeft()){
			velX = 0;
		}
		
		if(velX < 0) facing = -1;
		if(velX > 0) facing = 1;
		
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
			HUD.health--;	
		}
		
		// Reset to start if menu button is pressed
//		if(Game.state == Game.STATE.MENU)
//		{
//			handler.removeObject(this);
//		}
		
		collision(object);
		playerWalkRight.runAnimation();
		playerWalkLeft.runAnimation();
		playerJumpLeft.runAnimation();
		playerJumpRight.runAnimation();
	}
	
	private void collision(LinkedList<GameObject> object)
	{
		isMoving = false;
		
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
						respawn();
						HUD.health--;
					}		
				}
									
				// map collision //
				
				// top collision
				if(getBoundsTop().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() + 32;
					velY = 0;
				}
				// bottom collision
				if(getBounds().intersects(tempObject.getBounds()))
				{
					y = tempObject.getY() - height;
					velY = 0;
					falling = false;
					jumping = false;
					
					if(tempObject.getVelX() != 0 && b.getType() == 7)
					{
						isMoving = true;	
					}
				}else{
					falling = true;
				}
				// left collision
				if(getBoundsLeft().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() + 35;
					movement = false;
				}
				// right collision
				if(getBoundsRight().intersects(tempObject.getBounds()))
				{
					x = tempObject.getX() - width - 2;
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
					respawn();
					HUD.health--;
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
					respawn();
					HUD.health--;
				}
				// bottom collision
				if(getBounds().intersects(tempObject.getBounds()))
				{
					handler.removeObject(tempObject);
					respawn();
					HUD.health--;
				}
				// left collision
				if(getBoundsLeft().intersects(tempObject.getBounds()))
				{
					handler.removeObject(tempObject);
					respawn();
					HUD.health--;
				}
				// right collision
				if(getBoundsRight().intersects(tempObject.getBounds()))
				{
					handler.removeObject(tempObject);
					respawn();
					HUD.health--;
				}
			}
		}
	}

	public void render(Graphics g) 
	{		
		g.setColor(new Color(230, 92, 0));
		if(jumping)
		{
			if(facing == 1)
			{
				g.drawImage(tex.player[17],(int)x, (int)y,48 , 96, null);
			}else{
				g.drawImage(tex.player[16],(int)x, (int)y,48 , 96, null);
			}
		}else{
			if(velX != 0 && !isMoving)
			{
				if(facing == 1)
				{
					playerWalkRight.drawAnimation(g, (int)x, (int)y, 48, 96);
				}else{
					playerWalkLeft.drawAnimation(g, (int)x, (int)y, 48, 96);
				}
					
			}else{
				if(facing == 1)
				{
					g.drawImage(tex.player[15],(int)x, (int)y,48 , 96, null);
				}else{
					g.drawImage(tex.player[0],(int)x, (int)y,48 , 96, null);
				}			
			}
		}
		
		if(isMoving)
		{
			if(facing == 1)
			{
				g.drawImage(tex.player[8],(int)x, (int)y,48 , 96, null);
			}else{
				g.drawImage(tex.player[0],(int)x, (int)y,48 , 96, null);
			}	
		}
		
		
		// Draw collsion boxes
//		Graphics2D g2d = (Graphics2D) g;
//		g.setColor(Color.red);
//		g2d.draw(getBounds());
//		g2d.draw(getBoundsRight());
//		g2d.draw(getBoundsLeft());
//		g2d.draw(getBoundsTop());
	}
	
	public void respawn()
	{
		x = spawnX;
		y = spawnY;
		handler.switchLevel();
		HUD.bullets = HUD.START_BULLETS;
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x + (int)(width / 2)-(int)((width / 2) / 2), (int)y + (int)(height / 2), (int)width / 2, (int)height / 2);
	}
	
	public Rectangle getBoundsTop() 
	{
		return new Rectangle((int)x + (int)(width / 2)-(int)((width / 2) / 2), (int)y, (int)width / 2, (int)height / 2);
	}
	
	public Rectangle getBoundsRight() 
	{
		return new Rectangle((int)x + (int)(width - 5), (int)y + 5, (int)5, (int)height - 10);
	}
	
	public Rectangle getBoundsLeft() 
	{
		return new Rectangle((int)x, (int)y + 5, (int)5, (int)height - 10);
	}
	
	public Rectangle getBigBounds() 
	{
		return new Rectangle((int)x, (int)y, 48, 96);
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	
	public boolean getMovement() {
		return movement;
	}
	
}
