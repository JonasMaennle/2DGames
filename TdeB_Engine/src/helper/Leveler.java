package helper;

import static core.Constants.*;
import static helper.Graphics.*;
import static helper.Collection.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import Entity.TileType;
import core.Handler;
import core.TileGrid;
import object.Enemy_Basic;
import object.Player;

public class Leveler {
	public static TileGrid loadMap(Handler handler, String path)
	{
		Image image = quickLoaderImage(path);
		//System.out.println(image.getResourceReference());
		int w = image.getWidth();
		int h = image.getHeight();
		
		TILES_WIDTH = w;
		TILES_HEIGHT = h;
		
		TileGrid grid = new TileGrid(handler);

		//System.out.println("w: " + w + " h: " + h);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = image.getColor(x, y);
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				
		// Test Tiles
				// Black -> Default Tile
				if(red == 0 && green == 0 && blue == 0)
				{
					grid.setTile(x, y, TileType.Default);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Player Tile
				// Red -> Player
				if(red == 255 && green == 0 && blue == 0)
				{
					handler.setPlayer(new Player(x * TILE_SIZE, y * TILE_SIZE, handler));
				}
		// Enemy	
				// Blue -> Enemy
				if(red == 0 && green == 0 && blue == 255)
				{
					Enemy_Basic tmp = new Enemy_Basic(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					handler.enemyList.add(tmp);
					//handler.obstacleList.add(tmp);
					shadowObstacleList.add(tmp);
				}
			}
		}
		
		handler.setCurrentEntity(handler.getPlayer());
		return grid;
	}
}
