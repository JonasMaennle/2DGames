package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helper.Graphics.*;

import java.awt.Rectangle;

import static core.Constants.*;

import Entity.GameEntity;
import Entity.TileType;
import core.Handler;

public class Tile implements GameEntity{
	
	private float x, y;
	private int width, height;
	private TileType type;
	private Image image;
	
	public Tile(float x, float y, TileType type, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = type.width;
		this.height = type.height;
		this.type = type;
		
		this.image = quickLoaderImage("tiles/" + type.textureName);
	}
	
	public void update(){}

	public void draw()
	{
		drawQuadImage(image, x, y, width, height);	
	}

	public float getX() {
		return x;
	}
	
	public int getXPlace()
	{
		return (int)(x / TILE_SIZE);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}
	
	public int getYPlace()
	{
		return (int)(y / TILE_SIZE);
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public Vector2f[] getVertices() {	

		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
}

