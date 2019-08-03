package Gamestate;

import static helpers.Graphics.*;
import static helpers.Leveler.*;
import static helpers.Setup.*;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager.GameState;
import UI.UI;
import UI.UI.Menu;
import data.Camera;
import data.Handler;
import data.TileGrid;
import object.Goal;
import object.Tile;
import object.enemy.Enemy;
import object.enemy.GunganEnemy;
import object.entity.AT_ST_Walker;
import object.entity.Player;
import object.entity.Speeder;

// cant save maps

public class Editor {

	private TileGrid grid;

	private UI editorUI, editorUI_tree, menuUI, controllsUI;
	private Menu editorMainMenu, editorMainMenu_Page2;
	private Image menuBackground, space, tile_grid, menu_background_filter;
	private Handler handler;
	private Game game;
	private int activeLevel, menuY;
	private boolean editor_state, createLevel_state, menu_state;
	private boolean mouseDown;
	private int menuIndex;
	
	private Entity selectedEntity;
	private TileType selectedType;
	private Player player;
	private Speeder speeder;
	private AT_ST_Walker at_st_walker;
	private Goal goal;
	private CopyOnWriteArrayList<Enemy> enemyList;

	public Editor(Handler handler, Game game) 
	{
		this.handler = handler;
		this.game = game;
		this.activeLevel = 0;
		this.editor_state = false;
		this.createLevel_state = false;
		this.menuIndex = 0;

		this.enemyList = new CopyOnWriteArrayList<>();
		this.mouseDown = true;;
		this.menuY = 100;
		this.menu_state = true;
		
		this.menu_background_filter = quickLoaderImage("editor/menu_background");
		this.tile_grid = quickLoaderImage("editor/grid");
		this.space = quickLoaderImage("intro/Background_Space");
		this.menuBackground = quickLoaderImage("editor/background_white");
		Mouse.setGrabbed(false);
		Mouse.setCursorPosition(WIDTH/2, HEIGHT/2);
		setupUI();
	}

	public void update() 
	{
		draw();
		
		if(editor_state)
		{	
			checkClickedButtons();
			// Handle Keyboad Input
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) 
			{
				MOVEMENT_X += 64;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) 
			{
				MOVEMENT_X -= 64;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) 
			{
				MOVEMENT_Y += 64;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) 
			{
				MOVEMENT_Y -= 64;
			}
			
			if(selectedEntity != null)
			{
				selectedEntity.setX(Mouse.getX() - selectedEntity.getWidth()/2 - MOVEMENT_X);
				selectedEntity.setY(HEIGHT - Mouse.getY() - selectedEntity.getHeight()/2 - MOVEMENT_Y);
			}
			
			// Remove tile/entity
			if(Mouse.isButtonDown(1))
			{
				setTile(TileType.NULL);
				for(Enemy g : enemyList)
				{
					if(checkCollision(g.getX(), g.getY(), g.getWidth(), g.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y, 2, 2))
					{
						enemyList.remove(g);
					}
				}
				if(player != null)
				{
					if(checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y, 2, 2))
					{
						player = null;
					}
				}
				if(selectedEntity != null)
				{
					selectedEntity = null;
					selectedType = null;
				}
				if(speeder != null)
				{
					if(checkCollision(speeder.getX(), speeder.getY(), speeder.getWidth(), speeder.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y, 2, 2))
					{
						speeder = null;
					}
				}
				if(at_st_walker != null)
				{
					if(checkCollision(at_st_walker.getX(), at_st_walker.getY(), at_st_walker.getWidth(), at_st_walker.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y, 2, 2))
					{
						at_st_walker = null;
					}
				}
				if(goal != null)
				{
					if(checkCollision(goal.getX(), goal.getY(), goal.getWidth(), goal.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y, 2, 2))
					{
						goal = null;
					}
				}
			}
			
			// draw tile
			if(Mouse.isButtonDown(0)&& Mouse.getX()-MOVEMENT_X < getRightBorder() - 300)
			{
				if(selectedType != null)setTile(selectedType);
			}
			
			// Draw non-Tile
			if(!Mouse.isButtonDown(0) && selectedEntity != null &&  Mouse.getX()-MOVEMENT_X < getRightBorder() - 350)
			{
				if(selectedEntity.getClass().getSimpleName().equals("Player"))
				{
					player = (Player) selectedEntity;
					player.setX(Mouse.getX()-MOVEMENT_X - Mouse.getX()%TILE_SIZE);
					player.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE);
					selectedEntity = null;
					return;
				}
				if(selectedEntity.getClass().getSimpleName().equals("GunganEnemy"))
				{
					GunganEnemy tmp = (GunganEnemy) selectedEntity; 
					tmp.setX(Mouse.getX()-MOVEMENT_X - Mouse.getX()%TILE_SIZE);
					tmp.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE);
					enemyList.add(tmp);
					selectedEntity = null;
					return;
				}
				if(selectedEntity.getClass().getSimpleName().equals("Speeder"))
				{
					speeder = (Speeder) selectedEntity; 
					speeder.setX(Mouse.getX()-MOVEMENT_X - Mouse.getX()%64);
					speeder.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE*2);
					selectedEntity = null;
					return;
				}
				if(selectedEntity.getClass().getSimpleName().equals("AT_ST_Walker"))
				{
					at_st_walker = (AT_ST_Walker) selectedEntity; 
					at_st_walker.setX(Mouse.getX()-MOVEMENT_X - Mouse.getX()%TILE_SIZE);
					at_st_walker.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE*2);
					selectedEntity = null;
					return;
				}
				if(selectedEntity.getClass().getSimpleName().equals("Goal"))
				{
					goal = (Goal) selectedEntity; 
					goal.setX(Mouse.getX()-MOVEMENT_X - Mouse.getX()%TILE_SIZE);
					goal.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE);
					selectedEntity = null;
					return;
				}
			}
		}

		if(menu_state)
		{
			// Handle Mouse Input
			if(Mouse.next())
			{
				boolean mouseClicked = Mouse.isButtonDown(0);
				if(mouseClicked)
				{
					if(menuUI.isButtonClicked("Map1"))
					{
						setUpNewMap(1);
					}
					
					if(menuUI.isButtonClicked("Map2"))
					{
						setUpNewMap(2);
					}
					
					if(menuUI.isButtonClicked("Map3"))
					{
						setUpNewMap(3);
					}
					
					if(menuUI.isButtonClicked("Map4"))
					{
						setUpNewMap(4);
					}
					
					if(menuUI.isButtonClicked("Return"))
					{
						handler.getStatemanager().getMainMenu().enterMainMenu();
						StateManager.gameState = GameState.MAINMENU;
					}
				}
			}
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			AL.destroy();
			Display.destroy();
			System.exit(0);
		}
	}
	
	private void draw()
	{
		if(editor_state)
		{
			drawQuadImageStatic(menuBackground, 0, 0, 2048, 2048);
			grid.draw();
			drawBackgroundGrid();
			drawQuadImageStatic(menu_background_filter, Display.getWidth()-300, 0, 512, HEIGHT);
			
			switch (menuIndex) {
			case 0:
				editorUI.draw();
				break;
			case 1:
				editorUI_tree.draw();
				break;
			default:
				break;
			}

			
			controllsUI.draw();
			if(selectedEntity != null)
				selectedEntity.draw();
			
			if(player != null)
				player.draw();
			
			for(Enemy g : enemyList)
			{
				g.draw();
			}
			if(speeder != null)
				speeder.draw();
			
			if(at_st_walker != null)
				at_st_walker.draw();
			
			if(goal != null)
				goal.draw();
			
		}else if(createLevel_state){
			drawQuadImageStatic(space, 0, 0, 2048, 2048);
		}else if(menu_state){
			drawQuadImageStatic(space, 0, 0, 2048, 2048);
			menuUI.draw();
		}
	}

	private void setTile(TileType t) 
	{
		if(t == TileType.NULL)
		{
			for(int i = 0; i < handler.obstacleList.size(); i++)
			{
				if(handler.obstacleList.get(i).getXPlace() == (int) Math.floor(Mouse.getX() / TILE_SIZE) - (int)MOVEMENT_X/TILE_SIZE 
						&& handler.obstacleList.get(i).getYPlace() == (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE) - (int)MOVEMENT_Y/TILE_SIZE){
					handler.obstacleList.remove(i);
					
				}
			}
		}
		
		if(null != grid.getTile((int) Math.floor(Mouse.getX() / TILE_SIZE) - (int)MOVEMENT_X/TILE_SIZE, (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE) - (int)MOVEMENT_Y/TILE_SIZE))
		{
			grid.setTile((int) Math.floor(Mouse.getX() / TILE_SIZE) - (int)MOVEMENT_X/TILE_SIZE, (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE) - (int)MOVEMENT_Y/TILE_SIZE, t);
			if(t != TileType.NULL)handler.obstacleList.add(grid.getTile((int) Math.floor(Mouse.getX() / TILE_SIZE) - (int)MOVEMENT_X/TILE_SIZE, (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE) - (int)MOVEMENT_Y/TILE_SIZE));
		}	
	}
	
	private void setUpNewMap(int map)
	{
		activeLevel = map;
		menu_state = false;
		editor_state = true;
		createLevel_state = false;
		this.grid = loadMap(this.handler, "maps/editor_map_" + activeLevel);
		
		// update editor non-tile game objects
		if(handler.player != null)
			player = handler.player;
		if(handler.speeder != null)
			speeder = handler.speeder;
		if(handler.at_st_walker != null)
			at_st_walker = handler.at_st_walker;
		if(handler.levelGoal != null)
			goal = handler.levelGoal;
		if(handler.enemyList.size() != 0)
			enemyList = handler.enemyList;
	}
	
	private void saveMap(int width, int height)
	{
		try {
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bi.createGraphics();
			
			graphics.setPaint(new Color(255, 255, 255));
			//graphics.setPaint(new Color(16777215));
			graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());

			for(int y = 0; y < TILES_HEIGHT; y++)
			{
				for(int x = 0; x < TILES_WIDTH; x++)
				{
					//System.out.println(x + " " + y);
					
					//Tile t = grid.getTile(x, y);
					
					//System.out.println(t);
					graphics.setPaint(new Color(grid.getTile(x, y).getType().rgb));
					graphics.fillRect(x, y, 1, 1);
					
					// add player
					if(player != null)
					{
						graphics.setPaint(new Color(255,0,0));
						graphics.fillRect((int)player.getX()/TILE_SIZE, (int)player.getY()/TILE_SIZE, 1, 1);
					}
					
					if(speeder != null)
					{
						graphics.setPaint(new Color(0,255,255));
						graphics.fillRect((int)speeder.getX() / TILE_SIZE, (int)speeder.getY() / TILE_SIZE, 1, 1);
					}
					
					if(at_st_walker != null)
					{
						graphics.setPaint(new Color(0,0,255));
						graphics.fillRect((int)at_st_walker.getX() / TILE_SIZE, (int)at_st_walker.getY() / TILE_SIZE, 1, 1);
					}
					
					for(Enemy g : enemyList)
					{
						graphics.setPaint(new Color(0,255,120));
						graphics.fillRect((int)g.getX() / TILE_SIZE, (int)g.getY() / TILE_SIZE, 1, 1);
					}
					
					if(goal != null)
					{
						graphics.setPaint(new Color(255,0,255));
						graphics.fillRect((int)goal.getX() / TILE_SIZE, (int)goal.getY() / TILE_SIZE, 1, 1);
					}
				}
			}
			ImageIO.write(bi, "PNG", new File("res/maps/editor_map_" + activeLevel + ".png"));
			System.out.println("Saved:   res/maps/editor_map_" + activeLevel + ".png");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void drawBackgroundGrid()
	{
		for(int h = 0; h < grid.getTilesHigh(); h++)
		{
			for(int w = 0; w < grid.getTilesWide(); w++)
			{
				drawQuadImage(tile_grid, w * TILE_SIZE, h * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
	}
	
	private void transmitDatatoHandler()
	{
		StateManager.CURRENT_LEVEL = -activeLevel -1;
		
		handler.setMap(grid);
		handler.enemyList = enemyList;
		handler.player = player;
		handler.speeder = speeder;
		handler.at_st_walker = at_st_walker;
		handler.levelGoal = goal;
		handler.setCurrentEntity(player);
		
		game.setCamera(new Camera(handler.getCurrentEntity()));
		game.setBackgroundHandler(new BackgroundHandler(handler.player, handler));
	}
	
	public void transmitDataFromHandler()
	{
		editor_state = false;
		menu_state = true;
		
		activeLevel = StateManager.CURRENT_LEVEL * -1;
		grid = handler.getMap();
		//this.grid = loadMap(this.handler, "maps/editor_map_" + activeLevel);
		
		enemyList = handler.enemyList;
		player = handler.player;
		speeder = handler.speeder;
		at_st_walker = handler.at_st_walker;
		goal = handler.levelGoal;
		
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}
	
	private void setupUI()
	{
		menuUI = new UI();

		menuUI.addButton("Map1", "editor/button_map1", (WIDTH/2) - 128, menuY, 256, 128);
		menuY += 150;
		menuUI.addButton("Map2", "editor/button_map2", (WIDTH/2) - 128, menuY, 256, 128);
		menuY += 150;
		menuUI.addButton("Map3", "editor/button_map3", (WIDTH/2) - 128, menuY, 256, 128);
		menuY += 150;
		menuUI.addButton("Map4", "editor/button_map4", (WIDTH/2) - 128, menuY, 256, 128);
		menuUI.addButton("Return", "intro/Return", (int)getLeftBorder() + 10, (int)getTopBorder() + 10, 128, 64);
		
		editorUI = new UI();
		editorUI.createMenu("editorUI", (int)getRightBorder()-256, 128, 256, HEIGHT, 4, 4); // Menu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight)
		editorMainMenu = editorUI.getMenu("editorUI");
		
		editorUI_tree = new UI();
		editorUI_tree.createMenu("editorUI_tree", (int)getRightBorder()-256, 128, 256, HEIGHT, 4, 4);
		editorMainMenu_Page2 = editorUI_tree.getMenu("editorUI_tree");

		editorMainMenu.quickAdd("Grass_Flat", "tiles/Grass_Flat", 64, 64);
		editorMainMenu.quickAdd("Grass_Left", "tiles/Grass_Left", 64, 64);
		editorMainMenu.quickAdd("Grass_Right", "tiles/Grass_Right", 64, 64);
		editorMainMenu.quickAdd("Grass_Round", "tiles/Grass_Round", 64, 64);
		
		editorMainMenu.quickAdd("Grass_Flat_Half", "tiles/Grass_Flat_Half", 64, 32);
		editorMainMenu.quickAdd("Grass_Left_Half", "tiles/Grass_Left_Half", 64, 32);
		editorMainMenu.quickAdd("Grass_Right_Half", "tiles/Grass_Right_Half", 64, 32);
		editorMainMenu.quickAdd("Grass_Round_Half", "tiles/Grass_Round_Half", 64, 32);
		
		editorMainMenu.quickAdd("Dirt_Basic", "tiles/Dirt_Basic", 64, 64);
		editorMainMenu.quickAdd("Ramp_start", "tiles/Ramp_start", 64, 64);
		editorMainMenu.quickAdd("Ramp_end", "tiles/Ramp_end", 64, 64);
		editorMainMenu.quickAdd("Rock_Basic_0", "tiles/Rock_Basic_0", 64, 64);
		
		editorMainMenu.quickAdd("Player", "player/player_tmp", 64, 128);
		editorMainMenu.quickAdd("Gungan", "enemy/enemy_right", 64, 128);
		editorMainMenu.quickAdd("Goal", "objects/goal", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu.quickAdd("Speeder", "player/endor_speeder_right", 128, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu.quickAdd("AT_ST_Walker", "player/atst_tmp", 128, 128);
		
		editorMainMenu_Page2.quickAdd("Redwood_01", "tiles/redwood_01", 64, 128);
		editorMainMenu_Page2.quickAdd("Redwood_02", "tiles/redwood_02", 64, 128);
		editorMainMenu_Page2.quickAdd("Redwood_03", "tiles/redwood_03", 64, 128);
		editorMainMenu_Page2.quickAdd("Redwood_04", "tiles/redwood_04", 64, 128);
		
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu_Page2.quickAdd("Redwood_05", "tiles/redwood_05", 64, 96);
		editorMainMenu_Page2.quickAdd("Redwood_06", "tiles/redwood_06", 64, 96);
		editorMainMenu_Page2.quickAdd("Redwood_07", "tiles/redwood_01", 64, 96);
		editorMainMenu_Page2.quickAdd("Redwood_09", "tiles/redwood_01", 64, 164);
		
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu_Page2.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu_Page2.quickAdd("Redwood_08", "tiles/redwood_01", 48, 80);
		editorMainMenu_Page2.quickAdd("tree_stump_left", "tiles/tree_stump_left", 64, 64);
		editorMainMenu_Page2.quickAdd("tree_stump_center", "tiles/tree_stump_center", 64, 64);
		editorMainMenu_Page2.quickAdd("tree_stump_right", "tiles/tree_stump_right", 64, 64);
		
		controllsUI = new UI();
		controllsUI.addButton("play", "editor/button_play", (int)getRightBorder()-280, 0, 128, 48);
		controllsUI.addButton("save", "editor/button_save", (int)getRightBorder()-140, 0, 128, 48);
		controllsUI.addButton("Return", "intro/Return", (int)getLeftBorder() + 10, (int)getTopBorder() + 10, 128, 64);
		controllsUI.addButton("Page_Left", "editor/left", (int)getRightBorder()-260, (int)getTopBorder() + 50, 100, 50);
		controllsUI.addButton("Page_Right", "editor/right", (int)getRightBorder()-120, (int)getTopBorder() + 50, 100, 50);
	}
	
	private void checkClickedButtons()
	{
		// Handle Mouse Input
		if(Mouse.next())
		{
			if(!Mouse.isButtonDown(0))
				mouseDown = false;
			boolean mouseClicked = Mouse.isButtonDown(0);
			
			if(mouseClicked && !mouseDown)
			{
				if(controllsUI.isButtonClicked("play"))
				{
					saveMap(grid.getTilesWide(), grid.getTilesHigh());
					transmitDatatoHandler();
					
					StateManager.lastState = GameState.EDITOR;
					StateManager.gameState = GameState.GAME;
				}
				if(controllsUI.isButtonClicked("save"))
				{
					saveMap(grid.getTilesWide(), grid.getTilesHigh());
				}
				
				if(controllsUI.isButtonClicked("Return"))
				{
					StateManager.CURRENT_LEVEL = 0;
					this.editor_state = false;
					this.menu_state = true;
				}
				if(controllsUI.isButtonClicked("Page_Left"))
				{
					if(menuIndex > 0)
						menuIndex--;
				}
				if(controllsUI.isButtonClicked("Page_Right"))
				{
					if(menuIndex < 1)
						menuIndex++;
				}
			}
			
			
			if(mouseClicked && !mouseDown && menuIndex == 0)
			{
				if(editorMainMenu.isButtonClicked("Grass_Flat")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Flat, handler);
					selectedType = TileType.Grass_Flat;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Left")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Left, handler);
					selectedType = TileType.Grass_Left;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Right")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Right, handler);
					selectedType = TileType.Grass_Right;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Round")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Round, handler);
					selectedType = TileType.Grass_Round;
					mouseDown = true;
					return;
				}
				
				
				if(editorMainMenu.isButtonClicked("Grass_Flat_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Flat_Half, handler);
					selectedType = TileType.Grass_Flat_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Left_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Left_Half, handler);
					selectedType = TileType.Grass_Left_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Right_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Right_Half, handler);
					selectedType = TileType.Grass_Right_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Round_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Grass_Round_Half, handler);
					selectedType = TileType.Grass_Round_Half;
					mouseDown = true;
					return;
				}
				
				
				if(editorMainMenu.isButtonClicked("Dirt_Basic")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Dirt_Basic, handler);
					selectedType = TileType.Dirt_Basic;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Ramp_start")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Ramp_Start, handler);
					selectedType = TileType.Ramp_Start;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Ramp_end")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Ramp_End, handler);
					selectedType = TileType.Ramp_End;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Rock_Basic_0")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.Rock_Basic, handler);
					selectedType = TileType.Rock_Basic;
					mouseDown = true;
					return;
				}
				
				if(editorMainMenu.isButtonClicked("Player")){
					selectedEntity = new Player(Mouse.getX(), HEIGHT - Mouse.getY(), handler);
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Gungan")){
					selectedEntity = new GunganEnemy(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE * 2, handler, 2);
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Speeder")){
					selectedEntity = new Speeder(Mouse.getX(), HEIGHT - Mouse.getY(), handler);
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("AT_ST_Walker")){
					selectedEntity = new AT_ST_Walker(Mouse.getX(), HEIGHT - Mouse.getY(), handler);
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Goal")){
					selectedEntity = new Goal(Mouse.getX(), HEIGHT - Mouse.getY());
					mouseDown = true;
					return;
				}
			}
			
			if(mouseClicked && !mouseDown && menuIndex == 1)
			{
				if(editorMainMenu_Page2.isButtonClicked("Redwood_01")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_01, handler);
					selectedType = TileType.RedWood_01;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_02")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_02, handler);
					selectedType = TileType.RedWood_02;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_03")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_03, handler);
					selectedType = TileType.RedWood_03;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_04")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_04, handler);
					selectedType = TileType.RedWood_04;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_05")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_05, handler);
					selectedType = TileType.RedWood_05;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_06")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_06, handler);
					selectedType = TileType.RedWood_06;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_07")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_07, handler);
					selectedType = TileType.RedWood_07;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_08")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_08, handler);
					selectedType = TileType.RedWood_08;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("Redwood_09")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.RedWood_09, handler);
					selectedType = TileType.RedWood_09;
					mouseDown = true;
					return;
				}
				
				if(editorMainMenu_Page2.isButtonClicked("tree_stump_left")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.TreeStump_Left, handler);
					selectedType = TileType.TreeStump_Left;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("tree_stump_center")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.TreeStump_Center, handler);
					selectedType = TileType.TreeStump_Center;
					mouseDown = true;
					return;
				}
				if(editorMainMenu_Page2.isButtonClicked("tree_stump_right")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TileType.TreeStump_Right, handler);
					selectedType = TileType.TreeStump_Right;
					mouseDown = true;
					return;
				}
			}
		}
	}
}
