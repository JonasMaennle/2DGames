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
import object.TowerPlayer;

public class Handler {
	
	private InformationManager info_manager;
	private TileGrid map;
	private ParticleManager particleManager;
	private EnemySpawnManager enemySpawnManager;
	
	//private float brightness;
	private Image filter;
	private long time1, time2;
	
	private float filterScale = 0.125f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	private float outOfScreenBorder;
	
	private TowerPlayer towerPlayer;
	
	private CopyOnWriteArrayList<GameEntity> obstacleList;
	
	public Handler(){
		//this.brightness = 0.35f;
		
		this.enemySpawnManager = new EnemySpawnManager(this);
		this.particleManager = new ParticleManager();
		this.filter = quickLoaderImage("background/background");
		this.info_manager = InformationManager.getInstance();
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		this.outOfScreenBorder = 512;
		
		this.obstacleList = new CopyOnWriteArrayList<GameEntity>();

		setFogFilter(24);
	}
	
	public void update(){	
		
		if(towerPlayer != null)
			towerPlayer.update();
		
		particleManager.update();
		enemySpawnManager.update();
		info_manager.update();
		objectInfo();
	}
	
	public void draw(){
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;

		// draw tile map
		map.draw();
		renderLightEntity(shadowObstacleList);
		
		particleManager.draw();
		
		if(towerPlayer != null)
			towerPlayer.draw();
		
		// draw filter to darken the map
//		GL11.glColor4f(0, 0, 0, brightness);
//		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
//		GL11.glColor4f(1, 1, 1, 1);


		// render light list
		

		// draw fog of war
//		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
//			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
//				GL11.glColor4f(0, 0, 0, alphaFilter[y][x]);
//				drawQuadImageStatic(filter, (x*32), (y*32), 32, 32);
//				GL11.glColor4f(1, 1, 1, 1);
//			}
//		}
		
		renderLightList(lightsTopLevel);
		
		info_manager.draw();
	}
	
	public void wipe(){
		map = null;
		
		obstacleList.clear();
		lightsTopLevel.clear();
		lightsSecondLevel.clear();

		info_manager.resetAll();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			System.out.println("Anzahl Tiles: " + map.getSetTileCounter() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lightsSecondLevel.size());
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

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}

	public InformationManager getInfo_manager() {
		return info_manager;
	}


	public CopyOnWriteArrayList<GameEntity> getObstacleList() {
		return obstacleList;
	}


	public ParticleManager getParticleManager() {
		return particleManager;
	}

	public TowerPlayer getTowerPlayer() {
		return towerPlayer;
	}

	public void setTowerPlayer(TowerPlayer towerPlayer) {
		this.towerPlayer = towerPlayer;
	}
}
