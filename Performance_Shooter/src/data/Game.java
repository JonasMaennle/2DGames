package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;


import UI.UI;
import helpers.StateManager;
import shader.Shader;

public class Game {
	
	private Shader shader;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private UI ingame_HUD;
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
		ingame_HUD = new UI();
	}
	
	private void initShader()
	{
		shader = new Shader();
		shader.loadFragmentShader("res/shaders/shader.frag");
		shader.compile();

		glEnable(GL_STENCIL_TEST);
		glClearColor(0, 0, 0, 0);
	}
	
	private void setUpObjects() 
	{
		//lights.add(mouseLight);
	}
}
