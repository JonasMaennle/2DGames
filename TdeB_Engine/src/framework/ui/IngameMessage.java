package framework.ui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static framework.helper.Collection.*;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class IngameMessage {
	
	private float x, y;
	private String text;
	private Color color, color2;
	private TrueTypeFont font;
	private Font awtFont;
	private int durationTime;
	private long startTime;
	private float timeFactor;
	
	private int r, g, b, alpha;
	
	public IngameMessage(float x, float y, String text, Color color, int textSize, int time){
		this.x = x + MOVEMENT_X;
		this.y = y + MOVEMENT_Y;
		this.text = text;
		this.color = color;
		this.durationTime = time;
		this.startTime = 0;

		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
		
		awtFont = new Font("Arial", Font.BOLD, textSize);
		font = new TrueTypeFont(awtFont, false);
	}
	
	public void draw(){
		
		//System.out.println(durationTime - (System.currentTimeMillis() - startTime));
		timeFactor = (float)(durationTime - (System.currentTimeMillis() - startTime)) / durationTime;
		timeFactor *= 255;
		
		alpha = (int)timeFactor;
		//System.out.println(alpha);
		color = new Color(r, g, b, alpha);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color2 = new Color(255, 255, 255, 255);
		font.drawString(x, y, "", color2);

		GL11.glDisable(GL_BLEND);
	}

	public int getTime() {
		return durationTime;
	}

	public void setTime(int time) {
		this.durationTime = time;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
