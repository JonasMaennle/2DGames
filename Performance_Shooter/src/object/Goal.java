package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;

import Enity.Entity;
import shader.Light;

public class Goal implements Entity{

	private Image image;
	private float x, y, width, height;
	private Light light;
	
	public Goal(float x, float y)
	{
		this.x = x;
		this.y = y;
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		this.image = quickLoaderImage("objects/goal");
		this.light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), 25, 5, 25, 2f);
	}

	@Override
	public void update() 
	{
		light.setLocation(new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + 32));
	}

	@Override
	public void draw() 
	{
		if(x > getLeftBorder() - 64 && x < getRightBorder() + 64 && y > getTopBorder() - 64 && y < getBottomBorder() + 64)
		{
			renderSingleLightStatic(shadowObstacleList, light);
			drawQuadImage(image, x, y, width, height);
		}
	}
	
	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	@Override
	public int getWidth() 
	{
		return (int) width;
	}

	@Override
	public int getHeight() 
	{
		return (int) height;
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
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getVelX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVelY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOutOfMap() {
		return false;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
}
