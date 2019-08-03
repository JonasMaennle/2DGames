package com.jonas.neon.objects;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Game;

public class Collectable extends GameObject{
	
	private Texture tex = Game.getInstance();
	
	private int type;
	private float velY, max, min;

	public Collectable(float x, float y, int type, ObjectId id) {
		super(x, y, id);
		this.type = type;
		max = y + 20;
		min = y;
		velY = 0.2f;
	}
	
	// type 0 = crystal, type 1 = health

	public void tick(LinkedList<GameObject> object) 
	{
		if(type == 0 || type == 1)
		{
			if(y >= max)
			{
				velY = -0.2f;
			}
			if(y <= min)
			{
				velY = 0.2f;
			}
			
			y += velY;
		}
	}

	public void render(Graphics g) 
	{
		if(type == 0)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(tex.collectable[0],(int)x, (int)y, null);
		}
		
		if(type == 1)
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(tex.collectable[1],(int)x, (int)y, null);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x + 10, (int)y + 5, 25, 25);
	}

}
