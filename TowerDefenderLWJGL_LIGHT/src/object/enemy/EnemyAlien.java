package object.enemy;

import static helpers.Artist.lights;

import data.TileGrid;
import shader.Light;

public class EnemyAlien extends Enemy{

	public EnemyAlien(int tileX, int tileY, TileGrid grid, Light light) 
	{
		super(tileX, tileY, grid);
		this.setTexture("enemyAlien");
		super.light = light;
		//super.light.setRadius(1000);
		lights.add(light);
	}
}
