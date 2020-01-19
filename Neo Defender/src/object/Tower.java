package object;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import static framework.helper.Collection.*;

import framework.entity.GameEntity;

public abstract class Tower implements GameEntity{

	protected int x;
	protected int y;
	
	public Tower(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void update() {
	}

	@Override
	public void draw() {
		
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
	public Vector2f[] getVertices() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x - (TILE_SIZE * 2), y - (TILE_SIZE * 10), TILE_SIZE * 4, TILE_SIZE * 12);
	}

	@Override
	public Rectangle getTopBounds() {
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		return null;
	}
}
