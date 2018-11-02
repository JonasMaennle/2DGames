package data;

import static helpers.Artist.*;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import object.Cloud;

public class BackgroundHandler {
	
	private Image sky, background_mountain, background_forest, filter;
	private float bg00_offset, bg01_offset, bg02_offset;
	private float bg03_offset, bg04_offset, bg05_offset;
	private Random rand;
	private float alpha;
	
	private CopyOnWriteArrayList<Cloud> cloudList;
	private Player player;
	
	public BackgroundHandler(Player player)
	{
		this.background_mountain = quickLoaderImage("background_00");
		this.background_forest = quickLoaderImage("background_01");
		this.sky = quickLoaderImage("sky");
		this.filter = quickLoaderImage("filter");
		this.player = player;
		this.rand = new Random();
		
		this.cloudList = new CopyOnWriteArrayList<>();
		
		this.bg00_offset = Display.getX() - WIDTH;
		this.bg01_offset = Display.getX();
		this.bg02_offset = Display.getX() + WIDTH;
		
		this.bg03_offset = Display.getX() - WIDTH;
		this.bg04_offset = Display.getX();
		this.bg05_offset = Display.getX() + WIDTH;
		
		this.alpha = 0.2f;
	}
	
	public void draw()
	{
		// Draw blue sky
		drawQuadImageStatic(sky, 0, 0, 2048, 2048);
		
		// Calculate and draw mountains
		if(player.getVelX() > 0)
		{
			bg00_offset -= 0.1f;
			bg01_offset -= 0.1f; 
			bg02_offset -= 0.1f;
		}else if(player.getVelX() < 0)
		{
			bg00_offset += 0.1f;
			bg01_offset += 0.1f; 
			bg02_offset += 0.1f;
		}
		// Left image
		if(bg00_offset < (-WIDTH * 2))
		{
			bg00_offset = Display.getX()-WIDTH;
		}else if(bg00_offset > Display.getX())
		{
			bg00_offset = Display.getX()-WIDTH;
		}
		// Middle image
		if(bg01_offset < -WIDTH)
		{
			bg01_offset = Display.getX();
		}else if(bg01_offset > WIDTH)
		{
			bg01_offset = Display.getX();
		}
		// Right image
		if(bg02_offset < Display.getX())
		{
			bg02_offset = Display.getX() + WIDTH;
		}else if(bg02_offset > (WIDTH * 2))
		{
			bg02_offset = Display.getX() + WIDTH;
		}
		drawQuadImageStatic(background_mountain, bg00_offset, 0, 2048, 2048);
		drawQuadImageStatic(background_mountain, bg01_offset, 0, 2048, 2048);
		drawQuadImageStatic(background_mountain, bg02_offset, 0, 2048, 2048);
		
		// Calculate and draw forest
		if(player.getVelX() > 0)
		{
			bg03_offset -= 0.5f;
			bg04_offset -= 0.5f; 
			bg05_offset -= 0.5f;
		}else if(player.getVelX() < 0)
		{
			bg03_offset += 0.5f;
			bg04_offset += 0.5f; 
			bg05_offset += 0.5f;
		}
		// Left image
		if(bg03_offset < (-WIDTH * 2))
		{
			bg03_offset = Display.getX()-WIDTH;
		}else if(bg03_offset > Display.getX())
		{
			bg03_offset = Display.getX()-WIDTH;
		}
		// Middle image
		if(bg04_offset < -WIDTH)
		{
			bg04_offset = Display.getX();
		}else if(bg04_offset > WIDTH)
		{
			bg04_offset = Display.getX();
		}
		// Right image
		if(bg05_offset < Display.getX())
		{
			bg05_offset = Display.getX() + WIDTH;
		}else if(bg05_offset > (WIDTH * 2))
		{
			bg05_offset = Display.getX() + WIDTH;
		}
		drawQuadImageStatic(background_forest, bg03_offset, 0, 2048, 2048);
		drawQuadImageStatic(background_forest, bg04_offset, 0, 2048, 2048);
		drawQuadImageStatic(background_forest, bg05_offset, 0, 2048, 2048);
		
		// Draw alpha filter
		GL11.glColor4f(0, 0, 0, alpha);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
	
	public void cloudSpawner()
	{
		if(rand.nextInt(600) == 0 && cloudList.size() <= 3)
		{
			cloudList.add(new Cloud((MOVEMENT_X * -1) + WIDTH, 10));
		}
		
		for(Cloud c : cloudList)
		{
			c.update();
			c.draw();
			if(c.getX() + c.getWidth() < (MOVEMENT_X * -1))
			{
				cloudList.remove(c);
			}
		}
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
}