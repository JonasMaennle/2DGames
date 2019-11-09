package framework.core;

import java.util.Random;

import object.enemy.Enemy_Orange;
import static framework.helper.Collection.*;

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
			if(handler.getEnemyList().size() < 10) {
				spawnEnemy();
			}
		}
	}
	
	private void spawnEnemy() {
		int direction = rand.nextInt(4);
		switch (direction) {
		case 0: // top
			handler.getEnemyList().add(new Enemy_Orange(rand.nextInt(WIDTH), (int)getTopBorder() - rand.nextInt(500), TILE_SIZE, TILE_SIZE, handler));
			break;
		case 1: // bottom
			handler.getEnemyList().add(new Enemy_Orange(rand.nextInt(WIDTH), (int)getBottomBorder() + rand.nextInt(500), TILE_SIZE, TILE_SIZE, handler));
			break;
		case 2: // left
			handler.getEnemyList().add(new Enemy_Orange((int)getLeftBorder() - rand.nextInt(500), rand.nextInt(HEIGHT), TILE_SIZE, TILE_SIZE, handler));
			break;
		case 3: // right
			handler.getEnemyList().add(new Enemy_Orange((int)getRightBorder() + rand.nextInt(500), rand.nextInt(HEIGHT), TILE_SIZE, TILE_SIZE, handler));
			break;

		default:
			break;
		}
	}
}
