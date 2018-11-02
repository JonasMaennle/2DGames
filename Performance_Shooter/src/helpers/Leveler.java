package helpers;

import java.awt.image.BufferedImage;
import static helpers.Artist.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import data.Tile;
import data.TileGrid;
import data.TileType;

public class Leveler {
	
	public static ArrayList<Tile> obstacleList = new ArrayList<>();
	public static int playerX, playerY;
	
	public static TileGrid loadMap()
	{
		TileGrid grid = new TileGrid();
		BufferedImage image = bufferedImageLoader("/maps/map_default.png");
		
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
					obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Left
				if(red == 64 && green == 64 && blue == 64)
				{
					grid.setTile(x, y, TileType.Grass_Left);
					obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Right
				if(red == 48 && green == 48 && blue == 48)
				{
					grid.setTile(x, y, TileType.Grass_Right);
					obstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Round
				if(red == 128 && green == 128 && blue == 128)
				{
					grid.setTile(x, y, TileType.Grass_Round);
					obstacleList.add(grid.getTile(x, y));
				}
		// Rock Tile
				// Grey -> Rock_Basic
				if(red == 196 && green == 196 && blue == 196)
				{
					grid.setTile(x, y, TileType.Rock_Basic);
					obstacleList.add(grid.getTile(x, y));
				}
				
		// Player Tile
				// Red -> Player
				if(red == 255 && green == 0 && blue == 0)
				{
					playerX = x * TILE_SIZE;
					playerY = y * TILE_SIZE;
				}
			}
		}
		//System.out.println(helpers.Artist.tileCounter);
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
		return bufferedImageLoader("/maps/map_default.png").getWidth();
	}
	
	public static int getLevelHeight()
	{
		return bufferedImageLoader("/maps/map_default.png").getHeight();
	}
}
