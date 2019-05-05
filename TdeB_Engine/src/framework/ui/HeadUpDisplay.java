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
	private Image[] font_sw;
	private Image hud_background, hp_bar, hp_border, energy_bar, player_image;
	private Image text_fps , text_hp;
	private int hp_Offset;
	
	public HeadUpDisplay(Handler handler){
		this.awtFont = new Font("Arial", Font.BOLD, 22);
		this.font = new TrueTypeFont(awtFont, false);
		
		font_sw = new Image[10];
		for(int i = 0; i < 10; i++)
		{
			font_sw[i] = quickLoaderImage("font/font_" + i);
		}
		this.hud_background = quickLoaderImage("hud/hud");
		
		// status bar
		this.hp_bar = quickLoaderImage("hud/hp_bar");
		this.hp_border = quickLoaderImage("hud/hp_border");
		this.energy_bar = quickLoaderImage("hud/energy_bar");
		
		// player
		this.player_image = quickLoaderImage("player/player_idle_right");
		
		// text
		this.text_hp = quickLoaderImage("hud/text_hp");
		this.text_fps = quickLoaderImage("hud/text_fps");
		
		this.hp_Offset = 230;
	}
	
	public void draw(){
		drawQuadImageStatic(hud_background, 0, 0, 960, 32);
		
		//drawQuadImageStatic(battery_symbol, 200, 5, 32, 32);
		//drawQuadImageStatic(hp_bar, 200, 2, 96, 16);
		
		// Bar
		drawQuadImageStatic(hp_bar, hp_Offset, 2, PLAYER_HP, 13);
		drawQuadImageStatic(energy_bar, hp_Offset, 17, BATTERY_CHARGE, 13);
		
		drawQuadImageStatic(hp_border, hp_Offset, 2, 96, 13);
		drawQuadImageStatic(hp_border, hp_Offset, 17, 96, 13);
		
		drawQuadImageStatic(player_image, WIDTH/2 - TILE_SIZE/2, 0, TILE_SIZE, TILE_SIZE);
		
		// Text
		drawQuadImageStatic(text_hp, 164, 0, 64, 32);
		drawQuadImageStatic(text_fps, 690, 0, 64, 32);
		
		// FPS
		drawCustomNumber(StateManager.framesInLastSecond, 750, 8, 18, 18);
	}
	
	public void drawString(int x, int y, String text, Color color){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(255, 255, 255, 255);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public void drawCustomNumber(int number, int x, int y, int width, int height){
		if(number < 0)number *= -1;
		String num = String.valueOf(number);
		int offset = 0;
		for(int i = 0; i < num.length(); i++)
		{
			int tmp = Integer.parseInt(num.substring(i, i+1));
			//System.out.println(tmp);
			drawQuadImageStatic(font_sw[tmp], x + offset, y, width, height);
			offset += width;
		}
	}
}
