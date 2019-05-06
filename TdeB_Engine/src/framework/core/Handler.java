package framework.core;

import object.LightSpot;
import object.collectable.Collectable_Basic;
import object.enemy.Enemy_Basic;
import object.player.Player;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;

public class Handler {
	
	public CopyOnWriteArrayList<GameEntity> obstacleList;
	public CopyOnWriteArrayList<Enemy_Basic> enemyList;
	public CopyOnWriteArrayList<LightSpot> lightSpotList;
	public CopyOnWriteArrayList<Collectable_Basic> collectableList;
	
	private TileGrid map;
	private GameEntity currentEntity;
	private Player player;
	
	private float brightness;
	private Image filter, path;
	private long time1, time2;
	
	private float filterScale = 0.125f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	
	public Handler(){
		this.currentEntity = null;
		this.player = null;
		this.brightness = 0.5f;
		
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.lightSpotList = new CopyOnWriteArrayList<>();
		this.collectableList = new CopyOnWriteArrayList<>();
		
		this.filter = quickLoaderImage("background/Filter");
		this.path = quickLoaderImage("tiles/path");
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		
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
			if(c.getX() > getLeftBorder() && c.getX() < getRightBorder() && c.getY() > getTopBorder() && c.getY() < getBottomBorder()){
				c.update();
			}	
		}
		
		// update enemy
		for(Enemy_Basic e : enemyList){
			if(e.getX() > getLeftBorder() && e.getX() < getRightBorder() && e.getY() > getTopBorder() && e.getY() < getBottomBorder()){
				e.update();
				if(e.getHp() <= 0){
					e.die();
					enemyList.remove(e);
					shadowObstacleList.remove(e);
				}
			}

		}
		
		objectInfo();
	}
	
	public void draw(){
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;
		
		// draw tile map
		map.draw();
		
		// draw player
		if(player != null && currentEntity.equals(player))
			player.draw();
		
		// draw collectables
		for(Collectable_Basic c : collectableList){
			if(c.getX() > getLeftBorder() && c.getX() < getRightBorder() && c.getY() > getTopBorder() && c.getY() < getBottomBorder()){
				c.draw();
			}	
		}

		// draw enemy
		for(Enemy_Basic e : enemyList){
			if(e.getX() > getLeftBorder() && e.getX() < getRightBorder() && e.getY() > getTopBorder() && e.getY() < getBottomBorder()){
				e.draw();
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
	}
	
	public void wipe(){
		player = null;
		currentEntity = null;
		map = null;
		
		obstacleList.clear();
		enemyList.clear();
		lightSpotList.clear();
		collectableList.clear();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			System.out.println("Anzahl Tiles: " + map.getSetTileCounter() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lights.size() + "\tMovementX: " + MOVEMENT_X);
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
