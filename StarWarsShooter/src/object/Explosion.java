package object;

import org.newdawn.slick.Animation;

import static helpers.Setup.*;
import static helpers.Graphics.*;

public class Explosion {
	
	private int x, y, width, height;
	private Animation explosion;
	private int lastFrame;
	private boolean alive;
	
	public Explosion(int x, int y, int size, int duration)
	{
		this.x = x - ((size - TILE_SIZE) / 2);
		this.y = y - ((size - TILE_SIZE) / 2);
		this.width = size;
		this.height = size;
		
		this.explosion = new Animation(loadSpriteSheet("objects/explosion1", 100, 100), duration);
		this.lastFrame = explosion.getFrameCount();
		
		this.alive = true;
	}
	
	public void draw()
	{
		if(alive)
		{
			drawAnimation(explosion, x, y, width, height);
			//System.out.println(explosion.getFrame() + "  " + lastFrame);
			if(explosion.getFrame() == lastFrame-1)
				alive = false;
		}
	}
	
	public boolean isAlive()
	{
		return alive;
	}
}
