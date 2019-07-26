package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import static framework.helper.Leveler.*;


import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;

import framework.gamestate.Arena;
import framework.gamestate.Deathscreen;
import framework.gamestate.Game;
import framework.gamestate.Loadingscreen;
import framework.gamestate.Mainmenu;
import framework.path.Graph;
import framework.path.PathfindingThread;

public class StateManager {
	
	// possible gamestates
	public static enum GameState{
		MAINMENU, GAME, DEATHSCREEN, LOADING, ARENA
	}
	
	// Start parameter
	public static int CURRENT_LEVEL = 0;
	public int MAX_LEVEL = 2;
	public static GameState gameState = GameState.MAINMENU; // initial state -> gameState = GameState.MAINMENU;
	public static GameState gameMode = GameState.GAME;
	
	private Handler handler;
	private Game game;
	private Arena arena;
	private Deathscreen deathscreen;
	private Loadingscreen loadingscreen;
	private Mainmenu menu;
	
	private Graph graph;
	private PathfindingThread pathThread;
	private Image cursor;
	
	private long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	private int framesInCurrentSecond = 0;
	
	public StateManager(){
		this.graph = new Graph();
		this.handler = new Handler();
		this.game = new Game(handler, this);
		this.deathscreen = new Deathscreen(handler, this);
		this.loadingscreen = new Loadingscreen(this, handler);
		this.menu = new Mainmenu(this);
		this.arena = new Arena(handler, this);
		
		this.cursor = quickLoaderImage("hud/cursor");
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
			
		case ARENA:
			if(CURRENT_LEVEL == 0)
				loadLevel();
			
			arena.update();
			arena.render();
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
		
		drawQuadImageStatic(cursor, Mouse.getX() - 12, HEIGHT - Mouse.getY() - 12, 32, 32);
	}
	
	public void loadLevel(){
		
		String levelPath = "level/map_0";
		
		// select other map in arena mode
		if(gameState == GameState.ARENA) {
			levelPath = "level/arena_map_0";
			CURRENT_LEVEL = 0;
			gameMode = GameState.ARENA;
		}else {
			gameMode = GameState.GAME;
		}

		CURRENT_LEVEL++;
		
		if(MAX_LEVEL + 1 == StateManager.CURRENT_LEVEL){
			StateManager.gameState = GameState.MAINMENU;
			return;
		}
		
		handler.wipe();
		shadowObstacleList.clear();
		graph = new Graph();
		System.out.println(levelPath+CURRENT_LEVEL);
		
		switch (CURRENT_LEVEL) {
		// Szenario maps
		case 1:
			handler.setMap(loadMap(handler, levelPath + CURRENT_LEVEL, graph));
			System.out.println("Level 1");
			break;
		case 2:
			handler.setMap(loadMap(handler, levelPath + CURRENT_LEVEL, graph));
			System.out.println("Level 2");
			break;


		default:
			break;
		}
		
		// set camera
		game.getCamera().setEntity(handler.getCurrentEntity());
		arena.getCamera().setEntity(handler.getCurrentEntity());
		
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

	public Loadingscreen getLoadingscreen() {
		return loadingscreen;
	}

	public void setLoadingscreen(Loadingscreen loadingscreen) {
		this.loadingscreen = loadingscreen;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Graph getGraph() {
		return graph;
	}
	
}
