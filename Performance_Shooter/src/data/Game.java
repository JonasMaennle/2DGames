package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import UI.UI;
import helpers.Leveler;
import helpers.StateManager;
import object.Cloud;
import shader.Light;
import shader.Shader;

public class Game {

	private TileGrid grid;
	private Player player;
	private Shader shader;
	private Camera camera;
	private UI ingame_HUD;
	private Texture background_grass_1,background_grass_0, sky, background_lp;
	private float alpha;
	private int background_offset1, background_offset2;
	private CopyOnWriteArrayList<Cloud> cloudList;
	private Random rand;
	
	// Tmp
	private Light mouseLight;

	public Game(TileGrid grid)
	{
		this.grid = grid;
		this.player = new Player(Leveler.playerX, Leveler.playerY, grid);
		this.camera = new Camera(player);
		this.background_grass_1 = quickLoad("Background_lp3");
		this.background_grass_0 = quickLoad("Background_lp3");
		this.background_lp = quickLoad("Background_lp");
		this.sky = quickLoad("Himmel");
		this.alpha = 0.9f;
		this.background_offset1 = 0;
		this.background_offset2 = WIDTH;
		this.cloudList = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		
		setupUI();
		initShader();
		setUpObjects();
	}
	
	public void update()
	{
		drawBackground();
		camera.update();
		grid.draw();
		
		// draw alpha game filter
		GL11.glColor4f(0, 0, 0, alpha);
		//drawQuadTex(filter, 0, 0, 2048, 1080);
		GL11.glColor4f(1, 1, 1, 1);
		
		while(Keyboard.next())
		{
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				System.exit(0);
			}
		}
		//render light
		render();
		
		player.update();	
		updateUI();
		cloudSpawner();
	}
	
	private void updateUI()
	{
		ingame_HUD.draw();
		ingame_HUD.drawString(5, HEIGHT-40, "FPS: " + StateManager.framesInLastSecond);
		ingame_HUD.drawString(150, HEIGHT-40, " ");
	}
	
	// Renders lightning into the game
	public void render() 
	{
		mouseLight.setLocation(new Vector2f(Mouse.getX(), HEIGHT - Mouse.getY()));
		
		// lightList, entityList, shader
		//renderLightEntity(towerList, shader);
	}
	
	private void setupUI()
	{
		ingame_HUD = new UI();
	}
	
	private void drawBackground()
	{
		drawQuadTex(sky, 0 - MOVEMENT_X, 0, 2048, 2048);
		drawQuadTex(background_lp, 0 - MOVEMENT_X, -350, WIDTH, 2048);
		
		// moving forground
		int invertMX = MOVEMENT_X * -1;
		if(invertMX % WIDTH == 0)
		{
			background_offset1 = invertMX;
			background_offset2 = invertMX + WIDTH;
		}
		drawQuadTex(background_grass_0, 0 + background_offset1, -240, WIDTH, 2048);
		drawQuadTex(background_grass_1, 0 + background_offset2, -240, WIDTH, 2048);
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
		mouseLight = new Light(new Vector2f(0, 0), 10, 2, 0, 50);
		//lights.add(mouseLight);
	}
	
	private void cloudSpawner()
	{
		if(rand.nextInt(600) == 0 && cloudList.size() <= 3)
		{
			cloudList.add(new Cloud((MOVEMENT_X * -1) + WIDTH, 10));
		}
		
		for(Cloud c : cloudList)
		{
			c.update();
			c.draw();
			if(c.getX() + c.getWidth() < (MOVEMENT_X * -1))
			{
				cloudList.remove(c);
			}
		}
	}
}
