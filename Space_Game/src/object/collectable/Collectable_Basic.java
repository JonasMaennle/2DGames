package object.collectable;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import object.Player;

import static framework.helper.Graphics.*;

public abstract class Collectable_Basic implements GameEntity{
	
	protected float x, y;
	protected int width, height;
	protected Image image;
	protected boolean found;
	protected Player player;
	
	public Collectable_Basic(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.found = false;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		if(player == null)
			drawQuadImage(image, x, y, width, height);
		else
			drawQuadImageRotCenter(image, player.getX(), player.getY(), width, height, player.getPlayerRotation());
	}

	@Override
	public float getX() {return x;}

	@Override
	public float getY() {return y;}

	@Override
	public int getWidth() {return width;}

	@Override
	public int getHeight() {return height;}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {return new Rectangle((int)x, (int)y, width, height);}

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

	public void setPlayer(Player player) {
		this.player = player;
	}
}
