package Entity;

import static core.Constants.*;

public enum TileType {

	Floor_01("Floor_01", TILE_SIZE, TILE_SIZE),
	
	WallTop_01("Wall_01", TILE_SIZE, TILE_SIZE),
	WallBottom_01("Bottom_01", TILE_SIZE, TILE_SIZE),
	WallBottom_02("Bottom_inside", TILE_SIZE, TILE_SIZE),
	
	Default("Default", TILE_SIZE, TILE_SIZE),
	NULL("Blank", TILE_SIZE, TILE_SIZE); // to fill empty tiles on the grid
	
	public String textureName;
	public int width, height;
	
	TileType(String textureName, int width, int height)
	{
		this.textureName = textureName;
		this.width = width;
		this.height = height;
	}
}
