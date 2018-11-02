package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import Enity.Entity;
import data.Tile;
import data.TileGrid;
import shader.Light;

import static helpers.Artist.*;


public class Enemy implements Entity{

	private int width, height;
	private float speed, x, y, health, hiddenHealth;
	private Texture texture;
	private Tile startTile;
	private boolean first, alive;
	private TileGrid grid;
	protected Light light;
	
	// Default constructor
	public Enemy(int tileX, int tileY, TileGrid grid)
	{
		this.texture = quickLoad("Enemy");
		this.startTile = grid.getTile(tileX, tileY);
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		this.speed = 50;
		this.grid = grid;
		this.health = 100;
		this.hiddenHealth = health;
		this.first = true;
		this.alive = true;
	}
	
	public Enemy(Texture texture, Tile startTile, TileGrid grid, int width, int height, float speed, float health)
	{
		this.startTile = startTile;
		this.texture = texture;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.grid = grid;
		this.health = health;
		this.hiddenHealth = health;
		this.first = true;
		this.alive = true;
	}
	
	public void update()
	{

	}
	
	
	// Take damage from external source
	public void damage(int amount)
	{
			
	}
	
	public void draw()
	{
		//drawQuadTex(texture, x, y, width, height);
	}
	
	public float getHiddenHealth()
	{
		return hiddenHealth;
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

	public float getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setTexture(String texture)
	{
		this.texture = quickLoad(texture);
	}

	public Tile getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
	
	public TileGrid getTileGrid()
	{
		return grid;
	}

	public boolean isAlive() {
		return alive;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}
}
