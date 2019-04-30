package core;

import Entity.GameEntity;
import object.Enemy_Basic;
import object.Player;

import static core.Constants.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class Handler {
	
	public CopyOnWriteArrayList<GameEntity> obstacleList;
	public CopyOnWriteArrayList<Enemy_Basic> enemyList;
	
	private TileGrid map;
	private GameEntity currentEntity;
	private Player player;
	
	public Handler(){
		this.currentEntity = null;
		this.player = null;
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
	}
	
	public void update(){
		// update current entity 
		if(currentEntity != null)
		{
			currentEntity.update();
		}
		
		// update enemy
		for(Enemy_Basic e : enemyList){
			e.update();
		}
	}
	
	public void draw(){
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;
		
		// draw tile map
		map.draw();
		
		// draw player
		if(StateManager.gameState != StateManager.GameState.DEATHSCREEN && player != null && currentEntity.equals(player))
			player.draw();
		
		// draw enemy
		for(Enemy_Basic e : enemyList){
			e.draw();
		}
	}
	
	public void wipe()
	{
		player = null;
		currentEntity = null;
		map = null;
		obstacleList.clear();
		enemyList.clear();
	}

	public GameEntity getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(GameEntity currentEntity) {
		this.currentEntity = currentEntity;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}
}
