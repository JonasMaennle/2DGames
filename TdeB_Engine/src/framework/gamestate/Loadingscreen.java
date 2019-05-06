package framework.gamestate;

import static framework.helper.Collection.lights;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.path.PathfindingThread;

public class Loadingscreen {
	
	private StateManager manager;
	private Handler handler;
	
	public Loadingscreen(StateManager manager, Handler handler){
		this.manager = manager;
		this.handler = handler;
	}
	
	public void update(){
		reset();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.loadLevel();
		handler.setFogFilter(0);
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
