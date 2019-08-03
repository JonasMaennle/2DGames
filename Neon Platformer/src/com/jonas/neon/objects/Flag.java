package com.jonas.neon.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Game;

public class Flag extends GameObject{
	
	private Texture tex = Game.getInstance();

	public Flag(float x, float y, ObjectId id) 
	{
		super(x, y, id);
	}

	public void tick(LinkedList<GameObject> object) 
	{

	}

	public void render(Graphics g) 
	{
		g.drawImage(tex.flag[0], (int)x, (int)y, 32, 96, null);
	}

	public Rectangle getBounds() 
	{
		return new Rectangle((int)x, (int)y, 32, 96);
	}
}
