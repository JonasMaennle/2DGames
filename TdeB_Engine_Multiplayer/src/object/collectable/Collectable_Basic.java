package object.collectable;

import java.awt.Rectangle;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.shader.Light;

public class Collectable_Basic implements GameEntity, Serializable{

	private static final long serialVersionUID = 7036469980922995500L;
	protected int x, y, width, height;
	protected transient Image image;
	protected boolean found;
	protected transient Light light;
	
	public Collectable_Basic(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.found = false;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
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
	public Vector2f[] getVertices() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
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

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}
}
