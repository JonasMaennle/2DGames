package helpers;

import data.Game;
import data.Handler;
import data.MainMenu;
import data.TileGrid;
import static helpers.Leveler.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME, DEAD
	}
	
	public static GameState gameState = GameState.MAINMENU; // initial state -> gameState = GameState.MAINMENU;
	public static MainMenu mainMenu;
	public static Game game;
	public static Handler handler = new Handler();
	public static int CURRENT_LEVEL = 1;
	
	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	public static TileGrid map = loadMap(handler);
	
	
	public static void update()
	{
		switch (gameState) {
		case MAINMENU:
			if(mainMenu == null)
				mainMenu = new MainMenu();
			mainMenu.update();
			break;
		case GAME:
			if(game == null)
				game = new Game(handler);
			game.update();
			break;
		case DEAD:
			game.deathScreen();
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
	
	public static void resetCurrentLevel()
	{
		handler = new Handler();
		map = loadMap(handler);
		game = new Game(handler);	
		gameState = GameState.GAME;
	}
	
	public static void setState(GameState newState)
	{
		gameState = newState;
	}
}
