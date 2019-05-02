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

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import framework.core.Handler;
import framework.core.TileGrid;
import framework.path.Graph;
import framework.path.Node;
import framework.shader.Light;
import object.LightSpot;
import object.enemy.Enemy_Spider;
import object.player.Player;

public class Leveler {
	
	public static TileGrid loadMap(Handler handler, String path, Graph graph)
	{
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
		TileGrid grid = new TileGrid(handler);
		
		//t_map.getTileImage(0, 0, 0).
		//System.out.println(t_map.getTileId(0, 0, t_map.getLayerIndex("Background")));
		TileSet set = t_map.getTileSet(0);
		SpriteSheet ss = set.tiles;
		list = new TileImageStorage(32, 32, ss.getWidth(), ss.getHeight(), "/level/tileset_00.png"); // "res/tiles/Itch release tileset.png"

		// Add Tiles
		for(int x = 0; x < TILES_WIDTH; x++){
			for(int y = 0; y < TILES_HEIGHT; y++){	
				for(int layer = 0; layer < t_map.getLayerCount(); layer++){
					
					int tileIndex = t_map.getTileId(x, y, layer);
					if(tileIndex > 0){
						// floor layer
						if(layer == 0){
							grid.setTile(x, y, list.getImage(tileIndex-1));	
							// add node to graph
							graph.addNode(new Node(x, y));
						}
						// wall layer
						if(layer == 1){
							grid.setTile(x, y, list.getImage(tileIndex-1));
							handler.obstacleList.add(grid.getTile(x, y));
							shadowObstacleList.add(grid.getTile(x, y));
						}
						// shadow layer
						if(layer == 2){
							grid.setTile(x, y, list.getImage(tileIndex-1));
							handler.obstacleList.add(grid.getTile(x, y));
							shadowObstacleList.add(grid.getTile(x, y));
						}
						// border layer
						if(layer == 3){
							grid.setTile(x, y, list.getImage(tileIndex-1));
							handler.obstacleList.add(grid.getTile(x, y));
						}
					}
				}
			}
		}
		
		// Add Objects
		int objectGroup = 0;
		// t_map.getObjectCount(0) -> 0 == Objectgroup
		for(int objectCount = 0; objectCount < t_map.getObjectCount(objectGroup); objectCount++){
			
			int x = t_map.getObjectX(objectGroup, objectCount);
			int y = t_map.getObjectY(objectGroup, objectCount);
			String objName = t_map.getObjectName(objectGroup, objectCount);
			//System.out.println(x + "  " + y + " " + objName);			
			if(objName.equals("player")){
				handler.setPlayer(new Player(x, y, handler));
			}	
			if(objName.equals("EnemySpider")){
				Enemy_Spider tmp = new Enemy_Spider(x, y, TILE_SIZE, TILE_SIZE, handler);
				handler.enemyList.add(tmp);
				shadowObstacleList.add(tmp);
				//handler.obstacleList.add(tmp);
			}
			if(objName.equals("LightBlue")){
				handler.lightSpotList.add(new LightSpot(x, y, new Light(new Vector2f(0, 0), 15, 25, 25, 10)));
			}
		}
		
		handler.setCurrentEntity(handler.getPlayer());
		
		System.out.println("Nodes: " + graph.countNodes());
		graph.createMatrix();
		return grid;
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
						tmpStr =  "<objectgroup name=\"Objects\" width=\"32\" height=\"32\">";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
