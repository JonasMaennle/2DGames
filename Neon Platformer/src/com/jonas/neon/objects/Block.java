package com.jonas.neon.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Animation;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.Handler;

public class Block extends GameObject{

	Texture tex = Game.getInstance();
	private int type;
	private float maxX, minX, velX;
	
	private Animation lavaTop;
	private Animation lavaBlock;
	private Handler handler;
	private Player player;
	
	public Block(float x, float y, int type, Handler handler, ObjectId id) 
	{
		super(x, y, id);	
		this.type = type;
		this.handler = handler;
		
		this.minX = x - (32 * 3);
		this.maxX = x + (32 * 3);
		this.velX = 1;
		
		lavaTop = new Animation(5, tex.lava[0],tex.lava[1],tex.lava[2],tex.lava[3]);
		lavaBlock = new Animation(2, tex.lava[4],tex.lava[5],tex.lava[6],tex.lava[7]);
	}

	public void tick(LinkedList<GameObject> object) 
	{
		// movable block
		if(type == 7)
		{
			if(x >= maxX)
			{
				velX = -1;
			}else if(x <= minX)
			{
				velX = 1;
			}
			
			x += velX;
		}
		//System.out.println("block: " + velX);
		lavaTop.runAnimation();
		lavaBlock.runAnimation();
		collision(object);
	}

	public void render(Graphics g) 
	{
		// dirt block
		if(type == 0)
		{
			g.drawImage(tex.block[0], (int)x, (int)y, null);
		}
		// grass block
		if(type == 1)
		{
			g.drawImage(tex.block[1], (int)x, (int)y, null);
		}
		// lava top
		if(type == 2)
		{
			lavaTop.drawAnimation(g, (int)x, (int)y, 32, 32);
		}
		// lava block
		if(type == 3)
		{
			lavaBlock.drawAnimation(g, (int)x, (int)y, 32, 32);
		}
		// grass left
		if(type == 4)
		{
			g.drawImage(tex.block[4], (int)x, (int)y, null);
		}
		// grass right
		if(type == 5)
		{
			g.drawImage(tex.block[5], (int)x, (int)y, null);
		}
		// Grass single
		if(type == 6)
		{
			g.drawImage(tex.block[6], (int)x, (int)y, null);
		}
		// grass movable
		if(type == 7)
		{
			g.drawImage(tex.block[6], (int)x, (int)y, null);
		}
		// stone
		if(type == 8)
		{
			g.drawImage(tex.block[7], (int)x, (int)y, null);
		}
	}
	
	private void collision(LinkedList<GameObject> object)
	{
		if(type == 7)
		{
			for(int i = 0; i < handler.object.size(); i++)
			{
				GameObject temp = handler.object.get(i);
				if(temp.getID() == ObjectId.Player)
				{
					player = (Player) temp;
					Rectangle tempRec = new Rectangle((int)x, (int)y - 1, 32, 1);
					if(player.getBigBounds().intersects(tempRec))
					{
						player.setVelX(velX);
						player.setMoving(true);
						
						if(handler.isLeft())
						{
							player.setX(player.getX()-4);
							player.setMoving(false);
						}else if(handler.isRight())
						{
							player.setX(player.getX()+4);
							player.setMoving(false);
						}
					}		
				}
			}
		}	
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public int getType()
	{
		return type;
	}
	
	public float getVelX()
	{
		return velX;
	}
}
