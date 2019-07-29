package framework.arena;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Collection.WIDTH;
import static framework.helper.Collection.shadowObstacleList;

import java.util.LinkedList;
import java.util.Random;

import framework.core.Handler;
import framework.core.StateManager;
import framework.helper.Collection;
import framework.path.Node;
import object.Spawner;
import object.enemy.Enemy_Digger;
import object.enemy.Enemy_Fly;
import object.enemy.Enemy_Ghost;
import object.enemy.Enemy_Spider;

public class WaveManager {
	
	private int waveCounter;
	private boolean showWaveCounter;
	private Handler handler;
	
	private int waveOffsetInMSEC;
	private long timer;
	private boolean waveHasSpawned;
	
	private long t1, t2;
	
	private float enemiesInWave;
	private float enemyWaveMultiplier;
	private int currentEnemiesInWave;
	private Random rand;
	private StateManager manager;
	
	private long lastEnemyDead;
	private boolean lastEnemyDied;
	private boolean showLvl10Message;
	
	public WaveManager(Handler handler, StateManager manager) {
		this.waveCounter = 1;
		this.showWaveCounter = false;
		this.handler = handler;
		this.manager = manager;
		
		this.waveOffsetInMSEC = 3000;
		this.waveHasSpawned = false;
		this.rand = new Random();
		this.lastEnemyDied = false;
		
		this.enemiesInWave = 1;
		this.enemyWaveMultiplier = 1.2f; // can be changed -> value > 1
		
		this.currentEnemiesInWave = 0;
		this.showLvl10Message = false;
	}
	
	public void update() {
		
		// show current wave
		if(showWaveCounter){
			
			showWaveCounter = false;
			this.timer = System.currentTimeMillis();
		}
		
		// start wave after cooldown
		if(System.currentTimeMillis() - timer > waveOffsetInMSEC && !waveHasSpawned && (System.currentTimeMillis() - lastEnemyDead > 2000)) {		
			waveHasSpawned = true;
			enemiesInWave *= enemyWaveMultiplier;
			currentEnemiesInWave = 0;
			if(waveCounter == 10) {
				showLvl10Message = true;
			}
			handler.getInfo_manager().createNewMessage(WIDTH/2 - MOVEMENT_X - 64, HEIGHT/2 - 196 - MOVEMENT_Y, "Wave " + waveCounter, new org.newdawn.slick.Color(255,255,255), 32, 3000);
			waveCounter++;
			lastEnemyDied = false;
			showWaveCounter = true;
			Collection.ARENA_CURRENT_WAVE = waveCounter;
			
		}
		
		if(showLvl10Message) {
			showLvl10Message = false;
			handler.getInfo_manager().createNewMessage(WIDTH/2 - MOVEMENT_X - 256, HEIGHT/2 - 256 - MOVEMENT_Y, "More  Enemies  are  coming !", new org.newdawn.slick.Color(255,0,0), 32, 3000);
		}
		
		if(waveHasSpawned) {
			spawnWave();
		}
		
		if(handler.enemyList.size() == 0) {
			// remove all uncollected items after wave
//			if(waveCounter > 1) {
//				for(Collectable_Basic c : handler.collectableList) {
//					if(!(c instanceof Collectable_Helmet)) {
//						handler.collectableList.remove(c);
//					}
//				}
//			}
			
			
			if(!lastEnemyDied) {
				lastEnemyDead = System.currentTimeMillis();
				lastEnemyDied = true;
			}
				
			waveHasSpawned = false;
		}
	}
	
	private void spawnWave() {
		// test
		t1 = System.currentTimeMillis();
		int spawnCounter = 0;
		
		if(t1 - t2 > 2000 && currentEnemiesInWave <= enemiesInWave) {
			t2 = t1;
			// spawn on each spawner
			for(Spawner point : handler.spawnPoints) {
				currentEnemiesInWave++;
				spawnCounter++;
				// use only 2 spawnpoints when wave < 10
				if(Collection.ARENA_CURRENT_WAVE <= 10 && spawnCounter == handler.spawnPoints.size()) {
					spawnCounter = 0;
					break;
				}

				
				Double random = rand.nextDouble();

				// WAVE < 10
				if(waveCounter < 10) {
					if(random < 0.85) {
						Enemy_Spider tmp = new Enemy_Spider(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.85) {
						Enemy_Fly tmp = new Enemy_Fly(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}
					
				// WAVE >= 10 && < 20
				}else if(waveCounter >= 10 && waveCounter < 20) {
					if(random < 0.5) {
						Enemy_Spider tmp = new Enemy_Spider(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.5 && random < 0.7) {
						Enemy_Fly tmp = new Enemy_Fly(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.7 && random < 0.95) {
						Enemy_Ghost tmp = new Enemy_Ghost(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.95) {
						Node n = getRandomNode();
						Enemy_Digger tmp = new Enemy_Digger(n.getX() * TILE_SIZE, n.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
					}
					
				// WAVE >= 20 && < 30
				}else if(waveCounter >= 20 && waveCounter < 30) {
					if(random < 0.3) {
						Enemy_Spider tmp = new Enemy_Spider(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.3 && random < 0.7) {
						Enemy_Fly tmp = new Enemy_Fly(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.7 && random < 0.9) {
						Enemy_Ghost tmp = new Enemy_Ghost(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.9) {
						Node n = getRandomNode();
						Enemy_Digger tmp = new Enemy_Digger(n.getX() * TILE_SIZE, n.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
					}
					
				// WAVE >= 30 && < 40
				}else if(waveCounter >= 30 && waveCounter < 40) {
					// spawn diggers
					if(random < 0.3) {
						Enemy_Spider tmp = new Enemy_Spider(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.3 && random < 0.6) {
						Enemy_Fly tmp = new Enemy_Fly(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.6 && random < 0.75) {
						Enemy_Ghost tmp = new Enemy_Ghost(point.getX(), point.getY(), TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
						shadowObstacleList.add(tmp);
					}else if(random >= 0.75) {
						Node n = getRandomNode();
						Enemy_Digger tmp = new Enemy_Digger(n.getX() * TILE_SIZE, n.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE, handler);
						handler.enemyList.add(tmp);
					}
				}	
			}
		}
	}
	
	public void reset() {
		this.waveCounter = 1;
		this.waveOffsetInMSEC = 3000;
		this.waveHasSpawned = false;
		this.showWaveCounter = true;
		this.enemiesInWave = 1;
		currentEnemiesInWave = 0;
		this.lastEnemyDied = false;
	}

	public int getWaveCounter() {
		return waveCounter;
	}

	public void setWaveCounter(int waveCounter) {
		this.waveCounter = waveCounter;
	}
	
	private Node getRandomNode() {
		Node n = null;
		LinkedList<Node> nodes = manager.getGraph().getNodes();
		n = nodes.get(rand.nextInt(nodes.size()));
		return n;
	}
}
