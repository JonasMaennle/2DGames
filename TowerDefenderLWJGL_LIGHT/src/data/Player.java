package data;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import helpers.Clock;

import static helpers.Artist.*;

import java.util.ArrayList;

public class Player {
	
	private TileGrid grid;
	private WaveManager waveManager;
	private ArrayList<Tower> towerList;
	private boolean leftMouseButtonDown, rightMouseButtonDown, holdingTower;
	private Tower tempTower;
	public static int cash, lives;
	
	public Player(TileGrid grid, WaveManager waveManager)
	{
		this.grid = grid;
		this.waveManager = waveManager;
		this.towerList = new ArrayList<>();
		this.leftMouseButtonDown = false;
		this.rightMouseButtonDown = false;
		this.holdingTower = false;
		this.tempTower = null;
		cash = 0;
		lives = 0;
	}
	
	public void update()
	{
		// Update holding tower
		if(holdingTower)
		{
			tempTower.setX(getMouseTile().getX());
			tempTower.setY(getMouseTile().getY());
			tempTower.draw();
		}
		
		
		// Update all towers in the game
		for(Tower t : towerList)
		{
			t.update();
			t.updateEnemyList(waveManager.getCurrentWave().getEnemyList());
		}
		
		// Handle Mouse Input
		if(Mouse.isButtonDown(0) && !leftMouseButtonDown)
		{
			placeTower();
		}
			
		if(Mouse.isButtonDown(1) && !rightMouseButtonDown)
		{
			System.out.println("empty right click");
		}
		
		leftMouseButtonDown = Mouse.isButtonDown(0);
		rightMouseButtonDown = Mouse.isButtonDown(1);
		
		// Handle Keyboad Input
		while(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState())
			{
				Clock.changeMultiplier(-0.2f);
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState())
			{
				Clock.changeMultiplier(0.2f);
			}
		}
	}
	
	// Initialize Cash and Lives values for Player
	public void setup()
	{
		cash = 300;
		lives = 10;
	}
	
	// Check if player can afford tower, if so process transaction
	public static boolean modifyCash(int amount)
	{
		if(cash + amount >= 0)
		{
			cash += amount;
			//System.out.println("Current Cash: " + cash);
			return true;
		}
		return false;
	}
	
	public static void modifyLives(int amount)
	{
		lives += amount;
	}
	
	private void placeTower()
	{
		Tile currentTile = getMouseTile();
		if(holdingTower)
			if(!currentTile.getOccupied() && modifyCash(-tempTower.getCost()))
			{
				towerList.add(tempTower);
				tempTower.setPlaced(true);
				currentTile.setOccupied(true);
				holdingTower = false;
				tempTower = null;
			}
	}
	
	public void pickTower(Tower t)
	{
		tempTower = t;
		holdingTower = true;
	}
	
	public Tile getMouseTile()
	{
		if(Mouse.getX() >= GAME_WIDTH && Mouse.getY() < HEIGHT && Mouse.getY() > 0)
		{
			return grid.getTile(((GAME_WIDTH-TILE_SIZE) / TILE_SIZE), ((HEIGHT - Mouse.getY() - 1)/TILE_SIZE));
		}
		
		return grid.getTile((Mouse.getX() / TILE_SIZE), ((HEIGHT - Mouse.getY() - 1)/TILE_SIZE));
	}

	public ArrayList<Tower> getTowerList() 
	{
		return towerList;
	}
}
