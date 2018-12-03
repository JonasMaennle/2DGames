package Gamestate;

import data.Handler;
import data.MusicHandler;
import object.Shotgun;
import shader.Light;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import org.lwjgl.util.vector.Vector2f;

import UI.HeadUpDisplay;

import static helpers.Leveler.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME, DEAD, LOADING, EDITOR
	}
	
	// Start parameter
	private static final int START_LEVEL = 1;
	public static GameState gameState = GameState.MAINMENU; // initial state -> gameState = GameState.MAINMENU;
	public static String ENVIRONMENT_SETTING = "";
	
	public static GameState lastState = GameState.MAINMENU;
	public static int CURRENT_LEVEL = (START_LEVEL - 1);

	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	private MainMenu mainMenu;
	private Game game;
	private Handler handler;
	private LoadingScreen loadingScreen;
	private MusicHandler musicHandler;
	private Editor editor;
	
	public StateManager()
	{
		this.musicHandler = new MusicHandler();
		this.loadingScreen = new LoadingScreen(this);
		this.mainMenu = new MainMenu();	
		this.handler = new Handler(this);
		this.game = new Game(handler);
		this.editor = new Editor(handler, game);
	}
	
	public void update()
	{
		musicHandler.update();
		
		switch (gameState) {
		case MAINMENU:
			mainMenu.update();
			break;
			
		case GAME:
			if(CURRENT_LEVEL == (START_LEVEL - 1)) // Just for init state = GAME
			{
				gameState = GameState.LOADING;
				lastState = GameState.GAME;
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
			
		case EDITOR:
			editor.update();
			break;
			
		default:
			System.out.println("No gamestate found!");
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
		shadowObstacleList.clear();

		switch (CURRENT_LEVEL) 
		{
		// Szenario maps
		case 1:
			handler.setMap(loadMap(handler, "maps/map_" + CURRENT_LEVEL));
			break;
		case 2:
			handler.setMap(loadMap(handler, "maps/map_" + CURRENT_LEVEL));
			break;
		case 3:
			handler.setMap(loadMap(handler, "maps/map_" + CURRENT_LEVEL));
			break;
		case 4:
			game.getBackgroundHandler().setCustomBackground(quickLoaderImage("background/background_snow01"), quickLoaderImage("background/background_snow00"));
			ENVIRONMENT_SETTING = "_Snow";
			handler.setMap(loadMap(handler, "maps/map_" + CURRENT_LEVEL));
			game.setSun(new Light(new Vector2f(WIDTH + 700, -700), 153, 214, 255, 0.4f));
			break;
		case 5:
			game.getBackgroundHandler().setCustomBackground(quickLoaderImage("background/background_03"), quickLoaderImage("background/background_00"));
			ENVIRONMENT_SETTING = "";
			game.setSun(new Light(new Vector2f(WIDTH + 700, -700), 200, 80, 0, 0.4f));
			
			loadingScreen = new LoadingScreen(this);
			mainMenu.enterMainMenu();
			gameState = GameState.MAINMENU;
			break;

		// Editor Maps
		case -1:
			handler.setMap(loadMap(handler, "maps/editor_map_" + (CURRENT_LEVEL * -1)));
			break;
		case -2:
			handler.setMap(loadMap(handler, "maps/editor_map_" + (CURRENT_LEVEL * -1)));
			break;
		case -3:
			handler.setMap(loadMap(handler, "maps/editor_map_" + (CURRENT_LEVEL * -1)));
			break;
		case -4:
			handler.setMap(loadMap(handler, "maps/editor_map_" + (CURRENT_LEVEL * -1)));
			break;
		default:
			break;
		}
		if(HeadUpDisplay.shotsLeft > 0 && gameState != GameState.MAINMENU)
			handler.player.setWeapon(new Shotgun(handler.player.getX(), handler.player.getY(), 70, 35, handler.player, handler, quickLoaderImage("player/weapon_shotgun_left"), quickLoaderImage("player/weapon_shotgun_right")));
		
		game.getCamera().setEntity(handler.getCurrentEntity());
		game.getBackgroundHandler().setEntity(handler.getCurrentEntity());
	}
	
	public void resetCurrentLevel()
	{
		if(lastState != GameState.EDITOR)CURRENT_LEVEL--;
		loadLevel();
		gameState = GameState.GAME;
	}
	
	public static void setState(GameState newState)
	{
		gameState = newState;
	}

	public Game getGame() {
		return game;
	}
	
	public Editor getEditor()
	{
		return editor;
	}
	
	public MainMenu getMainMenu()
	{
		return mainMenu;
	}
}
