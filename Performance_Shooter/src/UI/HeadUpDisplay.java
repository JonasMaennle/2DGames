package UI;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import data.Handler;

import static helpers.Artist.*;
import static helpers.StateManager.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;

public class HeadUpDisplay {
	
	private TrueTypeFont font;
	private Font awtFont;
	private Image hud, hud_weapon, healthBackground, healthBorder, healthBar;
	private Image[] font_sw;
	
	public HeadUpDisplay(Handler handler)
	{
		this.hud = quickLoaderImage("font/HUD");
		this.hud_weapon = quickLoaderImage("player/weapon_right");
		this.awtFont = new Font("Arial", Font.BOLD, 24);
		this.font = new TrueTypeFont(awtFont, false);
		font_sw = new Image[10];
		for(int i = 0; i < 10; i++)
		{
			font_sw[i] = quickLoaderImage("font/font_" + i);
		}
		
		// Player HP bar
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		this.healthBar = quickLoaderImage("enemy/healthForeground");
	}
	
	public void drawString(int x, int y, String text)
	{
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
		font.drawString(x, y, text);	
		GL11.glDisable(GL_BLEND);
	}
	
	public void draw()
	{
		// draw HUD image
		drawQuadImageStatic(hud, (WIDTH/2) - 500, 0, hud.getWidth(), hud.getHeight());
		// draw HUD weapon
		drawQuadImageRotStatic(hud_weapon, (WIDTH/2) + 185, -2, 50, 25, -5);
		// draw Level counter
		drawCustomNumber(CURRENT_LEVEL, (WIDTH/2) - 54, -1, 20, 40);
		
		// draw HP Bar
		drawQuadImageStatic(healthBackground, (WIDTH/2) - 270, 13, 100, 16);
		drawQuadImageStatic(healthBar, (WIDTH/2) - 270, 13, handler.player.getHealth(), 16);
		drawQuadImageStatic(healthBorder, (WIDTH/2) - 270, 13, 100, 16);
	}
	
	public void drawCustomNumber(int number, int x, int y, int width, int height)
	{
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
