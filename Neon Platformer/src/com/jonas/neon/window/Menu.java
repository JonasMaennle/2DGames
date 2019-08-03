package com.jonas.neon.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.jonas.neon.framework.Texture;

public class Menu {
	
	Texture tex = Game.getInstance();
	private int scaling = 8;
	
	public Rectangle playButton = new Rectangle(Game.WIDTH / 2 - 50, 150, 100, 50);
	public Rectangle helpButton = new Rectangle(Game.WIDTH / 2 - 50, 250, 100, 50);
	public Rectangle quitButton = new Rectangle(Game.WIDTH / 2 - 50, 350, 100, 50);
	
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		Font font = new Font("arial", Font.BOLD, 50);
		g.setFont(font);
		g.setColor(Color.BLACK);
		//g.drawString("Dick in the Box", Game.WIDTH / 2 - 180, 100);
		
		Font fnt1 = new Font("arial", Font.BOLD, 30);
		g.setFont(fnt1);
		g.drawString("Play", playButton.x + 20, playButton.y + 33);
		g2d.draw(playButton);
		
		g.drawString("Help", helpButton.x + 20, helpButton.y + 33);
		g2d.draw(helpButton);
		
		g.drawString("Quit", quitButton.x + 20, quitButton.y + 33);
		g2d.draw(quitButton);
		
		g.drawImage(tex.player[15], -50, Window.height-760, 48 * scaling, 96 * scaling, null);
		g.drawImage(tex.enemy[0], Window.width-350, Window.height-680, 48 * scaling, 85 * scaling, null);
		// rotate weapon	
		g2d.rotate(Math.toRadians(20));
		g.drawImage(tex.weapon[0], 190, Window.height-600, 48 * scaling, 32 * scaling, null);
	}
}
