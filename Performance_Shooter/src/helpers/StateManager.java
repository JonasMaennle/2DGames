package helpers;

import data.Game;
import data.MainMenu;
import data.TileGrid;
import static helpers.Leveler.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME
	}
	
	public static GameState gameState = GameState.GAME; // -> gameState = GameState.MAINMENU;
	public static MainMenu mainMenu;
	public static Game game;
	
	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	static TileGrid map = loadMap();
	
	
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
				game = new Game(map);
			game.update();
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
	
	public static void setState(GameState newState)
	{
		gameState = newState;
	}
}
