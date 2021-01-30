package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.util.concurrent.CopyOnWriteArrayList;

import framework.core.pathfinding.Graph;
import object.buildings.Building;
import object.buildings.Stock;
import object.farming.Field;
import object.trees.Tree;
import object.player.Player;
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
	private CopyOnWriteArrayList<Tree> treeList;
	private CopyOnWriteArrayList<Field> fieldList;
	private CopyOnWriteArrayList<Building> buildingList;
	private CopyOnWriteArrayList<Player> playerList;
	private Graph graph;

	private Player selectedPlayer;
	
	public Handler(){
		this.enemySpawnManager = new EnemySpawnManager(this);
		this.particleManager = new ParticleManager();
		this.filter = quickLoaderImage("background/background");
		this.info_manager = InformationManager.getInstance();
		this.time1 = System.currentTimeMillis();
		this.time2 = time1;
		this.outOfScreenBorder = 512;
		
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.treeList = new CopyOnWriteArrayList<>();
		this.fieldList = new CopyOnWriteArrayList<>();
		this.buildingList = new CopyOnWriteArrayList<>();
		this.playerList = new CopyOnWriteArrayList<>();
	}
	
	public void update(){	
		particleManager.update();
		enemySpawnManager.update();
		info_manager.update();
		for(Player p : playerList)
			p.update();

		for(Tree t : treeList){
			if(t.getWoodLeft() == 0){
				treeList.remove(t);
			}
		}

		for(Field field : fieldList)
			field.update();

		objectInfo();
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
		mapLayers[3].draw();

		for(Field field : fieldList)
			field.draw();

		for(Building b : buildingList){
			b.draw();
		}

		// mapLayers[4].draw(); // color graph layer
		for(Tree tree: treeList){
			boolean transparent = false;
			for(Player player : playerList){
				if(tree.getTranparencyBounds().intersects(player.getTotalBounds())){
					transparent = true;
				}
			}
			if(transparent)
				tree.drawTransparent();
			else
				tree.draw();
		}
		for(Player player : playerList)
			player.draw();

		renderLightEntity(shadowObstacleList);
		
		particleManager.draw();
		info_manager.draw();
	}
	
	public void wipe(){
		mapLayers = null;
		obstacleList.clear();
		lightsTopLevel.clear();
		lightsSecondLevel.clear();
		treeList.clear();
		info_manager.resetAll();
		buildingList.clear();
		playerList.clear();
		fieldList.clear();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo(){
		time1 = System.currentTimeMillis();
		if(time1 - time2 > 2000)
		{
			time2 = time1;
			// Data output
			int tileCount = mapLayers[0].getSetTileCounter();
			String playerTasks = selectedPlayer == null ? "" : "Selected Player ";
			if(getSelectedPlayer() != null) playerTasks += getSelectedPlayer().printTaskList();

			System.out.println("Accessible Tiles: " + tileCount + "\tFPS: " + StateManager.framesInLastSecond + "\t" + playerTasks);
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

	public Graph getGraph() { return graph; }

	public void setGraph(Graph graph) { this.graph = graph; }

	public CopyOnWriteArrayList<Tree> getTreeList() {
		return treeList;
	}

	public void setTreeList(CopyOnWriteArrayList<Tree> treeList) {
		this.treeList = treeList;
	}

	public CopyOnWriteArrayList<Building> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(CopyOnWriteArrayList<Building> buildingList) {
		this.buildingList = buildingList;
	}

	public CopyOnWriteArrayList<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(CopyOnWriteArrayList<Player> playerList) {
		this.playerList = playerList;
	}

	public Player getSelectedPlayer() {
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer) {
		this.selectedPlayer = selectedPlayer;
	}

	public TileGrid getLayer(int layer){
		return mapLayers[layer];
	}

	public CopyOnWriteArrayList<Field> getFieldList() {
		return fieldList;
	}

	public void setFieldList(CopyOnWriteArrayList<Field> fieldList) {
		this.fieldList = fieldList;
	}
}
