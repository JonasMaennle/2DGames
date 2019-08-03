package com.jonas.neon.framework;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jonas.neon.window.Game;


public class MouseInput implements MouseListener, MouseMotionListener{

	private long t1, t2;
	public static boolean mouseVisible;
	
	public MouseInput() 
	{
		mouseVisible = true;
	}
	
	public void tick()
	{
		t1 = System.currentTimeMillis();
		if(cursorTimeout(1500))
		{
			t2 = System.currentTimeMillis();
			mouseVisible = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		/*
		 * 
		  	public Rectangle playButton = new Rectangle(Game.WIDTH / 2 - 50, 150, 100, 50);
			public Rectangle helpButton = new Rectangle(Game.WIDTH / 2 - 50, 250, 100, 50);
			public Rectangle quitButton = new Rectangle(Game.WIDTH / 2 - 50, 350, 100, 50);
			
			Ingame menu button:
			public Rectangle menu = new Rectangle(4, 0, 85, 35);
		 * 
		 */
		if(Game.state == Game.STATE.MENU)
		{
			// Play Button
			if(mx >= Game.WIDTH / 2 - 50 && mx <= Game.WIDTH / 2 + 50)
			{
				if(my >= 150 && my <= 200)
				{
					// Pressed Play Button
					Game.state = Game.STATE.GAME;
				}
			}
			
			// Help Button
			if(mx >= Game.WIDTH / 2 - 50 && mx <= Game.WIDTH / 2 + 50)
			{
				if(my >= 250 && my <= 300)
				{
					// Pressed Help Button
					Game.state = Game.STATE.HELP;
				}
			}
			
			// Quit Button
			if(mx >= Game.WIDTH / 2 - 50 && mx <= Game.WIDTH / 2 + 50)
			{
				if(my >= 350 && my <= 400)
				{
					// Pressed Quit Button
					System.exit(0);
				}
			}
		}else if (Game.state == Game.STATE.GAME)
		{
			if(mx >= 4 && mx <= 89)
			{
				if(my >= 0 && my <= 35)
				{
					Game.state = Game.STATE.MENU;
				}
			}
		}else if(Game.state == Game.STATE.HELP)
		{
			if(mx >= 4 && mx <= 89)
			{
				if(my >= 0 && my <= 35)
				{
					Game.state = Game.STATE.MENU;
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		t2 = System.currentTimeMillis();
		mouseVisible = true;
	}
	
	private boolean cursorTimeout(int value)
	{
		if(t1 - t2 > value)
		{
			return true;
		}else{
			return false;
		}
	}
}
