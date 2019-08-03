package object.enemy;

import static helpers.Artist.*;

import data.TileGrid;
import shader.Light;

public class EnemyUFO extends Enemy{

	public EnemyUFO(int tileX, int tileY, TileGrid grid, Light light) 
	{
		super(tileX, tileY, grid);
		this.setTexture("Enemy");
		this.setSpeed(75);
		this.setHealth(600);
		super.light = light;
		lights.add(light);
	}
}
