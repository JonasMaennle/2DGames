package Gamestate;

import static helpers.Setup.*;
import static helpers.Graphics.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Gamestate.StateManager.GameState;
import UI.HeadUpDisplay;
import data.Camera;
import data.Handler;
import shader.Light;

public class Game {
	
	private Camera camera;
	private HeadUpDisplay ingame_HUD;
	private Handler handler;
	private Light sun;
	private Image background;
	
	public Game(Handler handler)
	{
		this.handler = handler;
		this.camera = new Camera(handler.player);
		
		this.background = quickLoaderImage("background/background");
		setupUI();
	}
	
	public void update()
	{
		camera.update();
		handler.update();
		if(sun != null)sun.setLocation(new Vector2f(WIDTH*3 + MOVEMENT_X, -HEIGHT*3 + MOVEMENT_Y));

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
		background.draw(0, 0);
		
		// draw map, game-objects
		handler.draw();
		renderLightEntity(shadowObstacleList);
		
		// draw ingame UI
		updateUI();	
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
	
	public HeadUpDisplay getHeadUpDisplay()
	{
		return ingame_HUD;
	}

	public void deathScreen() 
	{
		render();

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
