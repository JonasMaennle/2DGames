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
import com.jonas.neon.window.Handler;

public class Bullet extends GameObject{

	private float type, camX;
	private float speed, velY, speed2;
	private Handler handler;
	private GameObject temp, tempObject;
	private Camera cam;
	Texture tex = Game.getInstance();
	private float locAngle;
	private Rectangle expo;
	
	private float expoX, expoY;
	
	private boolean explosion;
	private Animation collision;
	private long t1, t2;

	public Bullet(float x, float y, float type, Handler handler, Camera cam, ObjectId id) 
	{
		super(x, y, id);
		this.type = type;
		this.speed = 18;
		this.handler = handler;		
		this.cam = cam;
		speed2 = 25;
		
		collision = new Animation(1, tex.explo[0],tex.explo[1],tex.explo[2],tex.explo[3],tex.explo[4],tex.explo[5],tex.explo[6],tex.explo[7],tex.explo[8],tex.explo[9],tex.explo[10],tex.explo[11],tex.explo[12],tex.explo[13],tex.explo[14],tex.explo[15],tex.explo[16],tex.explo[17],tex.explo[18],tex.explo[19],tex.explo[20],tex.explo[21]);
				
		if(type == 0)
		{
			locAngle = AT_ST_Walker.angle;
			locAngle = (float) Math.toDegrees(locAngle);
			
			if(AT_ST_Walker.facing == 1)
			{
				velX = 1;
			}else if(AT_ST_Walker.facing == -1)
			{
				velX = -1;
			}	
			velY = locAngle * -1;
			System.out.println(velY + " " + locAngle);
		}
	}

	public void tick(LinkedList<GameObject> object) 
	{
		// get Screen position
		camX = cam.getX();
		camX *= -1;
		
		if(type != 0)
		{
			// get bullet direction
			if(type == 1)
			{
				velX = 1;
			}else{
				velX = -1;
			}
			x += velX * speed;
		}else{	
			x += velX * speed2;
			y += velY * speed2;
		}	

		// check bounds
		if(x > camX + Game.WIDTH)
		{
			handler.removeObject(this);
		}else if(x < camX)
		{
			handler.removeObject(this);
		}
		
		collisionBlocks();
		collsionEnemy();
		
		collision.runAnimation();
	}
	
	private void collisionBlocks()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			temp = handler.object.get(i);
			
			if(temp.getID() == ObjectId.Block)
			{		
				if(type == 0)
				{
					if(getBounds().intersects(temp.getBounds()))
					{
						// explosion stuff
						t1 = System.currentTimeMillis();
						velX = 0;
						velY = 0;
						expoX = temp.getX();
						expoY = temp.getY();
						explosion = true;
						expo = new Rectangle((int)x - 32, (int)y - 32, 64, 64);
						
						// search for other blocks
						for(int x = 0; x < handler.object.size(); x++)
						{
							tempObject = handler.object.get(x);
							
							if(tempObject.getID() == ObjectId.Block)
							{
								if(tempObject.getBounds().intersects(expo))
								{
									handler.removeObject(tempObject);
								}
							}	
						}
						handler.removeObject(temp);
					}
				}else{
					if(getBounds().intersects(temp.getBounds()))
					{
						handler.removeObject(this);
					}
				}
			}
		}
	}
	
	private void collsionEnemy()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			temp = handler.object.get(i);
			
			if(temp.getID() == ObjectId.Enemy)
			{
				if(getBounds().intersects(temp.getBounds()))
				{	
					// reduce enemy healthbar
					Enemy e = (Enemy) temp;
					int hp = (int)e.getHealth();
					if(hp > 0)
					{
						if(type == 0)
						{
							hp -= 100;
							
							// explosion stuff
							t1 = System.currentTimeMillis();
							explosion = true;
							expoX = temp.getX();
							expoY = temp.getY();
							velX = 0;
							velY = 0;
							expo = new Rectangle((int)x - 32, (int)y - 32, 64, 64);
						}else{
							hp -= 25;
							handler.removeObject(this);
						}
						
						e.setHealth(hp);
					}
					
					e.setHit(true);
					e.setHitTime(System.currentTimeMillis());
					
					if(hp <= 0)
					{
						handler.removeObject(temp);
					}				
				}
			}
		}
	}

	public float getDestX() {
		return type;
	}

	public void setDestX(float type) {
		this.type = type;
	}

	public void render(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
		if(type != 0)
		{
			g.setColor(Color.red);
			g2d.drawImage(tex.weapon[2],(int)x, (int)y + 2, null);
		}else{
			if(!explosion)
			{
				if(AT_ST_Walker.facing == 1)
				{
					AffineTransform at_right = new AffineTransform();
					at_right.translate((int)x, (int)y);
					at_right.rotate(-locAngle);
					at_right.translate(-13, -3);				
					g2d.drawImage(tex.weapon[5], at_right, null);
				}else if(AT_ST_Walker.facing == -1)
				{
					AffineTransform at_right = new AffineTransform();
					at_right.translate((int)x, (int)y);
					at_right.rotate(locAngle);
					at_right.translate(-13, -3);				
					g2d.drawImage(tex.weapon[5], at_right, null);
				}
			}		
		}
		
		
		if(explosion)
		{
			if(animationCooldown())
			{
				t2 = System.currentTimeMillis();
				collision.drawAnimation(g, (int)expoX - 32, (int)expoY - 32, 128, 128);
			}else{
				handler.removeObject(this);
			}
		}
		
		// g2d.draw(getExplosionBounds());
	}
	
	private boolean animationCooldown()
	{
		if(t2 - t1 < 500)
		{
			return true;
		}else{
			return false;
		}
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 13, 3);
	}
	
	public Rectangle getExplosionBounds()
	{
		return new Rectangle((int)x - 32, (int)y - 32, 64, 64);
	}
	
	public float getType()
	{
		return type;
	}
}
