package framework.core;

import java.util.Random;

import object.collectable.Collectable_Spike;
import object.enemy.Enemy_Basic;

public class CollectableSpawnManager {
	
	private Handler handler;
	private Random rand;
	
	public CollectableSpawnManager(Handler handler) {
		this.handler = handler;
		this.rand = new Random();
	}
	
	public void enemyHasDied(Enemy_Basic enemy) {
		switch (enemy.getClass().getSimpleName()) {
		case "Enemy_Orange":
			if(rand.nextInt(100) < 20)
				handler.getCollectableList().add(new Collectable_Spike(enemy.getX(), enemy.getY(), handler.getPlayer().getWidth(), handler.getPlayer().getHeight()));
			break;
		case "Enemy_Green":
			if(rand.nextInt(100) < 15)
				handler.getCollectableList().add(new Collectable_Spike(enemy.getX(), enemy.getY(), handler.getPlayer().getWidth(), handler.getPlayer().getHeight()));
			break;

		default:
			break;
		}
	}
}
