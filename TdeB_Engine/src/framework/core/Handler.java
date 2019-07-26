package framework.core;

import object.LightSpot;
import object.Spawner;
import object.collectable.Collectable_Basic;
import object.enemy.Enemy_Basic;
import object.player.Player;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import framework.core.StateManager.GameState;
import framework.entity.GameEntity;
import framework.ui.InformationManager;

public class Handler {
	
	public CopyOnWriteArrayList<GameEntity> obstacleList;
	public CopyOnWriteArrayList<Enemy_Basic> enemyList;
	public CopyOnWriteArrayList<LightSpot> lightSpotList;
	public CopyOnWriteArrayList<Collectable_Basic> collectableList;
	
	public CopyOnWriteArrayList<Spawner> spawnPoints;
	
	private InformationManager info_manager;
	private TileGrid map;
	private GameEntity currentEntity;
	private Player player;
	
	private float brightness;
	private Image filter, path;
	private long time1, time2;
	
	private int enemiesLeft;
	private int enemiesKilled;
	private int maxEnemies;
	
	private float filterScale = 0.125f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	
	private int outOfScreenBorder;
	
	public Handler(){
		this.currentEntity = null;
		this.player = null;
		this.brightness = 0.35f;
		
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.lightSpotList = new CopyOnWriteArrayList<>();
		this.collectableList = new CopyOnWriteArrayList<>();
		
		this.spawnPoints = new CopyOnWriteArrayList<>();
		
		this.filter = quickLoaderImage("background/Filter");
		this.path = quickLoaderImage("tiles/path");
		this.info_manager = new InformationManager(this);
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		this.outOfScreenBorder = 64;
		
		this.enemiesLeft = 0;
		this.enemiesKilled = 0;
		this.maxEnemies = 0;
		
		setFogFilter(0);
	}
	
	public void update(){
		// update current entity 
		if(currentEntity != null){
			currentEntity.update();
		}
		
		// update SpotLights
		for(LightSpot spot : lightSpotList){
			spot.update();
		}
		
		// update collectables
		for(Collectable_Basic c : collectableList){
			//if(c.getX() > getLeftBorder() - outOfScreenBorder && c.getX() < getRightBorder() + outOfScreenBorder && c.getY() > getTopBorder() - outOfScreenBorder && c.getY() < getBottomBorder() + outOfScreenBorder){
				c.update();
			//}	
		}
		
		// update enemy
		if(StateManager.gameMode == GameState.GAME) {
			for(Enemy_Basic e : enemyList){
				if(e.getX() > getLeftBorder() - outOfScreenBorder && e.getX() < getRightBorder() + outOfScreenBorder && e.getY() > getTopBorder() - outOfScreenBorder && e.getY() < getBottomBorder() + outOfScreenBorder){
					e.update();
					if(e.getHp() <= 0){
						e.die();
						enemyList.remove(e);
						shadowObstacleList.remove(e);
						enemiesKilled++;
						enemiesLeft = enemyList.size();
					}
				}else if(e.getSpeed() != 0){
					e.update();
					if(e.getHp() <= 0){
						e.die();
						enemyList.remove(e);
						shadowObstacleList.remove(e);
						enemiesKilled++;
						enemiesLeft = enemyList.size();
					}
				}
			}
		}else if(StateManager.gameMode == GameState.ARENA) {
			for(Enemy_Basic e : enemyList){

				e.update();
				if(e.getHp() <= 0){
					e.die();
					enemyList.remove(e);
					shadowObstacleList.remove(e);
					enemiesKilled++;
					enemiesLeft = enemyList.size();
				}
			}
		}
		
		
		info_manager.update();
		objectInfo();
	}
	
	public void draw(){
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;

		// draw tile map
		map.draw();
		
		// reset color values
		if(PLAYER_HP < 26)
			colorReset();
		
		// draw enemy
		for(Enemy_Basic e : enemyList){
			if(e.getX() > getLeftBorder() - outOfScreenBorder && e.getX() < getRightBorder() + outOfScreenBorder && e.getY() > getTopBorder() - outOfScreenBorder && e.getY() < getBottomBorder() + outOfScreenBorder){
				e.draw();
			}
		}

		// draw player
		if(player != null && currentEntity.equals(player))
			player.draw();
		
		// draw collectables
		for(Collectable_Basic c : collectableList){
			if(c.getX() > getLeftBorder() - outOfScreenBorder && c.getX() < getRightBorder() + outOfScreenBorder && c.getY() > getTopBorder() - outOfScreenBorder && c.getY() < getBottomBorder() + outOfScreenBorder){
				c.draw();
			}	
		}

		// draw enemy path to player
		//drawPath();	
		
		// draw filter to darken the map
		GL11.glColor4f(0, 0, 0, brightness);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);

		
		// render light list
		renderLightEntity(shadowObstacleList);

		// draw fog of war
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
				GL11.glColor4f(0, 0, 0, alphaFilter[y][x]);
				drawQuadImageStatic(filter, (x*32), (y*32), 32, 32);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
		
		info_manager.draw();
	}
	
	public void wipe(){
		player = null;
		currentEntity = null;
		map = null;
		
		obstacleList.clear();
		enemyList.clear();
		lightSpotList.clear();
		collectableList.clear();
		lights.clear();
		
		enemiesKilled = 0;
		enemiesLeft = 0;
		
		spawnPoints.clear();		
		info_manager.resetAll();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			System.out.println("Anzahl Tiles: " + map.getSetTileCounter() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lights.size() + "\tPlayer HP: " + PLAYER_HP);
		}
	}
	
	@SuppressWarnings("unused")
	private void drawPath(){
		for(Enemy_Basic e : enemyList){
			for(int i = 0; i < e.getPath().size(); i++){
				drawQuadImage(path, e.getPath().get(i).getX() * TILE_SIZE, e.getPath().get(i).getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
	}
	
	public void setFogFilter(int filterPlayerOffset){
		filterValue = 0;
		alphaFilter = new float[HEIGHT/TILE_SIZE + 1][(WIDTH/TILE_SIZE) + 1]; 		
		// fill array with value = 1
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
				alphaFilter[y][x] = 1;
			}
		}
		
		// render filter for topFilterObstacle (objects in the fog of war)
		for(int i = 0; i < WIDTH/TILE_SIZE; i++){
			alphaFilter = drawCircle(WIDTH/2/TILE_SIZE, HEIGHT/2/TILE_SIZE, i, alphaFilter);
			if(i > filterPlayerOffset)filterValue += filterScale;
		}
	}
	
	private float[][] drawCircle(int x, int y, int r, float[][] array) {
	    double angle, x1, y1;

	    int arrayWidth = array[0].length;
	    int arrayHeight = array.length;
	    
	    for (int i = 0; i < 360; i++) {
	        angle = i;
	        x1 = r * Math.cos(angle * Math.PI / 180);
	        y1 = r * Math.sin(angle * Math.PI / 180);

	        int ElX = (int) Math.round(x + x1);
	        int ElY = (int) Math.round(y + y1);
	        if(ElX < arrayWidth && ElX >= 0 && ElY < arrayHeight && ElY >= 0)
	        	array[ElY][ElX] = filterValue;
	    }  
	    return array;
	}
	
	private void colorReset(){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(255, 255, 255, 255);
		GL11.glDisable(GL_BLEND);	
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

	public InformationManager getInfo_manager() {
		return info_manager;
	}

	public int getEnemiesLeft() {
		return enemiesLeft;
	}

	public int getEnemiesKilled() {
		return enemiesKilled;
	}

	public int getMaxEnemies() {
		return maxEnemies;
	}

	public void setMaxEnemies(int maxEnemies) {
		this.maxEnemies = maxEnemies;
	}
}
