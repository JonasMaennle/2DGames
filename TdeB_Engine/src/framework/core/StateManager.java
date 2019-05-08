package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Leveler.*;

import framework.gamestate.Deathscreen;
import framework.gamestate.Game;
import framework.gamestate.Loadingscreen;
import framework.gamestate.Mainmenu;
import framework.path.Graph;
import framework.path.PathfindingThread;

public class StateManager {
	
	// possible gamestates
	public static enum GameState{
		MAINMENU, GAME, DEATHSCREEN, LOADING
	}
	
	// Start parameter
	public static int CURRENT_LEVEL = 0;
	public static GameState gameState = GameState.LOADING; // initial state -> gameState = GameState.MAINMENU;
	
	private Handler handler;
	private Game game;
	private Deathscreen deathscreen;
	private Loadingscreen loadingscreen;
	private Mainmenu menu;
	
	private Graph graph;
	private PathfindingThread pathThread;
	
	private long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	private int framesInCurrentSecond = 0;
	
	public StateManager(){
		this.graph = new Graph();
		this.handler = new Handler();
		this.game = new Game(handler);
		this.deathscreen = new Deathscreen(handler, this);
		this.loadingscreen = new Loadingscreen(this, handler);
		this.menu = new Mainmenu(this);
	}
	
	public void update(){
		
		switch (gameState) {
		case MAINMENU:
			menu.update();
			menu.render();
			break;
			
		case GAME:
			if(CURRENT_LEVEL == 0)
				loadLevel();
			
			game.update();
			game.render();
			break;
			
		case DEATHSCREEN:
			deathscreen.update();
			deathscreen.render();
			break;
			
		case LOADING:
			loadingscreen.update();
			loadingscreen.render();
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
		graph = new Graph();
		
		switch (CURRENT_LEVEL) {
		// Szenario maps
		case 1:
			handler.setMap(loadMap(handler, "level/map_0" + CURRENT_LEVEL, graph));
			System.out.println("Level 1");
			break;
		case 2:
			handler.setMap(loadMap(handler, "level/map_0" + CURRENT_LEVEL, graph));
			System.out.println("Level 2");
			break;

		default:
			break;
		}
		
		game.getCamera().setEntity(handler.getCurrentEntity());
		
		PathfindingThread.running = true;
		pathThread = new PathfindingThread(handler.enemyList, graph, handler);
		pathThread.start();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Mainmenu getMenu() {
		return menu;
	}

	public void setMenu(Mainmenu menu) {
		this.menu = menu;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
