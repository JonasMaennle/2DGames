package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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
	private Texture background_grass_1,background_grass_0, sky, background_lp, background_mountain;
	private float alpha;
	private float background_offset1, background_offset2;
	private float bg00_offset, bg01_offset, bg02_offset;
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
		this.background_mountain = quickLoad("background_00");
		this.sky = quickLoad("Himmel");
		this.alpha = 0.9f;
		this.background_offset1 = 0;
		this.background_offset2 = WIDTH;
		this.cloudList = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		
		this.bg00_offset = Display.getX()-WIDTH;
		this.bg01_offset = Display.getX();
		this.bg02_offset = Display.getX()+WIDTH;
		
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
		// Draw blue sky
		drawQuadTex(sky, 0, 0, 2048, 2048);
		
		// Calculate and draw mountains
		if(player.getVelX() > 0)
		{
			bg00_offset -= 0.1f;
			bg01_offset -= 0.1f; 
			bg02_offset -= 0.1f;
		}else if(player.getVelX() < 0)
		{
			bg00_offset += 0.1f;
			bg01_offset += 0.1f; 
			bg02_offset += 0.1f;
		}
		// Left image
		if(bg00_offset < (-WIDTH * 2))
		{
			bg00_offset = Display.getX()-WIDTH;
		}else if(bg00_offset > Display.getX())
		{
			bg00_offset = Display.getX()-WIDTH;
		}
		// Middle image
		if(bg01_offset < -WIDTH)
		{
			bg01_offset = Display.getX();
		}else if(bg01_offset > WIDTH)
		{
			bg01_offset = Display.getX();
		}
		// Right image
		if(bg02_offset < Display.getX())
		{
			bg02_offset = Display.getX() + WIDTH;
		}else if(bg02_offset > (WIDTH * 2))
		{
			bg02_offset = Display.getX() + WIDTH;
		}
		drawQuadTex(background_mountain, bg00_offset, 0, 2048, 2048);
		drawQuadTex(background_mountain, bg01_offset, 0, 2048, 2048);
		drawQuadTex(background_mountain, bg02_offset, 0, 2048, 2048);

		
		// Draw forest
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
