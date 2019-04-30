package helper;

import static core.Constants.*;
import static helper.Graphics.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import static helper.Collection.*;

import org.mapeditor.core.MapObject;
import org.mapeditor.io.TMXMapReader;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;

import Entity.TileType;
import core.Handler;
import core.TileGrid;
import object.Enemy_Basic;
import object.Player;

public class Leveler {
	
	public static TileGrid loadMap(Handler handler, String path)
	{
		TiledMap t_map = null;
		try {
			t_map = new TiledMap("res/level/map_01_new.tmx", true);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		Image image = quickLoaderImage(path);
		//System.out.println(image.getResourceReference());
		int w = image.getWidth();
		int h = image.getHeight();
		
		
		TILES_WIDTH = t_map.getWidth();
		TILES_HEIGHT = t_map.getHeight();
		
		TileGrid grid = new TileGrid(handler);
		//System.out.println(t_map.getTileImage(0, 0, 0));
		//System.out.println(t_map.getTileId(0, 0, t_map.getLayerIndex("Background")));
		
		TileSet set = t_map.getTileSet(0);

		int sheetX = set.getTileX(73);
		int sheetY = set.getTileY(73);

		// public Image getTileImage(int tileID) { return this.tileSet.get(tileID); }
		Image tmpImg;// = set.tiles.getSprite(sheetX, sheetY);
		tmpImg = set.tiles.getSubImage(sheetX * 32, sheetY * 32, 32, 32);
		
		SpriteSheet ss = set.tiles; // 23 * 24 images
		tmpImg = ss.getSubImage(4, 4);
		
		
		for(int x = 0; x < TILES_WIDTH; x++)
		{
			for(int y = 0; y < TILES_HEIGHT; y++)
			{	
				//System.out.println(set.getTileX(73));
				switch (t_map.getTileId(x, y, t_map.getLayerIndex("Background"))) {
				case 73:
					grid.setTile(x, y, tmpImg);
					break;

				default:
					break;
				}
			}
		}
		handler.setPlayer(new Player(0, 0, handler));

		//System.out.println("w: " + w + " h: " + h);
//		for(int x = 0; x < w; x++)
//		{
//			for(int y = 0; y < h; y++)
//			{
//				Color c = image.getColor(x, y);
//				int red = c.getRed();
//				int green = c.getGreen();
//				int blue = c.getBlue();
//				
//		// Test Tiles
//				// Black -> Default Tile
//				if(red == 0 && green == 0 && blue == 0)
//				{
//					grid.setTile(x, y, TileType.Default);
//					handler.obstacleList.add(grid.getTile(x, y));
//					shadowObstacleList.add(grid.getTile(x, y));
//				}
//		// Player Tile
//				// Red -> Player
//				if(red == 255 && green == 0 && blue == 0)
//				{
//					handler.setPlayer(new Player(x * TILE_SIZE, y * TILE_SIZE, handler));
//				}
//		// Enemy	
//				// Blue -> Enemy
//				if(red == 0 && green == 0 && blue == 255)
//				{
//					Enemy_Basic tmp = new Enemy_Basic(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//					handler.enemyList.add(tmp);
//					//handler.obstacleList.add(tmp);
//					shadowObstacleList.add(tmp);
//				}
//		// Walls
//				// Bottom
//				if(red == 127 && green == 50 && blue == 0)
//				{
//					grid.setTile(x, y, TileType.WallBottom_01);
//					handler.obstacleList.add(grid.getTile(x, y));
//					//shadowObstacleList.add(grid.getTile(x, y));
//				}
//				// Bottom inside
//				if(red == 127 && green == 90 && blue == 0)
//				{
//					grid.setTile(x, y, TileType.WallBottom_02);
//					//handler.obstacleList.add(grid.getTile(x, y));
//					//shadowObstacleList.add(grid.getTile(x, y));
//				}
//				// Top
//				if(red == 64 && green == 64 && blue == 64)
//				{
//					grid.setTile(x, y, TileType.WallTop_01);
//					handler.obstacleList.add(grid.getTile(x, y));
//					shadowObstacleList.add(grid.getTile(x, y));
//				}
//		// Floor
//				// Default
//				if(red == 200 && green == 50 && blue == 0)
//				{
//					grid.setTile(x, y, TileType.Floor_01);
//					//handler.obstacleList.add(grid.getTile(x, y));
//					//shadowObstacleList.add(grid.getTile(x, y));
//				}
//			}
//		}
		
		handler.setCurrentEntity(handler.getPlayer());
		return grid;
	}
}
