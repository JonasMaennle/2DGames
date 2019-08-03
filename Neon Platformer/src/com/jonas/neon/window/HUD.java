package com.jonas.neon.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.jonas.neon.framework.Texture;

public class HUD {
	
	public static int START_BULLETS = 20;
	
	private Texture tex = Game.getInstance();
	public static int health;
	public static int bullets;
	private Font hudFont;
	// Menu button
	public Rectangle menu = new Rectangle(4, 0, 85, 35);

	public HUD()
	{
		health = 3;
		bullets = START_BULLETS;
		hudFont = new Font("Verdana", Font.BOLD, 17);
	}
	
	public void tick()
	{
	}
	
	public void render(Graphics g)
	{
		// load HUD 
		g.drawImage(tex.hud_left[0], 0, 0, 200, 100, null);
		g.drawImage(tex.hud_right[0], Window.width-450, 0, 450, 100, null);

		g.setFont(hudFont);
		g.setColor(Color.BLACK);
		g.drawString("x "+ bullets, Window.width-240, 25);
		g.drawString("x "+ health, Window.width-145, 25);
		g.drawString("Level: " + Game.LEVEL, Window.width-100, 25);
		
		g.drawString("Menu", 20, 25);	
		//g2d.draw(menu);
	}
}
