package helpers;

import java.awt.image.BufferedImage;

import static Gamestate.StateManager.*;
import static helpers.Artist.*;
import java.io.IOException;
import javax.imageio.ImageIO;

import Enity.TileType;
import data.Handler;
import data.TileGrid;
import object.Goal;
import object.GunganEnemy;
import object.Player;

public class Leveler {
	
	public static TileGrid loadMap(Handler handler, int level)
	{
		TileGrid grid = new TileGrid();
		BufferedImage image = bufferedImageLoader("/maps/map_" + level +".png");
		
		int w = image.getWidth();
		int h = image.getHeight();
		//System.out.println("w: " + w + " h: " + h);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				int pixel = image.getRGB(x, y);			
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
		// Grass Tiles
				// Black -> Grass_Flat
				if(red == 0 && green == 0 && blue == 0)
				{
					grid.setTile(x, y, TileType.Grass_Flat);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Left
				if(red == 64 && green == 64 && blue == 64)
				{
					grid.setTile(x, y, TileType.Grass_Left);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Right
				if(red == 48 && green == 48 && blue == 48)
				{
					grid.setTile(x, y, TileType.Grass_Right);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Round
				if(red == 128 && green == 128 && blue == 128)
				{
					grid.setTile(x, y, TileType.Grass_Round);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Round_Half -> velX = 1
				if(red == 100 && green == 50 && blue == 50)
				{
					grid.setTile(x, y, TileType.Grass_Round_Half);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Round_Half -> velX = -1
				if(red == 100 && green == 50 && blue == 100)
				{
					grid.setTile(x, y, TileType.Grass_Round_Half);
					grid.getTile(x, y).setVelX(-1);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Left_Half
				if(red == 150 && green == 50 && blue == 50)
				{
					grid.setTile(x, y, TileType.Grass_Left_Half);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Right_Half
				if(red == 150 && green == 50 && blue == 70)
				{
					grid.setTile(x, y, TileType.Grass_Right_Half);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Flat_Half
				if(red == 150 && green == 70 && blue == 70)
				{
					grid.setTile(x, y, TileType.Grass_Flat_Half);
					handler.obstacleList.add(grid.getTile(x, y));
				}
		// Dirt Tile
				// Grey -> Dirt_Basic
				if(red == 127 && green == 50 && blue == 0)
				{
					grid.setTile(x, y, TileType.Dirt_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
				}
		// Rock Tile
				// Grey -> Rock_Basic
				if(red == 196 && green == 196 && blue == 196)
				{
					grid.setTile(x, y, TileType.Rock_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				
		// Player Tile
				// Red -> Player
				if(red == 255 && green == 0 && blue == 0)
				{
					handler.player = new Player(x * TILE_SIZE, y * TILE_SIZE, handler);
				}
		// Enemy Tile
				// Green -> GunganEnemy
				if(red == 0 && green == 255 && blue == 0)
				{
					handler.gunganList.add(new GunganEnemy(x * TILE_SIZE, y * TILE_SIZE, 64, 128, handler));
				}
				if(red == 0 && green == 255 && blue == 120)
				{
					handler.gunganList.add(new GunganEnemy(x * TILE_SIZE, y * TILE_SIZE, 64, 128, handler, 2));
				}
		// Goal Tile
				// Pink -> Level Goal
				if(red == 255 && green == 0 && blue == 255)
				{
					handler.levelGoal = new Goal(x * TILE_SIZE, y * TILE_SIZE);
				}
			}
		}
		return grid;
	}
	
	public static BufferedImage bufferedImageLoader(String path)
	{
		BufferedImage image = null;
		try {
			image = ImageIO.read(Leveler.class.getClass().getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	public static int getLevelWidth()
	{
		return bufferedImageLoader("/maps/map_" + CURRENT_LEVEL + ".png").getWidth();
	}
	
	public static int getLevelHeight()
	{
		return bufferedImageLoader("/maps/map_" + CURRENT_LEVEL + ".png").getHeight();
	}
}
