package data;

import static helpers.Artist.*;
import static helpers.Leveler.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import UI.UI;
import UI.UI.Menu;

public class Editor {

	private TileGrid grid;
	private int index;
	private TileType[] types;
	private UI editorUI;
	private Menu tilePickerMenu;
	private Texture menuBackground;

	public Editor() 
	{
		if(GAME_WIDTH == 1088)
		{
			this.grid = loadMap("mapSmall");//new TileGrid();
		}else if(GAME_WIDTH == 1280){
			this.grid = loadMap("mapLarge");//new TileGrid();
		}else{
			System.out.println("Unknows Game Size!");
			this.grid = loadMap("mapDefault");
		}
		
		this.index = 0;
		this.types = new TileType[4];
		this.types[0] = TileType.Grass;
		this.types[1] = TileType.Dirt;
		this.types[2] = TileType.Water;
		this.types[3] = TileType.Rock;
		this.menuBackground = quickLoad("menu_Background2");
		
		setupUI();
	}
	
	private void setupUI()
	{
		editorUI = new UI();
		editorUI.createMenu("TilePicker", GAME_WIDTH, 100, 192, HEIGHT, 2, 0);
		tilePickerMenu = editorUI.getMenu("TilePicker");
		tilePickerMenu.quickAdd("Grass", "Grass");
		tilePickerMenu.quickAdd("Dirt", "Dirt");
		tilePickerMenu.quickAdd("Water", "Water");
		tilePickerMenu.quickAdd("Rock", "Rock");
	}

	public void update() 
	{
		draw();

		// Handle Mouse Input
		if(Mouse.next())
		{
			boolean mouseClicked = Mouse.isButtonDown(0);
			if(mouseClicked)
			{
				if(tilePickerMenu.isButtonClicked("Grass"))
				{
					index = 0;
				}else if(tilePickerMenu.isButtonClicked("Dirt"))
				{
					index = 1;
				}else if(tilePickerMenu.isButtonClicked("Water"))
				{
					index = 2;
				}else if(tilePickerMenu.isButtonClicked("Rock"))
				{
					index = 3;
				}else{
					setTile();
				}
			}
		}

		// Handle Keyboad Input
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				moveIndex();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
				if(GAME_WIDTH == 1088)
				{
					saveMap("mapSmall", grid);
				}else if(GAME_WIDTH == 1280)
				{
					saveMap("mapLarge", grid);
				}else{
					saveMap("mapDefault", grid);
				}
			}
		}
	}
	
	private void draw()
	{
		drawQuadTex(menuBackground, GAME_WIDTH, 0, 256, 1080);
		grid.draw();
		editorUI.draw();
	}

	private void setTile() 
	{
		grid.setTile((int) Math.floor(Mouse.getX() / TILE_SIZE), (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE),types[index]);
	}
	
	// Allows editor to change which TileType is selected
	private void moveIndex()
	{
		index++;
		if(index > types.length - 1)
			index = 0;
	}
}
