package data;

import static helpers.Graphics.quickLoaderImage;
import static helpers.Setup.*;

import java.util.Random;
import static helpers.Graphics.*;
import org.newdawn.slick.Image;

public class Particle {
	
	private Image particles;
	private Random rand;
	private int x, y, width, height;
	private float velX, velY, speed, angle;
	private String color;
	
	public Particle(int x, int y, int width, int height, float velX, float velY, float speed, String color, float angle)
	{
		this.x = x;
		this.y = y;
		this.rand = new Random();
		this.velX = velX;
		this.velY = velY;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.angle = angle;
		this.color = color;
		
		if(color.equals("gray"))particles = quickLoaderImage("particles/Rock_" + rand.nextInt(5));
		if(color.equals("brown"))particles = quickLoaderImage("particles/Dirt_" + rand.nextInt(5));
		if(color.equals("red"))particles = quickLoaderImage("particles/Blood_" + rand.nextInt(5));
		if(color.equals("white"))particles = quickLoaderImage("particles/Snow_" + rand.nextInt(5));
		if(color.equals("orange"))particles = quickLoaderImage("particles/Lava_" + rand.nextInt(5));
		
		if(this.velX == 0)
			this.velX = rand.nextInt(4)+1;
		if(this.velY == 0)
			this.velY = rand.nextInt(3)+1;
	}
	
	public void update()
	{
		x += velX * speed;
		y += velY * speed;
		
		velY += 0.1f;
		
		if(color.equals("white"))
		{
			velX -= 0.1f;
			
			if(velX < -10)
				velX = -10;
			if(velY > 10)
				velY = 10;
		}
	}
	
	public void draw()
	{
		drawQuadImageRot(particles, x, y, width, height, angle);
	}
	
	public boolean isOutOfMap()
	{
		if(x < getLeftBorder() || x > getRightBorder() || y < getTopBorder() || y > getBottomBorder())
			return true;
		
		return false;
	}
	
	public boolean isOutOfMapBottom()
	{
		if(y > getBottomBorder())
			return true;
		return false;
	}
}
