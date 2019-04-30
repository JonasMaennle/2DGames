package core;

import gamestate.Game;
import static helper.Leveler.*;
import static helper.Collection.*;

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
	
	public StateManager()
	{
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
	}
	
	public void loadLevel()
	{
		CURRENT_LEVEL++;
		handler.wipe();
		shadowObstacleList.clear();

		switch (CURRENT_LEVEL) 
		{
		// Szenario maps
		case 1:
			handler.setMap(loadMap(handler, "level/map_0" + CURRENT_LEVEL));
			break;

		default:
			break;
		}
		
		game.getCamera().setEntity(handler.getCurrentEntity());
	}
}
