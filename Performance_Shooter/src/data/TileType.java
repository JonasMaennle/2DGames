package data;

public enum TileType {

	Grass_Flat("Grass_Flat"), Grass_Left("Grass_Left"),Grass_Right("Grass_Right"),Grass_Round("Grass_Round"), 
	Rock_Basic("Rock_Basic"), 
	NULL("Blank"), 
	Default("Filler");
	
	String textureName;
	
	TileType(String textureName)
	{
		this.textureName = textureName;
	}
}
