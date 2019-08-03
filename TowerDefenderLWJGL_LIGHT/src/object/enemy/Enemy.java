package object.enemy;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import data.Checkpoint;
import data.Entity;
import data.Player;
import data.Tile;
import data.TileGrid;
import shader.Light;

import static helpers.Artist.*;
import static helpers.Clock.*;

import java.util.ArrayList;

public class Enemy implements Entity{

	private int width, height, currentCheckpoint;
	private float speed, x, y, health, startHealth, hiddenHealth;
	private Texture texture, healthBackground, healthForeground, healthBorder;
	private Tile startTile;
	private boolean first, alive;
	private TileGrid grid;
	protected Light light;
	
	private ArrayList<Checkpoint> checkpoints;
	private int[] directions;
	
	// Default constructor
	public Enemy(int tileX, int tileY, TileGrid grid)
	{
		this.texture = quickLoad("Enemy");
		this.healthBackground = quickLoad("healthBackground");
		this.healthForeground = quickLoad("healthForeground");
		this.healthBorder = quickLoad("healthBorder");
		this.startTile = grid.getTile(tileX, tileY);
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = TILE_SIZE;
		this.height = TILE_SIZE;
		this.speed = 50;
		this.grid = grid;
		this.health = 100;
		this.startHealth = health;
		this.hiddenHealth = health;
		this.first = true;
		this.alive = true;
		this.checkpoints = new ArrayList<Checkpoint>();
		this.directions = new int[2];
		// x direction
		this.directions[0] = 0;
		// y directon
		this.directions[1] = 1;
		this.directions = findNextD(startTile);
		this.currentCheckpoint = 0;
		
		populateCheckpointList();
	}
	
	public Enemy(Texture texture, Tile startTile, TileGrid grid, int width, int height, float speed, float health)
	{
		this.startTile = startTile;
		this.texture = texture;
		this.healthBackground = quickLoad("healthBackground");
		this.healthForeground = quickLoad("healthForeground");
		this.healthBorder = quickLoad("healthBorder");
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.grid = grid;
		this.health = health;
		this.startHealth = health;
		this.hiddenHealth = health;
		this.first = true;
		this.alive = true;
		this.checkpoints = new ArrayList<Checkpoint>();
		this.directions = new int[2];
		// x direction
		this.directions[0] = 0;
		// y directon
		this.directions[1] = 1;
		this.directions = findNextD(startTile);
		this.currentCheckpoint = 0;
		
		populateCheckpointList();
	}
	
	public void update()
	{
		// Check if its the first time this class is updated, if do nothing
		if(first)
			first = false;
		else{
			if(checkpointReached())
			{
				// Check if there are more checkpoints before moving on
				if(currentCheckpoint + 1 == checkpoints.size())
				{
					endReached();
				}else{
					currentCheckpoint++;
				}	
			}else{
				// If not at checkpoint, continue in current direction
				x += delta() * checkpoints.get(currentCheckpoint).getxDirection() * speed;
				y += delta() * checkpoints.get(currentCheckpoint).getyDirection() * speed;
			}
		}	
	}
	
	// Run when last checkpoint is reached by enemy
	private void endReached()
	{
		Player.modifyLives(-1);
		die();
	}
	
	private boolean checkpointReached()
	{
		boolean reached = false;
		Tile t = checkpoints.get(currentCheckpoint).getTile();
		
		// Check if position reached tile within variance of 3 
		if(x > t.getX() - 3 && x < t.getX() + 3 && y > t.getY() - 3 && y < t.getY() +3)
		{
			reached = true;
			x = t.getX();
			y = t.getY();	
		}
		return reached;
	}
	
	private void populateCheckpointList()
	{
		// Add first checkpoint manually based on StartTile
		checkpoints.add(findNextC(startTile, directions = findNextD(startTile)));
		
		int counter = 0;
		boolean cont = true;
		
		while(cont)
		{
			int[] currentD = findNextD(checkpoints.get(counter).getTile());
			// check if a next direction/checkpoint exists, end after 20 attempts
			if(currentD[0] == 2 || counter == 20)
			{
				cont = false;
			}else{
				checkpoints.add(findNextC(checkpoints.get(counter).getTile(), directions = findNextD(checkpoints.get(counter).getTile())));
			}
			counter++;
		}
	}
	
	private Checkpoint findNextC(Tile s, int[] dir)
	{
		Tile next = null;
		Checkpoint c = null;
		
		// Boolean to decide if next checkpoint is found
		boolean found = false;
		
		// Integer to increment each loop
		int counter = 1;
		
		while(!found)
		{
			if(s.getXPlace() + dir[0] * counter == grid.getTilesWide() 
					|| s.getYPlace() + dir[1] * counter == grid.getTilesHigh() 
					|| s.getType() != grid.getTile(s.getXPlace() + dir[0] * counter, s.getYPlace() + dir[1] * counter).getType())
			{
				found = true;
				// move counter back 1 to find tile before new tiletype
				counter -= 1;
				next = grid.getTile(s.getXPlace() + dir[0] * counter, s.getYPlace() + dir[1] * counter);
			}
			counter++;
		}
		c = new Checkpoint(next, dir[0], dir[1]);
		return c;
	}
	
	// Find next direction
	private int[] findNextD(Tile s)
	{
		int[] dir = new int[2];
		Tile u = grid.getTile(s.getXPlace(), s.getYPlace() - 1);
		Tile r = grid.getTile(s.getXPlace() + 1, s.getYPlace());
		Tile d = grid.getTile(s.getXPlace(), s.getYPlace() + 1);
		Tile l = grid.getTile(s.getXPlace() - 1, s.getYPlace());
		
		// Check if current inhabited tiletype matches above, right, down or left
		if(s.getType() == u.getType() && directions[1] != 1)
		{
			dir[0] = 0;
			dir[1] = -1;
		}else if(s.getType() == r.getType() && directions[0] != -1)
		{
			dir[0] = 1;
			dir[1] = 0;
		}else if(s.getType() == d.getType() && directions[1] != -1)
		{
			dir[0] = 0;
			dir[1] = 1;
		}else if(s.getType() == l.getType() && directions[0] != 1)
		{
			dir[0] = -1;
			dir[1] = 0;
		}else{
			dir[0] = 2;
			dir[1] = 2;
		}
		return dir;
	}
	
	
	// Take damage from external source
	public void damage(int amount)
	{
		health -= amount;
		if(health <= 0)
		{
			Player.modifyCash(5);
			die();
		}
			
	}
	
	private void die()
	{
		alive = false;
		if(light != null)
			lights.remove(light);
	}
	
	public void draw()
	{
		float healthPercentage = health / startHealth;
		// Enemy texture
		//drawQuadTex(texture, x, y, width, height);
		// Healthbar textures
		drawQuadTex(healthBackground, x, y - 16, width, 8);
		drawQuadTex(healthForeground, x, y - 16, TILE_SIZE * healthPercentage, 8);
		drawQuadTex(healthBorder, x, y - 16, width, 8);		
		if(light != null)
			light.setLocation(new Vector2f(getX() + getWidth()/2, getY() + getHeight()/2));
	}
	
	public void reduceHiddenHealth(float amount)
	{
		hiddenHealth -= amount;
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
		this.startHealth = health;
		this.hiddenHealth = health;
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
