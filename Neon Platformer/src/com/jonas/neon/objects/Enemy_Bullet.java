package com.jonas.neon.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.window.Camera;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.Handler;

public class Enemy_Bullet extends GameObject{
	private float x,y;
	private float speed;
	private float velX;
	private float camX, type;
	private Handler handler;
	private AT_ST_Walker at_st;
	private Camera cam;
	
	public Enemy_Bullet(float x, float y, float type, Handler handler, Camera cam, ObjectId id)
	{
		super(x, y, id);
		this.x = x;
		this.y = y;
		speed = 5;
		this.handler = handler;
		this.cam = cam;
		this.type = type;
	}

	public void tick(LinkedList<GameObject> object) 
	{
		// get Screen position
		camX = cam.getX();
		camX *= -1;
		
		// get bullet direction
		if(type == 1)
		{
			velX = 1;
		}else{
			velX = -1;
		}
		x += velX * speed;
			

		// check bounds
		if(x > camX + Game.WIDTH)
		{
			handler.removeObject(this);
		}else if(x < camX)
		{
			handler.removeObject(this);
		}	
		collision(object);
	}

	public void render(Graphics g) 
	{
		g.setColor(Color.BLUE);
		g.fillOval((int)x, (int) y, 8, 8);
	}
	
	@SuppressWarnings("static-access")
	private void collision(LinkedList<GameObject> object)
	{

		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
				
			if(tempObject.getID() == ObjectId.Block)
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					handler.removeObject(this);
				}
			}
			
			if(tempObject.getID() == ObjectId.AT_ST)
			{
				at_st = (AT_ST_Walker) tempObject;
				if(at_st.enabled)
				{
					if(getBounds().intersects(at_st.getBoundsTop()) || getBounds().intersects(at_st.getBoundsRight()) || getBounds().intersects(at_st.getBoundsLeft()))
					{
						if(AT_ST_Walker.health >= 0)
						{
							AT_ST_Walker.health -= 10;
						}
						if(AT_ST_Walker.health <= 0)
						{
							handler.addObject(new Player(at_st.getX() + 30, at_st.getY() + 120, ObjectId.Player, handler, cam));
							handler.addObject(new Weapon(0, 0, handler, ObjectId.Weapon));
							AT_ST_Walker.enabled = false;
							handler.removeObject(at_st);
						}
						handler.removeObject(this);			
					}
				}			
			}
		}		
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 8, 8);
	}
}
