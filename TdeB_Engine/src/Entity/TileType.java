package Entity;

import static core.Constants.*;

public enum TileType {

	
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
