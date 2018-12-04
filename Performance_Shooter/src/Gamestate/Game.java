package Gamestate;

import static helpers.Setup.*;
import static helpers.Graphics.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import Gamestate.StateManager.GameState;
import UI.HeadUpDisplay;
import data.Camera;
import data.Handler;
import shader.Light;

public class Game {
	
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private HeadUpDisplay ingame_HUD;
	private Handler handler;
	private Light sun;
	
	// Tmp
	//private Light mouseLight;
	
	public Game(Handler handler)
	{
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler(handler.getCurrentEntity(), handler);
		this.handler = handler;
		
		this.sun = new Light(new Vector2f(WIDTH, 1), 200, 80, 0, 0.4f); // 200, 80, 0, 0.1f);
		
		setupUI();
		setUpObjects();
	}
	
	public void update()
	{
		camera.update();
		handler.update();
		sun.setLocation(new Vector2f(WIDTH + 700, -700));
		
		while(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
		// render entire game graphics
		render();
	}
	
	// Renders lightning into the game
	public void render() 
	{
		// draw background
		backgroundHandler.draw();
		
		// render sun, lava
		renderSingleLightStatic(shadowObstacleList, sun);

		// draw map, game-objects
		handler.draw();
		renderLightEntity(shadowObstacleList);
		// draw ingame UI
		updateUI();
		
		//mouseLight.setLocation(new Vector2f(Mouse.getX(), HEIGHT - Mouse.getY()));	
		
		//render lights (laser etc.)
	}
	
	private void updateUI()
	{
		ingame_HUD.draw();
		ingame_HUD.drawString(5, HEIGHT-40, "FPS: " + StateManager.framesInLastSecond);
		ingame_HUD.drawString(150, HEIGHT-40, " ");	
	}
	
	private void setupUI()
	{
		ingame_HUD = new HeadUpDisplay(handler);	
	}
	
	private void setUpObjects() 
	{
		//mouseLight = new Light(new Vector2f(0, 0), 10, 2, 0, 1);
		//lights.add(mouseLight);
	}
	
	public BackgroundHandler getBackgroundHandler()
	{
		return backgroundHandler;
	}
	
	public HeadUpDisplay getHeadUpDisplay()
	{
		return ingame_HUD;
	}

	public void deathScreen() 
	{
		render();
		backgroundHandler.endScreen(0.90f);
		ingame_HUD.drawString(WIDTH/2-100, HEIGHT/2 - 200, " Press 'r' to Restart the Game");
		ingame_HUD.drawString(WIDTH/2-100, HEIGHT/2 - 140, "or 'm' to return to the Main Menu");

		if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			HeadUpDisplay.playerHealth = 100;
			handler.getStatemanager().resetCurrentLevel();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_M))
		{
			StateManager.CURRENT_LEVEL = 0;
			MOVEMENT_X = 0;
			MOVEMENT_Y = 0;
			StateManager.gameState = GameState.MAINMENU;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			AL.destroy();
			Display.destroy();
			System.exit(0);
		}
	}

	public void setCamera(Camera camera) 
	{
		this.camera = camera;
	}
	
	public void setBackgroundHandler(BackgroundHandler bh)
	{
		this.backgroundHandler = bh;
	}

	public Camera getCamera() {
		return camera;
	}

	public Light getSun() {
		return sun;
	}

	public void setSun(Light sun) {
		this.sun = sun;
	}
}
