package object;

import static helpers.Graphics.quickLoaderImage;
import static helpers.Setup.*;

import java.util.Random;
import static helpers.Graphics.*;
import org.newdawn.slick.Image;

public class Particle {
	
	private Image particles;
	private Random rand;
	private int x, y, width, height;
	private float velX, velY, speed;
	
	public Particle(int x, int y, int width, int height, float velX, float velY, float speed, String color)
	{
		this.x = x;
		this.y = y;
		this.rand = new Random();
		this.velX = velX;
		this.velY = velY;
		this.width = width;
		this.height = height;
		this.speed = speed;
		
		if(color.equals("gray"))particles = quickLoaderImage("particles/Rock_" + rand.nextInt(5));
		if(color.equals("brown"))particles = quickLoaderImage("particles/Dirt_" + rand.nextInt(5));
		
		if(this.velX == 0)
			this.velX = rand.nextInt(10)+1;
		if(this.velY == 0)
			this.velY = rand.nextInt(10)+1;
	}
	
	public void update()
	{
		x += velX * speed;
		y += velY * speed;
	}
	
	public void draw()
	{
		drawQuadImage(particles, x, y, width, height);
	}
	
	public boolean isOutOfMap()
	{
		if(x < getLeftBoarder() || x > getRightBoarder() || y < getTopBoarder() || y > getBottomBoarder())
			return true;
		
		return false;
	}
}
