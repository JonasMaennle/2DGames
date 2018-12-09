package object;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Graphics.*;
import static helpers.Setup.*;

import Enity.Entity;
import shader.Light;

public class Lamp implements Entity{

	private float x, y;
	private int width, height;
	private Light light;
	private Image image;
	
	public Lamp(float x, float y, int r, int g, int b, float radius)
	{
		this.x = x;
		this.y = y;
		this.width = 64;
		this.height = 64;
		
		light = new Light(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), r, g, b, radius);
		
		image = quickLoaderImage("tiles/lamp");
	}
	
	@Override
	public void update() 
	{
		
	}

	@Override
	public void draw() 
	{
		light.setLocation(new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + 1));
		renderSingleLightStatic(shadowObstacleList, light);
		drawQuadImage(image, x, y, width, height);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		
		return new Vector2f[] {
			new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
			new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height/2), // left bottom
			new Vector2f(x + MOVEMENT_X + width / 2, y + MOVEMENT_Y), // right bottom
			new Vector2f(x + MOVEMENT_X + width / 2, y + MOVEMENT_Y), // right top
			
			new Vector2f(x + MOVEMENT_X + width / 2, y + MOVEMENT_Y), // left top
			new Vector2f(x + MOVEMENT_X + width / 2, y + MOVEMENT_Y), // left bottom
			new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height/2), // right bottom
			new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}
}
