package Enity;

public enum TileType {

	Grass_Flat("Grass_Flat", 100), Grass_Left("Grass_Left", 100),Grass_Right("Grass_Right", 100),Grass_Round("Grass_Round", 100),
	Grass_Round_Half("Grass_Round_Half", 100),Grass_Left_Half("Grass_Left_Half", 100),Grass_Right_Half("Grass_Right_Half", 100),Grass_Flat_Half("Grass_Flat_Half", 100),
	
	Dirt_Basic("Dirt_Basic", 100),
	Rock_Basic("Rock_Basic_0", 200), 
	NULL("Blank", 0), 
	Default("Filler", 100);
	
	public String textureName;
	public int hp;
	
	TileType(String textureName, int hp)
	{
		this.textureName = textureName;
		this.hp = hp;
	}
}
