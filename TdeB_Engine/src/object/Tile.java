package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helper.Graphics.*;

import java.awt.Rectangle;

import static helper.Collection.*;

import core.Handler;
import entity.GameEntity;

public class Tile implements GameEntity{
	
	private float x, y;
	private int width, height;
	private Image image;
	
	public Tile(float x, float y, Image image, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		//this.type = type;
		
		this.image = image;//quickLoaderImage("tiles/" + type.textureName);
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
	
	public Rectangle getTopBounds(){
		return new Rectangle((int)x + 4, (int)y, width - 8, 4);
	}
	
	public Rectangle getBottomBounds(){
		return new Rectangle((int)x + 4, (int)y + height-4, width - 8, 4);
	}
	
	public Rectangle getLeftBounds(){
		return new Rectangle((int)x, (int)y + 4, 4, height - 8);
	}
	
	public Rectangle getRightBounds(){
		return new Rectangle((int)x + width-4, (int)y + 4, 4, height - 8);
	}
}

