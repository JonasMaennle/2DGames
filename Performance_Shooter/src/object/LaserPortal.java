package object;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Graphics.*;
import static helpers.Setup.MOVEMENT_X;
import static helpers.Setup.MOVEMENT_Y;
import static helpers.Setup.*;

import Enity.Entity;
import shader.Light;

public class LaserPortal implements Entity{

	private float x, y;
	private int width, height;
	private Image laserWall;
	private CopyOnWriteArrayList<Light> laserList;
	private long timerPortal;
	private Rectangle laserBounds;
	
	public LaserPortal(float x, float y) 
	{
		this.x = x;
		this.y = y;
		this.width = 64;
		this.height = 64;
		this.timerPortal = System.currentTimeMillis();
		this.laserBounds = new Rectangle((int)x, (int)y, 64, 320);
		laserList = new CopyOnWriteArrayList<>();
		laserWall = quickLoaderImage("objects/laser_wall");
		
		for(int i = 0; i < 5; i++)
		{
			Light l = new Light(new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + (i * 64) + 32), 10, 0, 0, 10f);
					
			laserList.add(l);
		}
	}
	
	@Override
	public void update() 
	{
		for(int i = 0; i < 5; i++)
		{
			laserList.get(i).setLocation(new Vector2f(x + MOVEMENT_X + 32,  y + MOVEMENT_Y + (i * 64) + 32));
		}
		
		// update portal timer
		if(System.currentTimeMillis() - timerPortal > 1500)
		{
			timerPortal = System.currentTimeMillis();
			if(laserBounds.getHeight() != 0)
			{
				laserBounds.setBounds((int)x, (int)y, width, 0);
			}
			else if(laserBounds.getHeight() == 0)
			{
				laserBounds.setBounds((int)x, (int)y, width, 320);
			}
		}
	}

	@Override
	public void draw()
	{
		if(laserBounds.getHeight() != 0)
		{
			for(int i = 0; i < laserList.size(); i++)
			{
				renderSingleLightStatic(shadowObstacleList, laserList.get(i));
			}
			drawQuadImage(laserWall, x + 24, y, 16, 320);
		}

	}	

	@Override
	public Vector2f[] getVertices() {
		
		return new Vector2f[] {
			new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
			new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + 2), // left bottom
			new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + 2), // right bottom
			new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y), // right top
		};
	}
	
	public Rectangle getLaserBounds()
	{
		return laserBounds;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getVelX() {
		return 0;
	}

	@Override
	public float getVelY() {
		return 0;
	}

	@Override
	public void damage(float amount) {
	}

	@Override
	public boolean isOutOfMap() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
}
