package framework.core;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import static framework.helper.Leveler.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;

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
	public int MAX_LEVEL = 2;
	public static GameState gameState = GameState.GAME; // initial state -> gameState = GameState.MAINMENU;
	
	private Handler handler;
	private Game game;
	private Deathscreen deathscreen;
	private Loadingscreen loadingscreen;
	private Mainmenu menu;
	
	private BackgroundHandler backgroundHandler;
	
	private Graph graph;
	private PathfindingThread pathThread;
	private Image cursor;
	
	private long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	private int framesInCurrentSecond = 0;
	
	public StateManager(){
		this.graph = new Graph();
		this.handler = new Handler();
		this.backgroundHandler = new BackgroundHandler(quickLoaderImage("background/background_black"), WIDTH * 2, HEIGHT * 2);
		
		this.game = new Game(this, handler, backgroundHandler);
		this.deathscreen = new Deathscreen(this, handler, backgroundHandler);
		this.loadingscreen = new Loadingscreen(this, handler, backgroundHandler);
		this.menu = new Mainmenu(this, handler, backgroundHandler);
		
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
	
	private void loadLevel(){
		String levelPath = "level/map_0";

		CURRENT_LEVEL++;
		
		// if max level is reached
		if(MAX_LEVEL + 1 == StateManager.CURRENT_LEVEL){
			StateManager.gameState = GameState.MAINMENU;
			return;
		}
		
		handler.wipe();
		shadowObstacleList.clear();
		graph = new Graph();
		
		//System.out.println(levelPath+CURRENT_LEVEL);
		
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
		game.getCamera().setEntity(handler.getPlayer());
		
		PathfindingThread.running = true;
		pathThread = new PathfindingThread(handler.getEnemyList(), graph, handler);
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

	public Graph getGraph() {
		return graph;
	}

	public PathfindingThread getPathThread() {
		return pathThread;
	}
}
