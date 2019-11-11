package framework.entity;

import org.newdawn.slick.Color;

public enum EnemyType {
	ENEMY_ORANGE(10, "orange", new Color(255, 102, 0), 55, 35),
	ENEMY_GREEN(20, "green", new Color(0, 234, 7), 60, 40);
	
	
	private int points;
	private String color;
	private Color enemyColor;
	private int lightSize;
	private int deathParticleNumber;
	
	EnemyType(int points, String color, Color enemyColor, int lightSize, int deathParticleNumber) {
		this.points = points;
		this.color = color;
		this.enemyColor = enemyColor;
		this.lightSize = lightSize;
		this.deathParticleNumber = deathParticleNumber;
	}
	
	public int getPoints() { return points; }
	public String getColor() { return color; }
	public Color getEnemyColor() { return enemyColor; }
	public int getLightSize() { return lightSize; }
	public int getDeathParticleNumber() { return deathParticleNumber; }
}
