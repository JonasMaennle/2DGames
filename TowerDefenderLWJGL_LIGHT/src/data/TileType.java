package data;

public enum TileType {

	Grass("Grass", true), Dirt("Dirt", false), Water("Water", false), Rock("Rock", false), NULL("Water",false);
	
	String textureName;
	boolean buildable;
	
	TileType(String textureName, boolean buildable)
	{
		this.textureName = textureName;
		this.buildable = buildable;
	}
}
