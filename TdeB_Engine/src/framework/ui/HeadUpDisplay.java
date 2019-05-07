package framework.ui;

import org.lwjgl.opengl.GL11;
import static framework.helper.Collection.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import framework.core.Handler;
import framework.core.StateManager;

import static framework.helper.Graphics.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;


import java.awt.Font;

public class HeadUpDisplay {
	
	private TrueTypeFont font;
	private Font awtFont;
	private Image hp_bar, hp_border, energy_bar;
	private int hud_mid;
	private Color color;
	
	public HeadUpDisplay(Handler handler, int fontSize){
		this.awtFont = loadCustomFont("font/Pixel-Miners.ttf", fontSize);
		this.font = new TrueTypeFont(awtFont, false);
		
		// status bar
		this.hp_bar = quickLoaderImage("hud/hp_bar");
		this.hp_border = quickLoaderImage("hud/hp_border");
		this.energy_bar = quickLoaderImage("hud/energy_bar");
		
		this.hud_mid = WIDTH/2 + 15;
		this.color = new Color(200, 200, 200);
	}
	
	public void draw(){

		// HP Bar + Text
		drawString(hud_mid - 290, 4, "HP", color);
		drawQuadImageStatic(hp_bar, hud_mid - 260, 8, PLAYER_HP, 13);
		drawQuadImageStatic(hp_border, hud_mid - 260, 8, 96, 13);
		
		// Energy Bar + Text
		drawString(hud_mid - 160, 4, "|  Energy", color);
		drawQuadImageStatic(energy_bar, hud_mid - 60, 8, BATTERY_CHARGE, 13);	
		drawQuadImageStatic(hp_border, hud_mid - 60, 8, 96, 13);
		
		// Ammo
		drawString(hud_mid + 40, 4, "|  Ammo", color);
		drawString(hud_mid + 110, 4, "" + AMMO_LEFT, color);
		
		// FPS
		drawString(hud_mid + 160, 4, "|  FPS", color);
		drawString(hud_mid + 220, 4, "" + StateManager.framesInLastSecond, color);
	}
	
	public void drawString(int x, int y, String text, Color color){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(255, 255, 255, 255);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
}
