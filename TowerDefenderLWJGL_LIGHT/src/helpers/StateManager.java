package helpers;

import data.Editor;
import data.Game;
import data.MainMenu;
import data.TileGrid;
import static helpers.Leveler.*;
import static helpers.Artist.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME, EDITOR
	}
	
	public static GameState gameState = GameState.MAINMENU;
	public static MainMenu mainMenu;
	public static Game game;
	public static Editor editor;
	
	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	static TileGrid map = chooseMapSize();
	
	
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
		case EDITOR:
			if(editor == null)
				editor = new Editor();
			editor.update();
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
	
	private static TileGrid chooseMapSize()
	{
		TileGrid map;
		if(GAME_WIDTH == 1088)
		{
			map = loadMap("mapSmall");
		}else if(GAME_WIDTH == 1280)
		{
			map = loadMap("mapLarge");
		}else{
			map = loadMap("mapDefault");
		}
		return map;
	}
}
