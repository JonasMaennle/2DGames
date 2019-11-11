package object.weapon;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.entity.LaserType;
import framework.shader.Light;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

public abstract class Laser_Basic implements GameEntity{

	protected Image image;
	protected float x, y, angle, speed;
	protected float velX, velY;
	protected int width, height;
	protected Light light;
	protected LaserType laserType;
	
	public Laser_Basic(float x, float y, int width, int height, float velX, float velY, int speed, float angle, Light light) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.velX = velX;
		this.velY = velY;
		this.speed = speed;
		this.image = quickLoaderImage("player/bullet_tmp");
		this.light = light;
		
		lights.add(light);
	}
	
	@Override
	public void update() {
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + height/2));
		
		y += velY * speed;
		x += velX * speed;	
	}

	@Override
	public void draw() {
		// drawQuadImageRotCenter(image, x, y, width, height, angle);
	}
	
	public void die() {
		lights.remove(light);
	}
	
	public boolean isOutOfMap() {
		if(x - width < getLeftBorder()-100 || x + width > getRightBorder()+100 || y - height < getTopBorder() || y + height > getBottomBorder())
			return true;
		
		return false;
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
	public Vector2f[] getVertices() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Rectangle getTopBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public LaserType getLaserType() {
		return laserType;
	}
}
