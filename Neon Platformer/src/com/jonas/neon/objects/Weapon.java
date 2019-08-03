package com.jonas.neon.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.jonas.neon.framework.GameObject;
import com.jonas.neon.framework.ObjectId;
import com.jonas.neon.framework.Texture;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.Handler;

public class Weapon extends GameObject{

	Texture tex = Game.getInstance();
	private GameObject temp;
	private Handler handler;
	private double destX, destY;
	
	public Weapon(float x, float y,Handler handler, ObjectId id) 
	{	
		super(x, y, id);
		this.handler = handler;
		getPlayerPosition();
	}

	public void tick(LinkedList<GameObject> object) 
	{
		destX = getDestX();
		destY = getDestY();
		getPlayerPosition();
	}
	
	private void getPlayerPosition()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			temp = handler.object.get(i);
			if(temp.getID() == ObjectId.Player)
			{
				x = temp.getX();
				y = temp.getY();
			}
		}
	}	

	public void render(Graphics g) 
	{	
		//AffineTransform at = AffineTransform.getTranslateInstance((int)x + 8, (int)y + 26);
		// Calc angle (player.y - target.y / target.x - player.x)
		// System.out.println("destX: " + destX);
		//hypo = (double) Math.sqrt(((y + 28) - destY) * ((y + 28) - destY)) + ((destX - x) * (destX - x));
		//double a = Math.sin(((y + 28) - destY) / hypo);
		//System.out.println(a);
		
//		if(Player.facing == 1)
//		{
//			if(destX < x + 50)
//				destX = x + 50;
//				
//		}else{
//			if(destX  < (x - 50))
//				destX = (x - 50);
//		}

		//System.out.println(x);
		//System.out.println(destX);
		
//		angle = ((y + 28) - destY) / (destX - x);
//		
//		angle *= 100;
//		angle %= 360;
		
		//System.out.println("Angle: " + angle);
//		if(Player.facing == 1)
//		{
//			if(angle > 85)
//				angle = 85;
//			if(angle < -25)
//				angle = -25;
			
//			at.rotate(Math.toDegrees(-a), tex.weapon[0].getWidth()/2,tex.weapon[0].getHeight()/2 );
//		}else{
//			
//			
//			at.rotate(Math.toRadians(-angle),tex.weapon[0].getWidth()/2,tex.weapon[0].getHeight()/2);
//		}
		
		
		Graphics2D g2d = (Graphics2D) g;

		if(Player.facing == 1)
		{
			//g2d.drawImage(tex.weapon[0], at, null);
			g2d.drawImage(tex.weapon[0], (int)x + 8, (int)y + 26, null);
		}else{
			//g2d.drawImage(tex.weapon[1], at, null);
			g2d.drawImage(tex.weapon[1], (int)x - 8, (int)y + 26, null);
		}
		
	}

	public double getDestX() {
		return destX;
	}

	public void setDestX(double destX) {
		this.destX = destX;
	}

	public double getDestY() {
		return destY;
	}

	public void setDestY(double destY) {
		this.destY = destY;
	}

	public Rectangle getBounds() 
	{
		return null;
	}
}
