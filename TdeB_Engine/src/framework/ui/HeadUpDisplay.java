package framework.ui;

import org.lwjgl.opengl.GL11;
import static framework.helper.Collection.*;
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
	private Image hud_background;
	
	public HeadUpDisplay(Handler handler){
		this.awtFont = new Font("Arial", Font.BOLD, 20);
		this.font = new TrueTypeFont(awtFont, false);
//		font_sw = new Image[10];
//		for(int i = 0; i < 10; i++)
//		{
//			font_sw[i] = quickLoaderImage("font/font_" + i);
//		}
		this.hud_background = quickLoaderImage("hud/hud");
		// Player HP bar
	}
	
	public void drawString(int x, int y, String text){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
		font.drawString(x, y, text);	
		GL11.glDisable(GL_BLEND);
	}
	
	public void draw(){
		drawQuadImageStatic(hud_background, 0, 0, 960, 32);
		// FPS
		//drawString(10, 10, "" + StateManager.framesInLastSecond);
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
