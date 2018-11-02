package Enity;

public enum TileType {

	Grass_Flat("Grass_Flat"), Grass_Left("Grass_Left"),Grass_Right("Grass_Right"),Grass_Round("Grass_Round"), 
	Dirt_Basic("Dirt_Basic"),
	Rock_Basic("Rock_Basic"), 
	NULL("Blank"), 
	Default("Filler");
	
	public String textureName;
	
	TileType(String textureName)
	{
		this.textureName = textureName;
	}
}
