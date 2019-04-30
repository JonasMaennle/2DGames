package helper;

import static core.Constants.*;

import static helper.Collection.*;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import core.Handler;
import core.TileGrid;
import object.Enemy_Basic;
import object.Player;

public class Leveler {
	
	public static TileGrid loadMap(Handler handler, String path)
	{
		TileImageStorage list;	
		TiledMap t_map = null;
		
		try {
			t_map = new TiledMap(path + ".tmx");
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
		list = new TileImageStorage(32, 32, ss.getWidth(), ss.getHeight(), "res/tiles/tileset_00.png"); // "res/tiles/Itch release tileset.png"

		// Add Tiles
		for(int x = 0; x < TILES_WIDTH; x++){
			for(int y = 0; y < TILES_HEIGHT; y++){	
				for(int layer = 0; layer < t_map.getLayerCount(); layer++){
					
					int tileIndex = t_map.getTileId(x, y, layer);
					if(tileIndex > 0){
						// floor layer
						if(layer == 0){
							grid.setTile(x, y, list.getImage(tileIndex-1));	
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
			if(objName.equals("EnemyBasic")){
				Enemy_Basic tmp = new Enemy_Basic(x, y, TILE_SIZE, TILE_SIZE);
				handler.enemyList.add(tmp);
				shadowObstacleList.add(tmp);
			}
		}
		
		handler.setCurrentEntity(handler.getPlayer());
		return grid;
	}
}
