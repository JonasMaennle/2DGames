package data;

import static helpers.Leveler.*;
import static helpers.Setup.*;

import Enity.TileType;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	
	public TileGrid()
	{
		this.tilesWide = TILES_WIDTH;//WIDTH / TILE_SIZE;
		this.tilesHigh = TILES_HEIGHT;//HEIGHT / TILE_SIZE; 
		map = new Tile[tilesWide][tilesHigh];
	}
	
	public void setTile(int xCoord, int yCoord, TileType type)
	{
		if(type == TileType.Grass_Round_Half || type == TileType.Grass_Left_Half || type == TileType.Grass_Right_Half || type == TileType.Grass_Flat_Half)
		{
			map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, type);
		}
		else{
			map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, type);
		}
	}
	
	public Tile getTile(int xPlace, int yPlace)
	{
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
		{
			if(map[xPlace][yPlace] == null)
				return new Tile(0, 0, TileType.NULL);
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
				if(!(map[i][j] == null))map[i][j].draw();
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
