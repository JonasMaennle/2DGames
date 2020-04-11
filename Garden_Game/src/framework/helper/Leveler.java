package framework.helper;

import static framework.helper.Collection.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import framework.core.pathfinding.Graph;
import framework.core.pathfinding.Node;
import object.player.Player;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.TileGrid;

public class Leveler {
	
	public static TileGrid[] loadMap(StateManager stateManager, Handler handler, Graph graph, String path)
	{
		// disable in jar!
		prepareMap(path);
		
		TileImageStorage list;	
		TiledMap t_map = null;

		try {
			InputStream in = Leveler.class.getClassLoader().getResourceAsStream(path + ".tmx");
			//t_map = new TiledMap(in);
			t_map = new TiledMap(in, "level");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		TILES_WIDTH = t_map.getWidth();
		TILES_HEIGHT = t_map.getHeight();
		TileGrid[] map_layer = new TileGrid[4];
		map_layer[0] = new TileGrid(handler); // ground
		map_layer[1] = new TileGrid(handler); // hight
		map_layer[2] = new TileGrid(handler); // hight
		map_layer[3] = new TileGrid(handler); // tree
		
		//t_map.getTileImage(0, 0, 0).
		//System.out.println(t_map.getTileId(0, 0, t_map.getLayerIndex("Background")));
		TileSet set = t_map.getTileSet(0);
		SpriteSheet ss = set.tiles;
		list = new TileImageStorage(64, 64, ss.getWidth(), ss.getHeight(), "level/grid.png"); // "res/tiles/Itch release tileset.png"

		// Add Tiles
		for(int x = 0; x < TILES_WIDTH; x++){
			for(int y = 0; y < TILES_HEIGHT; y++){	
				for(int layer = 0; layer < t_map.getLayerCount(); layer++){
					
					int tileIndex = t_map.getTileId(x, y, layer);
					if(tileIndex > 0){
						int transformedX = (int) getIsometricCoordinates(x, y).x; // get real coordinates
						int transformedY = (int) getIsometricCoordinates(x, y).y;
						// ground layer
						if(layer == 0){
							map_layer[0].setTile(x, y, transformedX, transformedY, list.getImage(tileIndex-1), tileIndex-1);
							if(map_layer[0].getTile(x,y).getID() != 34)
								graph.addNode(new Node(x, y));
						}
						// hight layer
						if(layer == 1 || layer == 2){
							map_layer[layer].setTile(x, y, transformedX, transformedY, list.getImage(tileIndex-1), tileIndex-1);
							graph.addNode(new Node(x, y));
						}
					}
				}
			}
		}

		List<Integer> excludesNodeTiles = new ArrayList<>();
		excludesNodeTiles.add(93); // tree
		excludesNodeTiles.add(60); // tree
		// Add layer 2 Tiles
		for(int x = 0; x < TILES_WIDTH; x++){
			for(int y = 0; y < TILES_HEIGHT; y++){
				for(int layer = 0; layer < t_map.getLayerCount(); layer++){
					int tileIndex = t_map.getTileId(x, y, layer);
					if(tileIndex > 0){
						int transformedX = (int) getIsometricCoordinates(x, y).x; // get real coordinates
						int transformedY = (int) getIsometricCoordinates(x, y).y;
						// tree layer
						if(layer == 3){
							map_layer[3].setTile(x, y, transformedX, transformedY, list.getImage(tileIndex-1), tileIndex-1);
							// remove node if tree placed at tile
							if(graph.getNode(x,y) != null && excludesNodeTiles.contains(map_layer[3].getTile(x,y).getID())){
								graph.removeNode(graph.getNode(x + 1,y + 1));
							}
						}
					}
				}
			}
		}
		
		// Add Objects
		int objectGroup = 0;
		// t_map.getObjectCount(0) -> 0 == Objectgroup
		for(int objectCount = 0; objectCount < t_map.getObjectCount(objectGroup); objectCount++){
			int x = t_map.getObjectX(objectGroup, objectCount) + 32;
			int y = t_map.getObjectY(objectGroup, objectCount);

			// transform coordinates in grid coords
			int tmpX = (x / (TILE_SIZE / 2));     //(x * (TILE_SIZE/2) - y * (TILE_SIZE/2)) / TILE_SIZE; // transform coordinates
			int tmpY = (y / (TILE_SIZE / 2)) + 1; //(x * (TILE_SIZE/2) + y * (TILE_SIZE/2)) / TILE_SIZE;

			// transform grid coords to map coords
			int transformedX = (int) getIsometricCoordinates(tmpX, tmpY).x; // get real coordinates
			int transformedY = (int) getIsometricCoordinates(tmpX, tmpY).y;

			String objName = t_map.getObjectName(objectGroup, objectCount);
			System.out.println("Object: " + objName + "  X: " + tmpX + " / Y: " + tmpY);
			if(objName.equals("player")){
				handler.setPlayer(new Player(transformedX + 16, transformedY - (TILE_SIZE/2),24,48, handler));
			}

//			if(objName.equals("enemy_orange")){
//				Enemy_Orange tmp = new Enemy_Orange(x, y, TILE_SIZE, TILE_SIZE, handler);
//				handler.getEnemyList().add(tmp);
//				shadowObstacleList.add(tmp);
//			}
		}
		graph.createMatrix();
		handler.setGraph(graph);
		stateManager.getGame().getCamera().reset();
		return map_layer;
	}
	
	private static void prepareMap(String path){
		// parse .tmx file -> Object issue
		StringBuffer buffer = null;
		try {
			File f = new File("res/" + path + ".tmx");
			InputStream is = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			buffer = new StringBuffer();
			String tmpStr;
			try {
				while((tmpStr = reader.readLine()) != null){
					if(tmpStr.contains("Objects")){
						System.out.println("added object info to map");
						tmpStr =  "<objectgroup name=\"Objects\" width=\"64\" height=\"64\">";
						buffer.append(tmpStr + "\n");
					}else{
						buffer.append(tmpStr + "\n");
					}
					tmpStr = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Write File
		try {
			File f = new File("res/" + path + ".tmx");
			OutputStream os = new FileOutputStream(f);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
			
			try {
				writer.write(buffer.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("map prepared");
	}
}
