package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.ui.InformationManager;
import object.Player;
import object.enemy.Enemy_Basic;

public class Handler {
	
	private InformationManager info_manager;
	private TileGrid map;
	private Player player;
	
	private float brightness;
	private Image filter, path;
	private long time1, time2;
	
	private float filterScale = 0.125f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	private float outOfScreenBorder;
	
	private CopyOnWriteArrayList<Enemy_Basic> enemyList;
	private CopyOnWriteArrayList<GameEntity> obstacleList;
	
	public Handler(){
		this.player = null;
		this.brightness = 0.35f;
		
		this.filter = quickLoaderImage("background/Filter");
		this.path = quickLoaderImage("tiles/path");
		this.info_manager = new InformationManager();
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		this.outOfScreenBorder = 64;
		
		this.enemyList = new CopyOnWriteArrayList<Enemy_Basic>();
		this.obstacleList = new CopyOnWriteArrayList<GameEntity>();
		
		setFogFilter(14);
	}
	
	public void update(){
		// update current entity 
		if(player != null){
			player.update();
		}
		
		for(Enemy_Basic e : enemyList){
			if(e.getX() > getLeftBorder() - outOfScreenBorder && e.getX() < getRightBorder() + outOfScreenBorder && e.getY() > getTopBorder() - outOfScreenBorder && e.getY() < getBottomBorder() + outOfScreenBorder){
				e.update();
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
		
		// draw enemy
		for(Enemy_Basic e : enemyList){
			if(e.getX() > getLeftBorder() - outOfScreenBorder && e.getX() < getRightBorder() + outOfScreenBorder && e.getY() > getTopBorder() - outOfScreenBorder && e.getY() < getBottomBorder() + outOfScreenBorder){
				e.draw();
			}
		}

		// draw player
		if(player != null)
			player.draw();

		// draw enemy path to player
		//drawPath();	
		
		// draw filter to darken the map
//		GL11.glColor4f(0, 0, 0, brightness);
//		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
//		GL11.glColor4f(1, 1, 1, 1);

		
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
		map = null;
		
		obstacleList.clear();
		enemyList.clear();
		lights.clear();
		
		info_manager.resetAll();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			System.out.println("Anzahl Tiles: " + map.getSetTileCounter() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lights.size() + "\tPlayer HP: " + PLAYER_HP+ " " + MOVEMENT_X);
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
	
	@SuppressWarnings("unused")
	private void colorReset(){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(255, 255, 255, 255);
		GL11.glDisable(GL_BLEND);	
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

	public CopyOnWriteArrayList<Enemy_Basic> getEnemyList() {
		return enemyList;
	}

	public CopyOnWriteArrayList<GameEntity> getObstacleList() {
		return obstacleList;
	}
}
