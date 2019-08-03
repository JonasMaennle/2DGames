package com.jonas.neon.window;

import com.jonas.neon.framework.GameObject;

public class Camera{
	
	private float x, y;
	
	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject player)
	{
		float xTarg = -player.getX() + Game.WIDTH/2;
		x += ((xTarg - x) * 0.1);
			
		float yTarg = -player.getY() + Game.HEIGHT/2;
		y += ((yTarg - y) * 0.5);
	}
	
	public void tickAT_ST(GameObject at_st)
	{
		float xTarg = -at_st.getX() + Game.WIDTH/2;
		x += ((xTarg - x) * 0.1);
			
		float yTarg = -at_st.getY() + Game.HEIGHT/2;
		y += ((yTarg - y) * 0.5);
	}

	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}
