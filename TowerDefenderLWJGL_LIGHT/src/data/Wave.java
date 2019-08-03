package data;

import static helpers.Clock.delta;
import static helpers.Artist.*;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;

import object.enemy.Enemy;
import object.enemy.EnemyAlien;
import object.enemy.EnemyUFO;
import shader.Light;

public class Wave {
	
	private float timeSinceLastSpawn, spawnTime;
	private int[] enemyTypes;
	private CopyOnWriteArrayList<Enemy> enemyList;
	private int enemiesPerWave, enemiesSpawned;
	private boolean waveCompleted;
	private TileGrid grid;
	
	public Wave(int[] enemyTypes, TileGrid grid, float spawnTime, int enemiesPerWave)
	{
		this.spawnTime = spawnTime;
		this.grid = grid;
		this.enemyTypes = enemyTypes;
		this.enemiesPerWave = enemiesPerWave;
		this.timeSinceLastSpawn = 0;
		this.enemyList = new CopyOnWriteArrayList<>();
		this.waveCompleted = false;
		this.enemiesSpawned = 0;
		
		spawn();
	}
	
	public void update()
	{
		// Assume all enemies are dead, until for loop finds one
		boolean allEnemiesDead = true;
		
		if(enemiesSpawned < enemiesPerWave)
		{
			timeSinceLastSpawn += delta();
			if(timeSinceLastSpawn > spawnTime)
			{
				spawn();
				timeSinceLastSpawn = 0;
			}
		}
		
		for(Enemy e : enemyList)
		{
			if(e.isAlive())
			{
				allEnemiesDead = false;
				e.update();
				e.draw();
			}else{
				enemyList.remove(e);
			}
		}
		
		if(allEnemiesDead)
			waveCompleted = true;
	}
	
	private void spawn()
	{
		int enemyChosen = 0;
		Random rand = new Random();
		enemyChosen = enemyTypes[rand.nextInt(enemyTypes.length)];
		
		switch (enemyChosen) {
		case 0:// Light(Vector2f location, float red, float green, float blue, float radius)
			enemyList.add( new EnemyAlien(START_TILE_X, START_TILE_Y, grid, new Light(new Vector2f(4 * 64 + 32, 0 * 64 + 32), 10, 1, 0, 1)));
			enemiesSpawned++;
			break;
		case 1:
			enemyList.add(new EnemyUFO(START_TILE_X, START_TILE_Y, grid, new Light(new Vector2f(4 * 64 + 32, 0 * 64 + 32), 0, 10, 0, 1)));
			enemiesSpawned++;
			break;

		default:
			System.out.println("Fehlerhafter Index");
			break;
		}
	}
	
	public boolean isCompleted()
	{
		return waveCompleted;
	}
	
	public CopyOnWriteArrayList<Enemy> getEnemyList()
	{
		return enemyList;
	}
}
