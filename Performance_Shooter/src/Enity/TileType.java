package Enity;

import static helpers.Setup.*;

public enum TileType {

	Grass_Flat("Grass_Flat", 100, TILE_SIZE, TILE_SIZE, 0), 
	Grass_Left("Grass_Left", 100, TILE_SIZE, TILE_SIZE, 4210752),
	Grass_Right("Grass_Right", 100, TILE_SIZE, TILE_SIZE, 3158064),
	Grass_Round("Grass_Round", 100, TILE_SIZE, TILE_SIZE, 8421504),
	Grass_Round_Half("Grass_Round_Half", 100, TILE_SIZE, TILE_SIZE/2, 6566450),
	Grass_Left_Half("Grass_Left_Half", 100, TILE_SIZE, TILE_SIZE/2, 9843250),
	Grass_Right_Half("Grass_Right_Half", 100, TILE_SIZE, TILE_SIZE/2, 9843270),
	Grass_Flat_Half("Grass_Flat_Half", 100, TILE_SIZE, TILE_SIZE/2, 9848390),
	
	RedWood_01("redwood_01", 0, 448, 640, 11810560),RedWood_02("redwood_02", 0, 448, 640, 11155200),RedWood_03("redwood_03", 0, 448, 640,10499840),
	RedWood_04("redwood_04", 0, 384, 640, 9844480),RedWood_05("redwood_05", 0, 384, 512, 9189120),RedWood_06("redwood_06", 0, 384, 512, 8533760),
	RedWood_07("redwood_01", 0, 348, 512, 7878400),RedWood_08("redwood_08", 0, 256, 384, 7223040),RedWood_09("redwood_01", 0, 512, 768, 6567680),
	
	TreeStump_Left("tree_stump_left", 100, TILE_SIZE, TILE_SIZE, 5243007),
	TreeStump_Right("tree_stump_right", 100, TILE_SIZE, TILE_SIZE, 5898367),
	TreeStump_Center("tree_stump_center", 100, TILE_SIZE, TILE_SIZE, 5570687),
	
	Dirt_Basic("Dirt_Basic", 100, TILE_SIZE, TILE_SIZE, 8335872),
	
	Ramp_Start("Ramp_start", 100, TILE_SIZE, TILE_SIZE, 5255680),
	Ramp_End("Ramp_end", 100, TILE_SIZE, TILE_SIZE, 4600320),
	
	Ice_Basic("Ice", 200, TILE_SIZE, TILE_SIZE, 38143),
	Lava("Lava_Sheet", 200, TILE_SIZE, TILE_SIZE, 13125120),
	Lava_Light("Lava_Sheet", 200, TILE_SIZE, TILE_SIZE, 16729600),
	
	Rock_Basic("Rock_Basic_0", 200, TILE_SIZE, TILE_SIZE, 12895428),
	
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
