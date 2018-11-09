package data;

import static helpers.Artist.*;
import static helpers.Leveler.*;

import Enity.TileType;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	
	public TileGrid()
	{
		this.tilesWide = getLevelWidth();//WIDTH / TILE_SIZE;
		this.tilesHigh = getLevelHeight();//HEIGHT / TILE_SIZE; 
		map = new Tile[tilesWide][tilesHigh];
	}
	
	public void setTile(int xCoord, int yCoord, TileType type)
	{
		if(type == TileType.Grass_Round_Half || type == TileType.Grass_Left_Half || type == TileType.Grass_Right_Half || type == TileType.Grass_Flat_Half)
		{
			map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE/2, type);
		}else{
			map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE, type);
		}
	}
	
	public Tile getTile(int xPlace, int yPlace)
	{
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
			return map[xPlace][yPlace];
		else
			return new Tile(0, 0, 0, 0, TileType.NULL);
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
