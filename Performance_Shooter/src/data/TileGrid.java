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
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				//map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.NULL);
			}
		}
	}
	
//	public TileGrid(int[][] newMap)
//	{
//		this.tilesWide = newMap[0].length;
//		this.tilesHigh = newMap.length;
//		map = new Tile[tilesWide][tilesHigh];
//		for(int i = 0; i < map.length; i++)
//		{
//			for(int j = 0; j < map[i].length; j++)
//			{	
//				switch (newMap[j][i]) {
//				case 0:
//					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Grass_Flat);
//					break;
//				case 1:
//					
//					break;
//				case 2:
//					
//					break;
//				case 3:
//					
//					break;
//
//				default:
//					System.out.println("Wrong grid number!");
//					break;
//				}
//			}
//		}
//	}
	
	public void setTile(int xCoord, int yCoord, TileType type)
	{
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE, type);
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
