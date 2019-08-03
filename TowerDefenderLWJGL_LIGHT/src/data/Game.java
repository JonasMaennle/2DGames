package data;

import static helpers.Artist.*;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static helpers.Leveler.*;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import UI.UI;
import UI.UI.Menu;
import helpers.Leveler;
import helpers.StateManager;
import object.obstacle.Obstacle;
import object.tower.TowerCannonBlue;
import object.tower.TowerCannonIce;
import object.tower.TowerCannonRed;
import object.tower.TowerCannonYellow;
import shader.Light;
import shader.Shader;

public class Game {

	private TileGrid grid;
	private Player player;
	private WaveManager waveManager;
	private UI gameUI;
	private Menu towerPickerMenu;
	private Texture menuBackground;
	private Shader shader;
	private Texture filter;
	private float alpha;
	
	public ArrayList<Tower> towerList = new ArrayList<Tower>();
	
	// Tmp
	private Light mouseLight;

	public Game(TileGrid grid)
	{
		this.grid = grid;
		this.waveManager = new WaveManager(this.grid, 3, 3);
		this.player = new Player(grid, waveManager);
		player.setup();
		this.menuBackground = quickLoad("menu_Background2");
		this.filter = quickLoad("game_alpha_filter2");
		this.alpha = 0.9f;
		
		setupUI();
		initShader();
		setUpObjects();
	}
	
	public void update()
	{
		drawQuadTex(menuBackground, GAME_WIDTH, 0, 256, 1080);
		grid.draw();
		
		// draw alpha game filter
		GL11.glColor4f(0, 0, 0, alpha);
		drawQuadTex(filter, 0, 0, 2048, 1080);
		GL11.glColor4f(1, 1, 1, 1);
		
		//render light
		render();
		
		waveManager.update();
		player.update();	
		updateUI();
	}
	
	private void updateUI()
	{
		gameUI.draw();
		gameUI.drawString(GAME_WIDTH + 50, 30, "Towers");
		gameUI.drawString(GAME_WIDTH + 30, 400, "Lives: " + Player.lives);
		gameUI.drawString(GAME_WIDTH + 30, 450, "Cash: " + Player.cash);
		gameUI.drawString(GAME_WIDTH + 30, 500, "Wave: " + waveManager.getWaveNumber());
		gameUI.drawString(GAME_WIDTH + 30, 550, "Alpha: " + roundNumber(alpha));
		gameUI.drawString(5, HEIGHT-40, "FPS: " + StateManager.framesInLastSecond);
		
		if(Mouse.next())
		{
			boolean mouseClicked = Mouse.isButtonDown(0);
			if(mouseClicked)
			{
				if(towerPickerMenu.isButtonClicked("CannonBlue"))
				{
					player.pickTower(new TowerCannonBlue(TowerType.CannonBlue, player.getMouseTile(), waveManager.getCurrentWave().getEnemyList()));
				}	
				if(towerPickerMenu.isButtonClicked("CannonIce"))
				{
					player.pickTower(new TowerCannonIce(TowerType.CannonIce, player.getMouseTile(), waveManager.getCurrentWave().getEnemyList()));
				}
				if(towerPickerMenu.isButtonClicked("CannonRed"))
				{
					player.pickTower(new TowerCannonRed(TowerType.CannonRed, player.getMouseTile(), waveManager.getCurrentWave().getEnemyList()));
				}
				if(towerPickerMenu.isButtonClicked("CannonYellow"))
				{
					player.pickTower(new TowerCannonYellow(TowerType.CannonYellow, player.getMouseTile(), waveManager.getCurrentWave().getEnemyList()));
				}	
				
				if(gameUI.isButtonClicked("Plus"))
				{
					if(alpha <= 0.95f)
					{
						alpha += 0.05f;
					}else{
						alpha = 1;
					}
						
				}	
				if(gameUI.isButtonClicked("Minus"))
				{
					if(alpha >= 0.05f)
					{
						alpha -= 0.05f;
					}else{
						alpha = 0;
					}	
				}	
			}
		}
	}
	
	// Renders lightning into the game
	public void render() 
	{
		mouseLight.setLocation(new Vector2f(Mouse.getX(), HEIGHT - Mouse.getY()));
		towerList = player.getTowerList();
		
		for(Obstacle ob : obstacleList)
		{
			ob.update();
		}
		
		// lightList, entityList, shader
		renderLightEntity(towerList, shader);
		renderLightEntity(Leveler.obstacleList, shader);
	}
	
	private void setupUI()
	{
		gameUI = new UI();
		gameUI.createMenu("TowerPicker", GAME_WIDTH, 100, 192, HEIGHT, 2, 0);
		towerPickerMenu = gameUI.getMenu("TowerPicker");
		
		towerPickerMenu.quickAdd("CannonBlue", "cannonBlueFull");
		towerPickerMenu.quickAdd("CannonIce", "cannonIceFull");
		towerPickerMenu.quickAdd("CannonRed", "cannonRedFull");
		towerPickerMenu.quickAdd("CannonYellow", "cannonYellowFull");
		
		gameUI.addButton("Plus", "plus_Button", GAME_WIDTH + 20, 600);
		gameUI.addButton("Minus", "minus_Button", WIDTH - 84, 600);
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
}
