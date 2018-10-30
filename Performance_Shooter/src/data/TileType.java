package data;

public enum TileType {

	Grass("Grass_lp", true), Dirt("Dirt", false), Water("Water", false), Rock("Rock", false), NULL("Blank",false), Default("Filler", false);
	
	String textureName;
	boolean buildable;
	
	TileType(String textureName, boolean buildable)
	{
		this.textureName = textureName;
		this.buildable = buildable;
	}
}
