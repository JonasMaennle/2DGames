package framework.core.service;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import object.other.Tile;

public class WaveManager {
	
	private Tile[][] map;
	private long timer1, timer2, timer3, timer4, timer5, timer6;
	private int startX, startX2;
	private CopyOnWriteArrayList<Tile> wavelist;
	
	public WaveManager(Tile[][] map) {
		this.map = map;
		this.wavelist = new CopyOnWriteArrayList<Tile>();
		this.startX = 49;
		this.startX2 = 49;
		this.timer6 = System.currentTimeMillis() + 10000; // Time offset for second wave
	}
	
	public void update() {
		generateRandomOffset();
		addTilesToWave();
		moveWave();
	}
	
	private void generateRandomOffset() {
		Random rand = new Random();
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){	
				if(!(map[i][j] == null) && map[i][j].getID() == 34 && rand.nextInt(10000) > 9950){
					if(map[i][j].getWaveOffset() > 0 || map[i][j].getWaveOffset() < 12) {
						map[i][j].setWaveOffset((int) (map[i][j].getWaveOffset() + (rand.nextFloat() * 4) - 2));	
					}					
				}
			}
		}
	}

	private void addTilesToWave() {	
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 350) {
			timer2 = timer1;
			
			for(int y = 0; y < 50; y++) {
				if(!(map[startX][y] == null) && map[startX][y].getID() == 34 && !map[startX][y].isTriggerWave()){
					map[startX][y].setTriggerWave(true);
					wavelist.add(map[startX][y]);
				}
			}
			startX--;
			if(startX < 0)
				startX = 49;
		}
		
		timer5 = System.currentTimeMillis();
		if(timer5 - timer6 > 350) {
			timer6 = timer5;
			
			for(int y = 0; y < 50; y++) {
				if(!(map[startX2][y] == null) && map[startX2][y].getID() == 34 && !map[startX2][y].isTriggerWave()){
					map[startX2][y].setTriggerWave(true);
					wavelist.add(map[startX2][y]);
				}
			}
			startX2--;
			if(startX2 < 0)
				startX2 = 49;
		}
	}
	
	private void moveWave() {
		timer3 = System.currentTimeMillis();
		if(timer3 - timer4 > 80) {
			timer4 = timer3;
			
			for(Tile t : wavelist) {
				if(t.getWaveOffset() < 10 && !t.isTideMax()) {
					t.setWaveOffset(t.getWaveOffset() + 1);
				}else {
					t.setTideMax(true);
				}
				
				if(t.isTideMax() && t.getWaveOffset() > 0) {
					t.setWaveOffset(t.getWaveOffset() - 1);
				}
				
				if(t.isTideMax() && t.getWaveOffset() == 0) {
					t.setTriggerWave(false);
					t.setTideMax(false);
					wavelist.remove(t);
				}
			}
		}
	}
}
