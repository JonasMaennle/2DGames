package object;

import org.newdawn.slick.Image;
import static helpers.Artist.*;

import java.awt.Rectangle;

import data.Handler;
import data.Tile;

public class GunganEnemy extends Enemy{
	
	private Image image_left, image_right;
	private Handler handler;

	public GunganEnemy(float x, float y, int width, int height, Handler handler) 
	{
		super(x, y, width, height);
		this.image_left = quickLoaderImage("enemy_left");
		this.image_right = quickLoaderImage("enemy_right");
		this.velX = 2;
		this.handler = handler;
	}
	
	public void update()
	{
		x += velX;
		
		updateBounds();
		mapCollision();
	}
	
	public void draw()
	{
		if(velX > 0)
		{
			drawQuadImage(image_right, x, y, width, height);
		}else{
			drawQuadImage(image_left, x, y, width, height);
		}
		//drawBounds();
	}
	
	private void mapCollision()
	{
		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);

			if(r.intersects(rectLeft))
			{
				velX *= -1;
				x = (float) ((float) r.getX() + r.getWidth());
			}
			if(r.intersects(rectRight))
			{
				velX *= -1;
				x = (float) (r.getX() - width);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}
	
	private void shoot()
	{
		System.out.println("shoot");
	}
}
