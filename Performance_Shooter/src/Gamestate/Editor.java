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
import data.Tile;
import data.TileGrid;
import object.GunganEnemy;
import object.Player;
import object.Speeder;

public class Editor {

	private TileGrid grid;

	private UI editorUI, menuUI, controllsUI;
	private Menu editorMainMenu;
	private Image menuBackground, space, tile_grid, menu_background_filter;
	private Handler handler;
	private Game game;
	private int activeLevel, menuY;
	private boolean editor_state, createLevel_state, menu_state;
	private boolean mouseDown;
	
	private Entity selectedEntity;
	private TileType selectedType;
	private Player player;
	private Speeder speeder;
	private CopyOnWriteArrayList<GunganEnemy> enemyList;

	public Editor(Handler handler, Game game) 
	{
		this.handler = handler;
		this.game = game;
		this.activeLevel = 0;
		this.editor_state = false;
		this.createLevel_state = false;;

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
				for(GunganEnemy g : enemyList)
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
			}
			
			// draw tile
			if(Mouse.isButtonDown(0)&& Mouse.getX()-MOVEMENT_X < getRightBoarder() - 300)
			{
				if(selectedType != null)setTile(selectedType);
			}
			
			// Draw non-Tile
			if(!Mouse.isButtonDown(0) && selectedEntity != null &&  Mouse.getX()-MOVEMENT_X < getRightBoarder() - 350)
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
					speeder.setY(HEIGHT - (Mouse.getY() - Mouse.getY()%TILE_SIZE) - MOVEMENT_Y - TILE_SIZE);
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
						MOVEMENT_X = 0;
						MOVEMENT_Y = 0;
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
			editorUI.draw();
			controllsUI.draw();
			if(selectedEntity != null)
				selectedEntity.draw();
			
			if(player != null)
				player.draw();
			
			for(GunganEnemy g : enemyList)
			{
				g.draw();
			}
			if(speeder != null)
				speeder.draw();
			
		}else if(createLevel_state){
			drawQuadImage(space, 0, 0, 2048, 2048);
		}else if(menu_state){
			drawQuadImage(space, 0, 0, 2048, 2048);
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
		if(handler.gunganList.size() != 0)
			enemyList = handler.gunganList;
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
					
					for(GunganEnemy g : enemyList)
					{
						graphics.setPaint(new Color(0,255,120));
						graphics.fillRect((int)g.getX() / TILE_SIZE, (int)g.getY() / TILE_SIZE, 1, 1);
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
		handler.gunganList = enemyList;
		handler.player = player;
		handler.speeder = speeder;
		handler.at_st_walker = null;
		handler.setCurrentEntity(player);
		
		game.setCamera(new Camera(handler.getCurrentEntity()));
		game.setBackgroundHandler(new BackgroundHandler(handler.player));
	}
	
	public void transmitDataFromHandler()
	{
		editor_state = false;
		menu_state = true;
		
		activeLevel = StateManager.CURRENT_LEVEL * -1;
		grid = handler.getMap();
		enemyList = handler.gunganList;
		player = handler.player;
		speeder = handler.speeder;
		
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
		menuUI.addButton("Return", "intro/Return", (int)getLeftBoarder() + 10, (int)getTopBoarder() + 10, 128, 64);
		
		editorUI = new UI();
		editorUI.createMenu("editorUI", (int)getRightBoarder()-256, 128, 256, HEIGHT, 4, 4); // Menu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight)
		editorMainMenu = editorUI.getMenu("editorUI");

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
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		editorMainMenu.quickAdd("Blank", "tiles/Blank", 64, 64);
		
		editorMainMenu.quickAdd("Speeder", "player/endor_speeder_right", 256, 93);
		
		controllsUI = new UI();
		controllsUI.addButton("play", "editor/button_play", (int)getRightBoarder()-280, 0, 128, 128);
		controllsUI.addButton("save", "editor/button_save", (int)getRightBoarder()-140, 0, 128, 128);
		controllsUI.addButton("Return", "intro/Return", (int)getLeftBoarder() + 10, (int)getTopBoarder() + 10, 128, 64);
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
					this.editor_state = false;
					this.menu_state = true;
				}
				
				if(editorMainMenu.isButtonClicked("Grass_Flat")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Flat);
					selectedType = TileType.Grass_Flat;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Left")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Left);
					selectedType = TileType.Grass_Left;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Right")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Right);
					selectedType = TileType.Grass_Right;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Round")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Round);
					selectedType = TileType.Grass_Round;
					mouseDown = true;
					return;
				}
				
				
				if(editorMainMenu.isButtonClicked("Grass_Flat_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Flat_Half);
					selectedType = TileType.Grass_Flat_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Left_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Left_Half);
					selectedType = TileType.Grass_Left_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Right_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Right_Half);
					selectedType = TileType.Grass_Right_Half;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Grass_Round_Half")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Grass_Round_Half);
					selectedType = TileType.Grass_Round_Half;
					mouseDown = true;
					return;
				}
				
				
				if(editorMainMenu.isButtonClicked("Dirt_Basic")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Dirt_Basic);
					selectedType = TileType.Dirt_Basic;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Ramp_start")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Ramp_Start);
					selectedType = TileType.Ramp_Start;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Ramp_end")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Ramp_End);
					selectedType = TileType.Ramp_End;
					mouseDown = true;
					return;
				}
				if(editorMainMenu.isButtonClicked("Rock_Basic_0")){
					selectedEntity = new Tile(Mouse.getX(), HEIGHT - Mouse.getY(), TILE_SIZE, TILE_SIZE, TileType.Rock_Basic);
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
			}
		}
	}
}
