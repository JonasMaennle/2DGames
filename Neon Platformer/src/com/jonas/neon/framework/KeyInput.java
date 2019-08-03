package com.jonas.neon.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.jonas.neon.objects.AT_ST_Walker;
import com.jonas.neon.objects.Bullet;
import com.jonas.neon.objects.Player;
import com.jonas.neon.objects.Weapon;
import com.jonas.neon.window.Camera;
import com.jonas.neon.window.Game;
import com.jonas.neon.window.HUD;
import com.jonas.neon.window.Handler;

public class KeyInput extends KeyAdapter{
	
	private Handler handler;
	private Camera cam;
	private Sound sound;
	private boolean shootReady = true;
	private long t1, t2;
	
	public KeyInput(Camera cam, Handler handler, Sound sound)
	{
		this.handler = handler;
		this.cam = cam;
		this.sound = sound;
	}
	
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();	
		if(Game.state == Game.STATE.GAME)
		{
			// player movement
			for(int i = 0; i < handler.object.size(); i++)
			{
				GameObject tempObject = handler.object.get(i);
			
				if(tempObject.getID() == ObjectId.Player)
				{
					if(code == KeyEvent.VK_A) handler.setLeft(true);
					if(code == KeyEvent.VK_D) handler.setRight(true);
					
					if(code == KeyEvent.VK_SPACE && !tempObject.isJumping())
					{
						tempObject.setJumping(true);
						tempObject.setVelY(-13);
					}
					if(code == KeyEvent.VK_X)
					{
						AT_ST_Walker.toggleEntry = true;
					}
					
					// shoot 
					if(code == KeyEvent.VK_ENTER)
					{		
						for(int x = 0; x < handler.object.size(); x++)
						{
							GameObject temp = handler.object.get(x);
							if(temp.getID() == ObjectId.Weapon)
							{

								if(HUD.bullets > 0 && shootReady)
								{
									shootReady = false;
									if(Player.facing == 1)
									{
										handler.addObject(new Bullet(temp.getX() + 52, temp.getY() + 38, 1, handler, cam, ObjectId.Bullet));
										sound.play(0);
										HUD.bullets--;
									}else{
										handler.addObject(new Bullet(temp.getX() - 10, temp.getY() + 38, -1, handler, cam, ObjectId.Bullet));
										sound.play(0);
										HUD.bullets--;
									}
								}						
							}		
						}
					}
				}	
				
				// AT_ST movement
				
				if(tempObject.getID() == ObjectId.AT_ST)
				{
					if(AT_ST_Walker.enabled)
					{
						if(code == KeyEvent.VK_A) handler.setLeft(true);
						if(code == KeyEvent.VK_D) handler.setRight(true);
						
						if(code == KeyEvent.VK_X)
						{	
							handler.addObject(new Player((int)tempObject.getX() + 30, tempObject.getY() + 120, ObjectId.Player, handler, cam));
							handler.addObject(new Weapon(0, 0, handler, ObjectId.Weapon));
							AT_ST_Walker.enabled = false;
						}
								
						if(code == KeyEvent.VK_UP){
							AT_ST_Walker.velAngle = 0.0005f;
						}
						if(code == KeyEvent.VK_DOWN){
							AT_ST_Walker.velAngle = -0.0005f;
						}
						
						// shoot 
						if(code == KeyEvent.VK_ENTER)
						{		
							if(shootReady && cooldown(750))
							{
								shootReady = false;
								t1 = System.currentTimeMillis();
								if(AT_ST_Walker.facing == 1)
								{
									handler.addObject(new Bullet(tempObject.getX() + 120, tempObject.getY() + 82, 0, handler, cam, ObjectId.Bullet));
									sound.play(1);
								}else{
									handler.addObject(new Bullet(tempObject.getX() + 35, tempObject.getY() + 82, 0, handler, cam, ObjectId.Bullet));
									sound.play(1);
								}
							}								
						}		
					}			
				}	
			}		
		}
		// Exit
		if(code == KeyEvent.VK_ESCAPE)
		{
			System.exit(0);
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_X)
		{
			AT_ST_Walker.toggleEntry = false;
		}
		
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ObjectId.Player)
			{
				if(code == KeyEvent.VK_A) handler.setLeft(false);
				if(code == KeyEvent.VK_D) handler.setRight(false);
				
				if(code == KeyEvent.VK_ENTER)
				{
					shootReady = true;
				}
			}
			// AT_ST
			if(tempObject.getID() == ObjectId.AT_ST)
			{
				if(code == KeyEvent.VK_A) handler.setLeft(false);
				if(code == KeyEvent.VK_D) handler.setRight(false);
				
				if(code == KeyEvent.VK_UP){
					AT_ST_Walker.velAngle = 0.0f;
				}
				if(code == KeyEvent.VK_DOWN){
					AT_ST_Walker.velAngle = 0.0f;
				}
				
				if(code == KeyEvent.VK_ENTER)
				{
					t2 = System.currentTimeMillis();
					shootReady = true;
				}
			}
		}
	}
	
	private boolean cooldown(int offset)
	{
		if(t2 - t1 > offset)
		{
			return true;
		}else{
			return false;
		}
	}
}
