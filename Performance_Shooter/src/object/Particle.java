package object;

import static helpers.Graphics.quickLoaderImage;

import java.util.Random;
import static helpers.Graphics.*;
import org.newdawn.slick.Image;

public class Particle {
	
	private Image particles;
	private Random rand;
	private int x, y, width, height;
	private float velX, velY, speed;
	
	public Particle(int x, int y, int width, int height, float velX, float velY, float speed)
	{
		this.x = x;
		this.y = y;
		this.rand = new Random();
		this.velX = velX;
		this.velY = velY;
		this.speed = speed;
		particles = quickLoaderImage("particles/Rock_" + rand.nextInt(5));
	}
	
	public void update()
	{
		x += velX * speed;
		y += velY * speed;
	}
	
	public void draw()
	{
		//drawQuadImage(particles, x, y, width, height);
		drawQuad(x, y, 64, 64);
	}
}
