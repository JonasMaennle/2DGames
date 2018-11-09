package Gamestate;

import data.Camera;
import data.Handler;

import static helpers.Leveler.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME, DEAD, LOADING
	}
	
	public static GameState gameState = GameState.MAINMENU; // initial state -> gameState = GameState.MAINMENU;
	public static int CURRENT_LEVEL = 0;

	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	private MainMenu mainMenu;
	private Game game;
	private Handler handler;
	private LoadingScreen loadingScreen;
	
	public StateManager()
	{
		this.loadingScreen = new LoadingScreen(this);
		this.mainMenu = new MainMenu();	
		this.handler = new Handler(this);
		this.game = new Game(handler);
		//loadLevel();
	}
	
	public void update()
	{
		switch (gameState) {
		case MAINMENU:
			mainMenu.update();
			break;
		case GAME:
			if(CURRENT_LEVEL == 0) // Just for init state = GAME
			{
				gameState = GameState.LOADING;
				return;
			}
			game.update();
			break;
		case DEAD:
			game.deathScreen();
			break;
		case LOADING:
			loadingScreen.update();
			break;
		default:
			
			break;
		}
		
		// Calculate FPS
		long currentTime = System.currentTimeMillis();
		if(currentTime > nextSecond)
		{
			//System.out.println("FPS: " + framesInLastSecond);
			nextSecond += 1000;
			framesInLastSecond = framesInCurrentSecond;
			framesInCurrentSecond = 0;
		}
		framesInCurrentSecond++;
	}
	
	public void loadLevel()
	{
		CURRENT_LEVEL++;
		handler.wipe();

		switch (CURRENT_LEVEL) {
		case 1:
			handler.setMap(loadMap(handler, CURRENT_LEVEL));
			break;
		case 2:
			handler.setMap(loadMap(handler, CURRENT_LEVEL));
			break;

		default:
			break;
		}
		game.setCamera(new Camera(handler.player));
		game.setBackgroundHandler(new BackgroundHandler(handler.player));
	}
	
	public void resetCurrentLevel()
	{
		CURRENT_LEVEL--;
		loadLevel();
		gameState = GameState.GAME;
	}
	
	public static void setState(GameState newState)
	{
		gameState = newState;
	}
}
