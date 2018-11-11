package data;

import org.newdawn.slick.Image;

import Enity.TileType;

import static helpers.Graphics.*;
import static helpers.Setup.*;

public class Tile {
	
	private float x, y, maxX, minX, velX;
	private int width, height, hp;
	private TileType type;
	private Image image;
	private Image[] aImage;
	private int index;
	
	public Tile(float x, float y, int width, int height, TileType type)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
		this.hp = type.hp;
		this.image = quickLoaderImage("tiles/" + type.textureName);
		tileCounter++;
		this.aImage = new Image[4];
		this.maxX = x + TILE_SIZE * 2;
		this.minX = x - TILE_SIZE * 2;
		this.velX = 1;
		this.index = 0;
		for(int i = 0; i < 4; i++)
		{
			aImage[i] = quickLoaderImage("tiles/Rock_Basic_" + i);
		}
	}
	
	public void update()
	{
		if(x > maxX)
		{
			velX -= 1;
		}
		if(x < minX)
		{
			velX = 1;
		}
		x += velX * 1;
	}

	public void draw()
	{
		if(type == TileType.Rock_Basic)
		{
			drawQuadImage(aImage[index], x, y, TILE_SIZE, TILE_SIZE);
		}else{
			drawQuadImage(image, x, y, width, height);
		}
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) 
	{
		this.hp = hp;
	}
	
	public int getIndex() {
		return index;
	}

	public void addIndex() 
	{
		if(this.index < 3)
		{
			this.index++;
		}
	}
	
	public void testDraw()
	{
		drawQuad(x, y, width, height);
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

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}
}
