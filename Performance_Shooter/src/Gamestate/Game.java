package Gamestate;

import static helpers.Setup.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import Gamestate.StateManager.GameState;
import UI.HeadUpDisplay;
import data.Camera;
import data.Handler;
import shader.Shader;

public class Game {
	
	private Shader shader;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private HeadUpDisplay ingame_HUD;
	private Handler handler;

	public Game(Handler handler)
	{
		this.camera = new Camera(handler.player);
		this.backgroundHandler = new BackgroundHandler(handler.player);
		this.handler = handler;
		
		setupUI();
		initShader();
		setUpObjects();
	}
	
	public void update()
	{
		camera.update();
		handler.update();
		
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
		handler.draw();
		
		// draw ingame UI
		updateUI();
		
		//mouseLight.setLocation(new Vector2f(Mouse.getX(), HEIGHT - Mouse.getY()));
		// lightList, entityList, shader
		//renderLightEntity(towerList, shader);
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
	
	private void initShader()
	{
		shader = new Shader();
		shader.loadFragmentShader("not used -> path in Shader");
		shader.compile();

		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}
	
	private void setUpObjects() 
	{
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
			handler.getStatemanager().resetCurrentLevel();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_M))
		{
			handler.getStatemanager().resetCurrentLevel();
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
}
