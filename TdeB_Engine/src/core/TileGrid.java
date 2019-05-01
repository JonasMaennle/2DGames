package core;

import static helper.Collection.*;

import org.newdawn.slick.Image;

import object.Tile;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	private Handler handler;
	
	public TileGrid(Handler handler){
		this.tilesWide = TILES_WIDTH;//WIDTH / TILE_SIZE;
		this.tilesHigh = TILES_HEIGHT;//HEIGHT / TILE_SIZE; 
		map = new Tile[tilesWide][tilesHigh];
		this.handler = handler;
	}
	
	public void setTile(int xCoord, int yCoord, Image image){
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, image, handler);	
	}
	
	public Tile getTile(int xPlace, int yPlace){
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
		{
			if(map[xPlace][yPlace] == null)
				return new Tile(0, 0, null, handler);
			else
				return map[xPlace][yPlace];
		}
		else
			return null; //new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void draw(){
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{	
				if(!(map[i][j] == null))
				{
					map[i][j].draw();;			
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
