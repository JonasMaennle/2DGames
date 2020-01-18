package framework.entity;

public enum LaserType {
	SIMPLE_BLUE("player", 100), 
	SIMPLE_GREEN("enemy", 4);
	
	String owner;
	int damage;
	
	LaserType(String owner, int damage){
		this.owner = owner;
		this.damage = damage;
	}
	
	public String getOwner() { return owner; }
	public int getDamage() { return damage; }
	
}
