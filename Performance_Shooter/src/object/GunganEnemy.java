package object;

import org.newdawn.slick.Image;
import static helpers.Artist.*;
import static helpers.Leveler.obstacleList;

import java.awt.Rectangle;

import data.Tile;
import data.TileGrid;

public class GunganEnemy extends Enemy{
	
	private Image image_left, image_right;

	public GunganEnemy(float x, float y, int width, int height) 
	{
		super(x, y, width, height);
		this.image_left = quickLoaderImage("enemy_left");
		this.image_right = quickLoaderImage("enemy_right");
	}
	
	public void update()
	{
		updateBounds();
	}
	
	public void draw()
	{
		//drawQuadImage(image, x, y, width, height);
		drawBounds();
	}
	
	private void mapCollision()
	{

		for(Tile t : obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);

			if(r.intersects(rectLeft))
			{
				velX = 0;
				x = (float) ((float) r.getX() + r.getWidth());//(float) (r.getX() + r.getWidth());
			}
			if(r.intersects(rectRight))
			{
				velX = 0;
				x = (float) (r.getX() - width);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height);
			}
		}
	}

}
