package object;

import static framework.helper.Collection.*;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;

import framework.entity.GameEntity;
import framework.shader.Light;

public class LightSpot implements GameEntity{

	private static final long serialVersionUID = 2360442024122641364L;
	private int x, y;
	private Light light;
	
	public LightSpot(int x, int y, Light light){
		this.x = x;
		this.y = y;
		
		this.light = light;
		lights.add(light);
	}
	
	public void update() {
		light.setLocation(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y));
	}

	public void draw() {
	}
	
	public void removeLightFromList(){
		lights.remove(light);
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
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setWidth(int width) {	
	}

	@Override
	public void setHeight(int height) {	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
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
}
