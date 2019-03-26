package Gamestate;

import data.Handler;
import shader.Light;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import UI.HeadUpDisplay;

import static helpers.Leveler.*;

public class StateManager {
	
	public static enum GameState{
		MAINMENU, GAME, DEAD, EDITOR, LOADING
	}
	
	// Start parameter
	private static final int START_LEVEL = 1;
	public static GameState gameState = GameState.LOADING; // initial state -> gameState = GameState.MAINMENU;
	public static String ENVIRONMENT_SETTING = "";
	public static String ENVIRONMENT = "";
	
	public static GameState lastState = GameState.MAINMENU;
	public static int CURRENT_LEVEL = (START_LEVEL - 1);

	public static long nextSecond = System.currentTimeMillis() + 1000;
	public static int framesInLastSecond = 0;
	public static int framesInCurrentSecond = 0;
	
	private Game game;
	private Handler handler;
	private Image cursor;
	private Light mouseLight;
	private int red_color, green_color, blue_color;
	private float lightRadius;
	
	public StateManager()
	{
		this.handler = new Handler(this);
		this.game = new Game(handler);
		this.cursor = quickLoaderImage("objects/cursor");
		
		this.red_color = 244;
		this.green_color = 128;
		this.blue_color = 66;
		this.lightRadius = 12;
		
		this.mouseLight = new Light(new Vector2f(0, 0), red_color, green_color, blue_color, lightRadius);
		Mouse.setGrabbed(true);
	}
	
	public void update()
	{
		switch (gameState) {
			
		case GAME:
			game.update();
			break;
			
		case LOADING:
			loadLevel();
			gameState = GameState.GAME;
			break;
			
		case DEAD:
			game.deathScreen();
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
		
		// draw cursor
		mouseLight.setLocation(new Vector2f(Mouse.getX(), HEIGHT - Mouse.getY()));	
		
		//mouseLight.setRadius(300);
		renderSingleLightMouse(shadowObstacleList, mouseLight);
		
		// Mouse update -> depends on rendering
//		if(mouseLight.getRadius() == 0)
//			mouseLight = new Light(new Vector2f(0, 0), 0, 0, 0, 0f);
//		if(mouseLight.getRadius() != 0)
//			mouseLight = new Light(new Vector2f(0, 0), red_color, green_color, blue_color, lightRadius);
		
		drawQuadImageStatic(cursor, Mouse.getX() - 16, HEIGHT - Mouse.getY() - 16, 32, 32);
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
		default:
			break;
		}
//		if(HeadUpDisplay.shotsLeft > 0 && gameState != GameState.MAINMENU)
//			handler.player.setWeapon(new Shotgun(handler.player.getX(), handler.player.getY(), 70, 35, handler.player, handler, quickLoaderImage("player/weapon_shotgun_left"), quickLoaderImage("player/weapon_shotgun_right")));
		HeadUpDisplay.shotsLeft = -1;
		game.getCamera().setPlayer(handler.player);
	}
	
	public void resetCurrentLevel()
	{
		if(lastState != GameState.EDITOR)CURRENT_LEVEL--;
		loadLevel();
		HeadUpDisplay.playerHealth = 100;
		gameState = GameState.GAME;
	}
	
	public static void setState(GameState newState)
	{
		gameState = newState;
	}

	public Game getGame() {
		return game;
	}

	public void setRed_color(int red_color) 
	{
		this.red_color = red_color;
	}

	public void setGreen_color(int green_color) 
	{
		this.green_color = green_color;
	}

	public void setBlue_color(int blue_color) 
	{
		this.blue_color = blue_color;
	}

	public void setLightRadius(float lightRadius) 
	{
		this.lightRadius = lightRadius;
	}
}
