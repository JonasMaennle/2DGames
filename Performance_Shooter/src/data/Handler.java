package data;

import static Gamestate.StateManager.*;
import static helpers.Setup.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager;
import UI.UI;
import object.AT_ST_Walker;
import object.Goal;
import object.GunganEnemy;
import object.Player;
import object.Speeder;

public class Handler {
	
	public CopyOnWriteArrayList<Tile> obstacleList = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<GunganEnemy> gunganList = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<ParticleEvent> eventList;
	
	public Player player;
	public AT_ST_Walker at_st_walker;
	public Speeder speeder;
	
	public Goal levelGoal;
	private long timer1, timer2;
	private TileGrid map;
	private StateManager statemanager;
	private Entity currentEntity;
	private UI gameUI;
	
	
	public Handler(StateManager statemanager)
	{
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.statemanager = statemanager;
		this.eventList = new CopyOnWriteArrayList<>();
		this.gameUI = new UI();
	}
	
	public void update()
	{
		// if player exists -> update
		if(player != null && currentEntity.equals(player))
		{
			player.update();

			if(player.isOutOfMap())
				statemanager.resetCurrentLevel();	
		}
		
		// if at_st exists -> update
		if(at_st_walker != null)
		{
			at_st_walker.update();
			if(at_st_walker.isOutOfMap())
				statemanager.resetCurrentLevel();
		}	
		// if speeder exitsts -> update
		if(speeder != null)
		{
			speeder.update();
			if(speeder.isOutOfMap())
				statemanager.resetCurrentLevel();
		}

		// update Level Goal
		if(levelGoal != null)
			levelGoal.update();
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}

		// check player collision with level goal
		if(levelGoal != null && checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), levelGoal.getX(), levelGoal.getY(), levelGoal.getWidth(), levelGoal.getHeight()))
		{
			//statemanager.loadLevel();
			StateManager.gameState = GameState.LOADING;
		}
		
		// update map
		for(Tile t : obstacleList)
		{
			if(t.getType() == TileType.Grass_Round_Half)
			{
				t.update();
			}
		}
		
		// update particle if tile is dead
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				event.update();
			}
		}
		
		// If level was created in editor
		if(gameUI.isButtonClicked("ReturnToEditor"))
		{
			StateManager.gameState = GameState.EDITOR;
			statemanager.getEditor().transmitDataFromHandler();
		}

		objectInfo();
	}
	
	public void draw()
	{
		// draw tile map
		map.draw();
		
		// draw player
		if(gameState != GameState.DEAD && player != null && currentEntity.equals(player))
			player.draw();
		
		// draw at st
		if(at_st_walker != null)
			at_st_walker.draw();
		
		// draw speeder
		if(speeder != null)
			speeder.draw();
		
		// draw gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.draw();
		}
		
		// draw Level Goal
		if(levelGoal != null)
			levelGoal.draw();
		
		//draw particles if tile dies
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				event.draw();
			}
		}
		
		// draw Buttons
		gameUI.draw();
	}
	
	//@SuppressWarnings("unused")
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
		at_st_walker = null;
		speeder = null;
		levelGoal = null;
		gunganList.clear();
		obstacleList.clear();
		eventList.clear();
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

	public Entity getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(Entity currentEntity) {
		this.currentEntity = currentEntity;
	}
	
	public void addParticleEvent(ParticleEvent event){
		eventList.add(event);
	}

	public UI getGameUI() {
		return gameUI;
	}

	public void setGameUI(UI gameUI) {
		this.gameUI = gameUI;
	}
}
