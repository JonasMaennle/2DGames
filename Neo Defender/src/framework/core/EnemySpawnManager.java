package framework.core;

import java.util.Random;


public class EnemySpawnManager {
	
	private Handler handler;
	private long t1, t2;
	private Random rand;
	
	public EnemySpawnManager(Handler handler) {
		this.handler = handler;
		this.rand = new Random();
	}
	
	public void update() {
		t1 = System.currentTimeMillis();
		if(t1 - t2 > 500) {

		}
	}
	
	private void spawnEnemy() {

	}
}
