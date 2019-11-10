package framework.entity;

import org.newdawn.slick.Color;

public enum EnemyType {
	ENEMY_ORANGE(10, "orange", new Color(255, 102, 0));
	
	
	private int points;
	private String color;
	private Color enemyColor;
	
	EnemyType(int points, String color, Color enemyColor) {
		this.points = points;
		this.color = color;
		this.enemyColor = enemyColor;
	}
	
	public int getPoints() { return points; }
	public String getColor() { return color; }
	public Color getEnemyColor() { return enemyColor; }
}
