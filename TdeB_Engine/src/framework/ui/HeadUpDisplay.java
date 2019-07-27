package framework.ui;

import org.lwjgl.opengl.GL11;
import static framework.helper.Collection.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.gamestate.Deathscreen;
import framework.helper.Collection;

import static framework.helper.Graphics.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;


import java.awt.Font;

public class HeadUpDisplay {
	
	
	private Handler handler;
	private StateManager statemanager;
	private TrueTypeFont font;
	private Font awtFont;
	private Image hp_bar, energy_bar;
	private int hud_mid;
	private Color color;
	private UI ui;
	
	private int r;
	private int g;
	private int b;
	private int alpha;
	
	public HeadUpDisplay(Handler handler, int fontSize, StateManager manager){
		this.handler = handler;
		this.statemanager = manager;
		this.awtFont = loadCustomFont("font/Pixel-Miners.ttf", fontSize);
		font = new TrueTypeFont(awtFont, false);
		this.ui = new UI();
		this.ui.addButton("Back", "hud/button_back", 2, 0, 32, 32);
		
		// status bar
		this.hp_bar = quickLoaderImage("hud/hp_bar");
		this.energy_bar = quickLoaderImage("hud/energy_bar");
		
		this.hud_mid = WIDTH/2 + 15;
		this.color = new Color(200, 200, 200);

		r = 255;
		g = 255;
		b = 255;
		alpha = 255;
	}
	
	public void update() {
		if(ui.isButtonClicked("Back")) {
			
			Collection.MOVEMENT_X = 0;
			Collection.MOVEMENT_Y = 0;
			new Deathscreen(handler, statemanager).resetGame();
			StateManager.gameState = GameState.MAINMENU;
		}
	}
	
	public void draw(){

		if(PLAYER_HP > 25) {
			r = 255;
			g = 255;
			b = 255;
			alpha = 255;
		}
		// HP Bar + Text
		drawString(hud_mid - 290, 4, "HP", color);
		
		if(PLAYER_HP > 25) {
			drawQuadImageStatic(hp_bar, hud_mid - 260, 8, PLAYER_HP, 10);
		}else if(PLAYER_HP > 10){
			if(g > 200)
				g -= 2;
			if(b > 200)
				b -=2 ;
			drawQuadImageStatic(hp_bar, hud_mid - 260, 8, PLAYER_HP, 10);
		}else {
			if(g > 80)
				g -= 2;
			if(b > 80)
				b -=2 ;
			drawQuadImageStatic(hp_bar, hud_mid - 260, 8, PLAYER_HP, 10);
		}
		
		// Energy Bar + Text
		drawString(hud_mid - 160, 4, "|  Energy", color);
		drawQuadImageStatic(energy_bar, hud_mid - 60, 8, BATTERY_CHARGE, 10);		
		//drawQuadImageStatic(hp_border, hud_mid - 60, 8, 96, 13);
		
		// Ammo
		drawString(hud_mid + 40, 4, "|  Ammo", color);
		drawString(hud_mid + 110, 4, "" + AMMO_LEFT, color);
		
		// FPS
		drawString(hud_mid + 160, 4, "|  FPS", color);
		drawString(hud_mid + 220, 4, "" + StateManager.framesInLastSecond, color);
		
		ui.draw();
	}
	
	public void drawString(int x, int y, String text, Color color){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(r, g, b, alpha);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public void resetColors() {
		r = 255;
		g = 255;
		b = 255;
		alpha = 255;
	}
}
