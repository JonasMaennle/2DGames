package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import framework.core.pathfinding.Graph;
import object.player.Player;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.ui.InformationManager;

public class Handler {
	
	private InformationManager info_manager;
	private TileGrid[] mapLayers;
	private ParticleManager particleManager;
	private EnemySpawnManager enemySpawnManager;
	
	//private float brightness;
	private Image filter;
	private long time1, time2;
	
	private float filterScale = 0.125f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	private float outOfScreenBorder;
	
	private CopyOnWriteArrayList<GameEntity> obstacleList;
	private Player player;
	private Graph graph;
	
	public Handler(){
		this.enemySpawnManager = new EnemySpawnManager(this);
		this.particleManager = new ParticleManager();
		this.filter = quickLoaderImage("background/background");
		this.info_manager = InformationManager.getInstance();
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		this.outOfScreenBorder = 512;
		
		this.obstacleList = new CopyOnWriteArrayList<GameEntity>();
		this.player = null;
	}
	
	public void update(){	
		particleManager.update();
		enemySpawnManager.update();
		info_manager.update();
		if(player != null) player.update();

		// objectInfo();
	}
	
	public void draw(){
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;

		// draw tile map
		/*
		for(TileGrid layer : mapLayers) {
			layer.draw();
		}
		*/
		mapLayers[0].draw();
		mapLayers[1].draw();
		mapLayers[2].draw();
		if(player != null) player.draw();
		mapLayers[3].draw();

		renderLightEntity(shadowObstacleList);
		
		particleManager.draw();
		info_manager.draw();
	}
	
	public void wipe(){
		mapLayers = null;
		
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
			int tileCount = 0;
			for(TileGrid grid : mapLayers){
				tileCount += grid.getSetTileCounter();
			}
			System.out.println("Accessible Tiles: " + tileCount + "\tFPS: " + StateManager.framesInLastSecond );
		}
	}

	public TileGrid[] getMaps() {
		return mapLayers;
	}

	public void setMaps(TileGrid[] map) {
		this.mapLayers = map;
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

	public Player getPlayer() { return player; }

	public void setPlayer(Player player) { this.player = player; }

	public Graph getGraph() { return graph; }

	public void setGraph(Graph graph) { this.graph = graph; }
}
