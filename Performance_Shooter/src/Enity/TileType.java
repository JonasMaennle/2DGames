package Enity;

import static helpers.Setup.*;

public enum TileType {

	Grass_Flat("Grass_Flat", 100, TILE_SIZE, TILE_SIZE), Grass_Left("Grass_Left", 100, TILE_SIZE, TILE_SIZE),Grass_Right("Grass_Right", 100, TILE_SIZE, TILE_SIZE),Grass_Round("Grass_Round", 100, TILE_SIZE, TILE_SIZE),
	Grass_Round_Half("Grass_Round_Half", 100, TILE_SIZE, TILE_SIZE),Grass_Left_Half("Grass_Left_Half", 100, TILE_SIZE, TILE_SIZE),Grass_Right_Half("Grass_Right_Half", 100, TILE_SIZE, TILE_SIZE),Grass_Flat_Half("Grass_Flat_Half", 100, TILE_SIZE, TILE_SIZE),
	
	TreeBig_01("tree_01", 0, 448, 640),TreeBig_02("tree_02", 0, 448, 640),TreeBig_03("tree_03", 0, 256, 640),TreeBig_04("tree_04", 0, 256, 640),TreeBig_05("tree_05", 0, 320, 384),
	TreeBig_06("tree_06", 0, 196, 320),TreeBig_07("tree_07", 0, 348, 448),TreeBig_08("tree_08", 0, 128, 196),
	
	TreeStump_Left("tree_stump_left", 100, TILE_SIZE, TILE_SIZE),TreeStump_Right("tree_stump_right", 100, TILE_SIZE, TILE_SIZE),
	
	Dirt_Basic("Dirt_Basic", 100, TILE_SIZE, TILE_SIZE),
	Ramp_Start("Ramp_start", 100, TILE_SIZE, TILE_SIZE),Ramp_End("Ramp_end", 100, TILE_SIZE, TILE_SIZE),
	Rock_Basic("Rock_Basic_0", 200, TILE_SIZE, TILE_SIZE), 
	NULL("Blank", 0, TILE_SIZE, TILE_SIZE), 
	Default("Filler", 100, TILE_SIZE, TILE_SIZE);
	
	public String textureName;
	public int hp;
	public int width, height;
	
	TileType(String textureName, int hp, int width, int height)
	{
		this.textureName = textureName;
		this.hp = hp;
		this.width = width;
		this.height = height;
	}
}
