package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Leveler.*;

import framework.gamestate.Game;
import framework.path.Graph;
import framework.path.PathfindingThread;

public class StateManager {
	
	// possible gamestates
	public static enum GameState{
		MAINMENU, GAME, DEATHSCREEN, LOADING
	}
	
	// Start parameter
	public static int CURRENT_LEVEL = 0;
	public static GameState gameState = GameState.GAME; // initial state -> gameState = GameState.MAINMENU;
	
	private Handler handler;
	private Game game;
	
	private Graph graph;
	private PathfindingThread pathThread;
	
	private long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	private int framesInCurrentSecond = 0;
	
	public StateManager(){
		this.graph = new Graph();
		this.handler = new Handler();
		this.game = new Game(handler);
	}
	
	public void update()
	{
		switch (gameState) {
		case MAINMENU:
			
			break;
		case GAME:
			if(CURRENT_LEVEL == 0)
				loadLevel();
			
			game.update();
			game.render();
			break;

		default:
			System.out.println("No valid gamestate found");
			break;
		}
		
		// Calculate FPS
		long currentTime = System.currentTimeMillis();
		if(currentTime > nextSecond){
			//System.out.println("FPS: " + framesInLastSecond);
			nextSecond += 1000;
			framesInLastSecond = framesInCurrentSecond;
			framesInCurrentSecond = 0;
		}
		framesInCurrentSecond++;
	}
	
	public void loadLevel(){
		CURRENT_LEVEL++;
		handler.wipe();
		shadowObstacleList.clear();

		switch (CURRENT_LEVEL) {
		// Szenario maps
		case 1:
			handler.setMap(loadMap(handler, "level/map_0" + CURRENT_LEVEL, graph));
			break;

		default:
			break;
		}
		
		game.getCamera().setEntity(handler.getCurrentEntity());
		
		pathThread = new PathfindingThread(handler.enemyList, graph, handler);
		pathThread.start();
	}
}
