package data;

import static helpers.Leveler.*;
import static helpers.Setup.*;

import Enity.TileType;
import object.Tile;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	private Handler handler;
	
	public TileGrid(Handler handler)
	{
		this.tilesWide = TILES_WIDTH;//WIDTH / TILE_SIZE;
		this.tilesHigh = TILES_HEIGHT;//HEIGHT / TILE_SIZE; 
		map = new Tile[tilesWide][tilesHigh];
		this.handler = handler;
	}
	
	public void setTile(int xCoord, int yCoord, TileType type)
	{
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, type, handler);	
	}
	
	public Tile getTile(int xPlace, int yPlace)
	{
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
		{
			if(map[xPlace][yPlace] == null)
				return new Tile(0, 0, TileType.NULL, handler);
			else
				return map[xPlace][yPlace];
		}
		else
			return null; //new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void draw()
	{
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{	
				if(!(map[i][j] == null))
				{
					if(map[i][j].getX() > getLeftBorder() - 256 && map[i][j].getX() < getRightBorder() + 256 && map[i][j].getY() > getTopBorder() - 640 && map[i][j].getY() < getBottomBorder() + 640)
					{
						map[i][j].draw();;
					}
				}
			}
		}
	}

	public int getTilesWide() {
		return tilesWide;
	}

	public void setTilesWide(int tilesWide) {
		this.tilesWide = tilesWide;
	}

	public int getTilesHigh() {
		return tilesHigh;
	}

	public void setTilesHigh(int tilesHigh) {
		this.tilesHigh = tilesHigh;
	}
}
