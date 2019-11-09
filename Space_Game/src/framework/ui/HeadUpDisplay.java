package framework.ui;

import org.lwjgl.opengl.GL11;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import framework.core.Handler;
import framework.core.StateManager;

import static framework.helper.Collection.loadCustomFont;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;

public class HeadUpDisplay {
	
	private TrueTypeFont font;
	private Font awtFont;

	private int r;
	private int g;
	private int b;
	private int alpha;
	
	public HeadUpDisplay(Handler handler, int fontSize, StateManager manager){
		this.awtFont = loadCustomFont("font/Pixel-Miners.ttf", fontSize);
		font = new TrueTypeFont(awtFont, false);
	}
	
	public void update() {

	}
	
	public void draw(){


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
