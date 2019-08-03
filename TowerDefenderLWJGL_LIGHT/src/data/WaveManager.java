package data;

import java.util.Random;

public class WaveManager {
	
	private float timeSinceLastWave, timeBetweenEnemies;
	private int waveNumber, enemiesPerWave;
	private int[] enemyTypes;
	private Wave currentWave;
	private TileGrid grid;
	
	public WaveManager(TileGrid grid, int enemiesPerWave, float timeBetweenEnemies)
	{
		this.grid = grid;
		this.timeSinceLastWave = 0;
		this.timeBetweenEnemies = timeBetweenEnemies;
		this.waveNumber = 0;
		this.enemiesPerWave = enemiesPerWave;
		this.currentWave = null;
		
		createEnemies(grid);
		
		newWave();
	}
	
	private void createEnemies(TileGrid grid)
	{
		Random rand = new Random();
		enemyTypes = new int[5];
		for(int i = 0; i < enemyTypes.length; i++)
		{
			enemyTypes[i] = rand.nextInt(2);
		}
	}
	
	public void update()
	{
		if(!currentWave.isCompleted())
			currentWave.update();
		else
			newWave();
	}

	private void newWave()
	{
		currentWave = new Wave(enemyTypes, grid, timeBetweenEnemies, enemiesPerWave);
		waveNumber++;
		//System.out.println("Beginning Wave: " + waveNumber);
	}
	
	public Wave getCurrentWave()
	{
		return currentWave;
	}
	
	public float getTimeSinceLastWave()
	{
		return timeSinceLastWave;
	}

	public int getWaveNumber() {
		return waveNumber;
	}
}
