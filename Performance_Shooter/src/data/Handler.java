package data;

import java.util.ArrayList;
import static helpers.Artist.*;
import java.util.concurrent.CopyOnWriteArrayList;

import helpers.StateManager;
import object.Goal;
import object.GunganEnemy;
import object.Player;
import static helpers.StateManager.*;

public class Handler {
	
	public ArrayList<Tile> obstacleList = new ArrayList<>();
	public CopyOnWriteArrayList<GunganEnemy> gunganList = new CopyOnWriteArrayList<>();
	public Player player;
	public Goal levelGoal;
	private long timer1, timer2;
	private TileGrid map;
	private StateManager statemanager;
	
	public Handler(StateManager statemanager)
	{
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.statemanager = statemanager;
	}
	
	public void update()
	{
		// update Player
		player.update();
		
		// update Level Goal
		if(levelGoal != null)
			levelGoal.update();
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}
		
		if(levelGoal != null && checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), levelGoal.getX(), levelGoal.getY(), levelGoal.getWidth(), levelGoal.getHeight()))
		{
			statemanager.loadLevel();
		}
		
		objectInfo();
	}
	
	public void draw()
	{
		// draw tile map
		map.draw();
		
		// draw player
		if(gameState != GameState.DEAD)
			player.draw();
		
		// draw gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.draw();
		}
		
		// draw Level Goal
		if(levelGoal != null)
			levelGoal.draw();
	}
	
	private void objectInfo()
	{
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 2000)
		{
			timer2 = timer1;
			// Data output
			System.out.println("Anzahl Tiles: " + obstacleList.size() + "\tAnzahl Enemies: " + gunganList.size() + "\tFPS: " + StateManager.framesInLastSecond);
		}
	}
	
	public void wipe()
	{
		player = null;
		gunganList.clear();
		obstacleList.clear();
		levelGoal = null;
	}

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}

	public StateManager getStatemanager() {
		return statemanager;
	}

	public void setStatemanager(StateManager statemanager) {
		this.statemanager = statemanager;
	}
	
	
}
