package object;

import java.awt.Rectangle;
import static helper.Graphics.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Entity.GameEntity;
import core.Handler;

public class Player implements GameEntity{
	
	private int x, y, width, height;
	private Image placeholder;
	
	public Player(int x, int y, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = 32;
		this.height = 32;
		
		this.placeholder = quickLoaderImage("player/Player_tmp");
	}

	public void update() {
		
	}

	public void draw() {
		drawQuadImage(placeholder, x, y, width, height);
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
		this.x = (int)x;
	}

	@Override
	public void setY(float y) {
		this.y = (int)y;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		return null;
	}
}
