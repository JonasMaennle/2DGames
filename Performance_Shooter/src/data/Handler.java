package data;

import static Gamestate.StateManager.*;
import static helpers.Setup.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

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
	
	private StateManager statemanager;
	public CopyOnWriteArrayList<Tile> obstacleList;
	public CopyOnWriteArrayList<GunganEnemy> gunganList;
	private CopyOnWriteArrayList<ParticleEvent> eventList;
	
	public Player player;
	public AT_ST_Walker at_st_walker;
	public Speeder speeder;
	public Goal levelGoal;
	
	private long timer1, timer2;
	private TileGrid map;
	private Entity currentEntity;
	private UI gameUI;
	
	public Handler(StateManager statemanager)
	{
		this.statemanager = statemanager;
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.gunganList = new CopyOnWriteArrayList<>();
		this.eventList = new CopyOnWriteArrayList<>();
		
		this.player = null;
		this.at_st_walker = null;
		this.speeder = null;
		this.levelGoal = null;
		
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.currentEntity = null;
		this.gameUI = new UI();
		this.gameUI.addButton("Return", "intro/Return", (int)(getLeftBorder() + MOVEMENT_X) + 5, (int)(getTopBorder() + MOVEMENT_Y) + 5, 128, 64);
	}
	
	public void update()
	{
		// update current entity (player || at_st || speeder)
		if(currentEntity != null)
		{
			currentEntity.update();
			if(currentEntity.isOutOfMap())
				statemanager.resetCurrentLevel();
		}
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}

		// check current entity for collision with goal
		if(levelGoal != null && checkCollision(currentEntity.getX(), currentEntity.getY(), currentEntity.getWidth(), currentEntity.getHeight(), levelGoal.getX(), levelGoal.getY(), levelGoal.getWidth(), levelGoal.getHeight()))
		{
			//statemanager.loadLevel();
			if(lastState == GameState.EDITOR)StateManager.gameState = GameState.EDITOR;
			if(lastState != GameState.EDITOR)StateManager.gameState = GameState.LOADING;
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
		
		// Check if "Return" Button was clicked
		if(gameUI.getButton("Return") != null)
		{
			while(Mouse.next())
			{
				if(gameUI.isButtonClicked("Return"))
				{
					if(lastState == GameState.EDITOR)
					{
						StateManager.gameState = GameState.EDITOR;
						statemanager.getEditor().transmitDataFromHandler();
					}else{
						statemanager.getMainMenu().enterMainMenu();
						StateManager.gameState = GameState.MAINMENU;
					}
					lastState = GameState.GAME;
				}
			}
		}
		
		// Game information output in console
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
