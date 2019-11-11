package framework.entity;

public enum LaserType {
	SIMPLE_BLUE("player"), 
	SIMPLE_GREEN("enemy");
	
	String owner;
	
	LaserType(String owner){
		this.owner = owner;
	}
	
	public String getOwner() { return owner; }
	
}
