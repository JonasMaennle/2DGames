package framework.ui;

import org.lwjgl.opengl.GL11;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import framework.core.Handler;
import framework.core.StateManager;

import static framework.helper.Collection.loadCustomFont;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.util.concurrent.CopyOnWriteArrayList;

public class HeadUpDisplay {
	
	private TrueTypeFont font;
	private Font awtFont;
	private Image boostBar;
	private Handler handler;

	private int r;
	private int g;
	private int b;
	private int alpha;
	
	private int hud_center;
	
	// hp area
	private CopyOnWriteArrayList<Image> hpBlockList;
	
	public HeadUpDisplay(Handler handler, int fontSize, StateManager manager){
		this.handler = handler;
		this.hud_center = WIDTH / 2;
		this.awtFont = loadCustomFont("font/SIMPLIFICA.ttf", fontSize, Font.PLAIN);
		font = new TrueTypeFont(awtFont, false);
		setColors();
		
		this.boostBar = quickLoaderImage("player/boost");
		this.hpBlockList = new CopyOnWriteArrayList<Image>();
		
		for(int i = 0; i < PLAYER_HP_BLOCKS; i++) {
			hpBlockList.add(quickLoaderImage("hud/hp"));
		}
	}
	
	public void update() {

	}
	
	public void draw(){
		// show center
		//drawString(hud_center, 16, "|", new Color(255,255,255));
		
		// hud center
		drawString(hud_center - 236, 8, "|  AMMO:  " + AMMO_LEFT + "  |  SPACE GAME  |  SCORE:  " + GAMESCORE + "  |", new Color(255,255,255));
		
		// fps
		drawString(8, 8, "FPS: " + StateManager.framesInLastSecond, new Color(255,255,255));
		
		// boost bar
		drawQuadImageStatic(boostBar, hud_center - handler.getPlayer().getBoostEnergyMax() / 2, 64, handler.getPlayer().getBoostEnergy(), 4);
		
		// hp bar
		for(int i = 0; i < PLAYER_HP_BLOCKS; i++) {
			// last iteration
			if(i + 1 >= PLAYER_HP_BLOCKS) {
				drawQuadImageStatic(hpBlockList.get(i / 10), hud_center - 370 + (i * 20), 8 + (32 - PLAYER_HP), 16, PLAYER_HP);
			}else {
				drawQuadImageStatic(hpBlockList.get(i / 10), hud_center - 370 + (i * 20), 8, 16, 32);
			}
		}
	}
	
	public void drawString(int x, int y, String text, Color color){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(r, g, b, alpha);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public void setColors() {
		r = 255;
		g = 255;
		b = 255;
		alpha = 255;
	}
}
