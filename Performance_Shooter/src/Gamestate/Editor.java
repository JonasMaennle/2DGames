package Gamestate;

import static helpers.Graphics.*;
import static helpers.Leveler.*;
import static helpers.Setup.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;

import Enity.TileType;
import UI.UI;
import UI.UI.Menu;
import data.Handler;
import data.TileGrid;

public class Editor {

	private TileGrid grid;
	private int index;
	private TileType[] types;
	private UI editorUI;
	private Menu tilePickerMenu;
	private Image menuBackground, gitter;
	private Handler handler;

	public Editor(Handler handler) 
	{
		this.handler = handler;

		this.grid = loadMap(this.handler, 0);//new TileGrid();

		
		this.index = 0;
		this.types = new TileType[4];
		this.types[0] = TileType.Grass_Flat;
		this.types[1] = TileType.Dirt_Basic;
		this.types[2] = TileType.Rock_Basic;
		this.types[3] = TileType.Default;
		this.menuBackground = quickLoaderImage("background/menu_background");
		this.gitter = quickLoaderImage("background/menu_background_gitter");
		createLevelFile(600, 100);
		setupUI();
	}
	
	private void setupUI()
	{
		editorUI = new UI();
		editorUI.createMenu("TilePicker", WIDTH, 100, 192, HEIGHT, 2, 0);
		tilePickerMenu = editorUI.getMenu("TilePicker");
		tilePickerMenu.quickAdd("Grass", "tiles/Grass_Flat");
		tilePickerMenu.quickAdd("Dirt", "tiles/Dirt_Basic");
		tilePickerMenu.quickAdd("Rock", "tiles/Rock_Basic_0");
		tilePickerMenu.quickAdd("Default", "tiles/Filler");
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

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			AL.destroy();
			Display.destroy();
			System.exit(0);
		}
		
		// Handle Keyboad Input
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
				moveIndex();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState())
			{
				saveMap("mapSmall", grid);
			}
		}
	}
	
	private void draw()
	{
		drawQuadImage(menuBackground, 0, 0, 2048, 2048);
		drawQuadImage(gitter, 0, 0, 2048, 2048);
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
	
	private void createLevelFile(int width, int height)
	{
		try {
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = bi.createGraphics();
			
			graphics.setPaint(new Color(255, 255, 255));
			graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
			
			ImageIO.write(bi, "PNG", new File("res/maps/map_0.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
