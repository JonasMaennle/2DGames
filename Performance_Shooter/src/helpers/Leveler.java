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

				// check if there is a white pixel / default
				if(red == 0 && green == 0 && blue == 0)
				{
					grid.setTile(x, y, TileType.Grass);
					obstacleList.add(grid.getTile(x, y));
				}
				// check if there is a red pixel / player
				if(red == 255 && green == 0 && blue == 0)
				{
					playerX = x * TILE_SIZE;
					playerY = y * TILE_SIZE;
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
		return bufferedImageLoader("/maps/map_default.png").getWidth();
	}
	
	public static int getLevelHeight()
	{
		return bufferedImageLoader("/maps/map_default.png").getHeight();
	}
}
