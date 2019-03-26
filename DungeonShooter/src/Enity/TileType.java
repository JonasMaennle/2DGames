package Enity;

import static helpers.Setup.*;

public enum TileType {

	Default("Default", 100, TILE_SIZE, TILE_SIZE, 0), 

	Lava("Lava_Sheet", 200, TILE_SIZE, TILE_SIZE, 13125120),
	Lava_Light("Lava_Sheet", 200, TILE_SIZE, TILE_SIZE, 16729600),

	NULL("Blank", 0, TILE_SIZE, TILE_SIZE, 16777215);
	
	//Default("Filler", 100, TILE_SIZE, TILE_SIZE);
	
	public String textureName;
	public int hp;
	public int width, height;
	public int rgb;
	
	TileType(String textureName, int hp, int width, int height, int rgb)
	{
		this.textureName = textureName;
		this.hp = hp;
		this.width = width;
		this.height = height;
		this.rgb = rgb;
	}
}
