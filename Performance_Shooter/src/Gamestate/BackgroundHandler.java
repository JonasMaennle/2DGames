package Gamestate;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import Enity.Entity;
import object.Cloud;

public class BackgroundHandler {
	
	private Image sky, background, foreground, filter, bottom;
	private float bg00_offset, bg01_offset, bg02_offset;
	private float bg03_offset, bg04_offset, bg05_offset;
	private float alpha;
	private Cloud cloud;
	private Entity entity;
	
	public BackgroundHandler(Entity entity)
	{
		this.background = quickLoaderImage("background/background_00");
		this.foreground = quickLoaderImage("background/background_01");
		this.sky = quickLoaderImage("background/Sky");
		this.filter = quickLoaderImage("background/filter");
		this.bottom = quickLoaderImage("background/bottom");
		this.entity = entity;
		
		this.cloud = new Cloud(Display.getX() + Display.getWidth(), 0);
		
		this.bg00_offset = Display.getX() - Display.getWidth();
		this.bg01_offset = Display.getX();
		this.bg02_offset = Display.getX() + Display.getWidth();
		
		this.bg03_offset = Display.getX() - Display.getWidth();
		this.bg04_offset = Display.getX();
		this.bg05_offset = Display.getX() + Display.getWidth();
		
		this.alpha = 0.4f;
	}
	
	public void draw()
	{
		// Draw blue SKY
		drawQuadImageStatic(sky, 0, 0, 2048, 2048);
		
		// Calculate and draw MOUNTAINS
		if(entity.getVelX() > 0)
		{
			bg00_offset -= 0.1f;
			bg01_offset -= 0.1f; 
			bg02_offset -= 0.1f;
		}else if(entity.getVelX() < 0)
		{
			bg00_offset += 0.1f;
			bg01_offset += 0.1f; 
			bg02_offset += 0.1f;
		}
		// Left image
		if(bg00_offset < (-Display.getWidth() * 2))
		{
			bg00_offset = Display.getX()-Display.getWidth();
		}else if(bg00_offset > Display.getX())
		{
			bg00_offset = Display.getX()-Display.getWidth();
		}
		// Middle image
		if(bg01_offset < -Display.getWidth())
		{
			bg01_offset = Display.getX();
		}else if(bg01_offset > Display.getWidth())
		{
			bg01_offset = Display.getX();
		}
		// Right image
		if(bg02_offset < Display.getX())
		{
			bg02_offset = Display.getX() + Display.getWidth();
		}else if(bg02_offset > (Display.getWidth() * 2))
		{
			bg02_offset = Display.getX() + Display.getWidth();
		}
		
		
		drawQuadImageStatic(background, bg00_offset, 0, WIDTH, HEIGHT*2);
		drawQuadImageStatic(background, bg01_offset, 0, WIDTH, HEIGHT*2);
		drawQuadImageStatic(background, bg02_offset, 0, WIDTH, HEIGHT*2);

		// Calculate and draw CLOUDS
		cloud.update();
		cloud.draw();
		
		// Calculate and draw FOREST
		if(entity.getVelX() > 0)
		{
			bg03_offset -= 0.5f;
			bg04_offset -= 0.5f; 
			bg05_offset -= 0.5f;
		}else if(entity.getVelX() < 0)
		{
			bg03_offset += 0.5f;
			bg04_offset += 0.5f; 
			bg05_offset += 0.5f;
		}
		// Left image
		if(bg03_offset < (-Display.getWidth() * 2))
		{
			bg03_offset = Display.getX()-Display.getWidth() + 1;
		}else if(bg03_offset > Display.getX())
		{
			bg03_offset = Display.getX()-Display.getWidth() + 1;
		}
		// Middle image
		if(bg04_offset < -Display.getWidth())
		{
			bg04_offset = Display.getX();
		}else if(bg04_offset > Display.getWidth())
		{
			bg04_offset = Display.getX();
		}
		// Right image
		if(bg05_offset < Display.getX())
		{
			bg05_offset = Display.getX() + Display.getWidth() -1;
		}else if(bg05_offset > (Display.getWidth() * 2))
		{
			bg05_offset = Display.getX() + Display.getWidth() -1;
		}
		// Dark Bottom
		drawQuadImageStatic(bottom, 0, HEIGHT - 100, 2048, 2048);
		
		drawQuadImageStatic(foreground, bg03_offset, (MOVEMENT_Y * 0.1f) + 100, WIDTH, foreground.getHeight()/2);
		drawQuadImageStatic(foreground, bg04_offset, (MOVEMENT_Y * 0.1f) + 100, WIDTH, foreground.getHeight()/2);
		drawQuadImageStatic(foreground, bg05_offset, (MOVEMENT_Y * 0.1f) + 100, WIDTH, foreground.getHeight()/2);
		
		// Draw alpha FILTER
		GL11.glColor4f(0, 0, 0, alpha);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	public void setCustomBackground(Image foreground, Image background)
	{
		this.background = background;
		this.foreground = foreground;
	}
	
	public void drawBasicBackground()
	{
		drawQuadImageStatic(sky, 0, 0, 2048, 2048);
	}
	
	public void setAlpha(float alpha)
	{
		this.alpha = alpha;
	}
	
	public float getAlpha()
	{
		return alpha;
	}
	
	public void endScreen(float alpha)
	{
		GL11.glColor4f(0, 0, 0, alpha);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
