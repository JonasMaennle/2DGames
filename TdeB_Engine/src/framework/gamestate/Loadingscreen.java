package framework.gamestate;

import static framework.helper.Collection.lights;

import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.path.PathfindingThread;

public class Loadingscreen {
	
	private StateManager manager;
	
	public Loadingscreen(StateManager manager){
		this.manager = manager;
	}
	
	public void update(){
		reset();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.loadLevel();
		
		StateManager.gameState = GameState.GAME;
	}
	
	public void render(){
		
	}
	
	private void reset(){
		//handler.setFogFilter(0);
		lights.clear();
		PathfindingThread.running = false;
	}
}
