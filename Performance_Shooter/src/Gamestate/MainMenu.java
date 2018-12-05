package Gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import Gamestate.StateManager.GameState;
import UI.Button;
import UI.UI;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.util.ArrayList;

public class MainMenu {
	
	private Image text_big, background_space;
	private UI menuUI;
	private int startX, startY;
	private float scaleWidth, scaleHeight;
	private float speed;
	private float button_moveY;
	private long timer1, timer2;
	private ArrayList<Button> buttonList;
	private Music mainTheme;
	private boolean playMusic, enableMouse, showAtStart;
	private int selectedButton;
	private boolean down, up;
	private StateManager statemanager;
	
	public MainMenu(StateManager statemanager)
	{
		this.statemanager = statemanager;
		this.text_big = quickLoaderImage("intro/Text_big");
		this.background_space = quickLoaderImage("intro/Background_Space");

		menuUI = new UI();
		menuUI.addButton("Start", "intro/Start_Button", WIDTH / 2 - 100, (int)HEIGHT, 350, 80);
		menuUI.addButton("Editor", "intro/Editor_Button", WIDTH / 2 - 100, (int) HEIGHT, 350, 80);
		menuUI.addButton("Exit", "intro/Exit_Button", WIDTH / 2 - 100, (int) HEIGHT, 350, 80);
		
		scaleWidth = text_big.getWidth();
		scaleHeight = text_big.getHeight();	
		
		this.speed = 1f;
		this.playMusic = true;
		this.enableMouse = true;
		this.showAtStart = true;
		this.down = false;
		this.up = false;
		
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.buttonList = menuUI.getButtonList();
		this.button_moveY = HEIGHT + 10;
		this.selectedButton = -1;
		try {
			this.mainTheme = new Music("sound/MainThemeShort.wav");
		} catch (SlickException e) {e.printStackTrace();}
		
		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(200, 200);
	}
	
	public void update()
	{
		statemanager.setRed_color(255);
		statemanager.setGreen_color(255);
		statemanager.setBlue_color(255);
		statemanager.setLightRadius(50);
		// Exit Game
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			AL.destroy();
			Display.destroy();
			System.exit(0);
		}
		
		// Skip Intro
		if(Keyboard.isKeyDown(Keyboard.KEY_I)) 
		{
			enterMainMenu();
		}
		// Draw Space
		drawQuadImageStatic(background_space, 0, 0, 2048, 2048);
		
		timer1 = System.currentTimeMillis();
		// Wait one Sec at begin
		if(timer1 - timer2 > 1300 && showAtStart)
		{
			// Play main theme
			if(playMusic)
			{
				mainTheme.play();
				playMusic = false;
				
				Mouse.setGrabbed(true);
				Mouse.setCursorPosition(200, 200);
			}
			//timer2 = timer2;
			scaleWidth -= 1.0f * speed;
			scaleHeight -= 1.0f * speed;
			speed += 0.02f;
			
			startX = (int) ((WIDTH - scaleWidth) / 2);
			startY = (int) ((HEIGHT - scaleHeight) / 2);
			
			// Draw STAR WARS text
			if(scaleWidth > 40 && scaleHeight > 10)
				drawQuadImage(text_big, startX, startY, scaleWidth, scaleHeight);
		}
		
		if(scaleWidth <= 40 || scaleHeight <= 10)
		{
			showAtStart = false;
			menuUI.draw();
			updateButton();
			
			for(Button b : buttonList)
			{
				if(b.getName().equals("Start"))
				{
					b.setY((int)button_moveY);
				}
				if(b.getName().equals("Editor"))
				{
					b.setY((int)button_moveY + 150);
				}
				if(b.getName().equals("Exit"))
				{
					b.setY((int)button_moveY + 300);
				}
			}
			
			if(button_moveY > (HEIGHT * 0.35f))
			{
				button_moveY -= 1;
			}else{
				if(enableMouse)
				{
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(WIDTH/2, (int)(HEIGHT * 0.66f));
					enableMouse = false;
					selectedButton = 0;
				}
			}
			
			// Select button via keyboard
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !down)
			{
				down = true;
				selectedButton++;
				if(selectedButton >= 2)
					selectedButton = 2;
				return;
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_DOWN) && down)
			{
				down = false;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !up)
			{
				up = true;
				selectedButton--;
				if(selectedButton <= 0)
					selectedButton = 0;
				return;
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_UP) && up)
			{
				up = false;
			}
			
			// Enter
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN))
			{
				if(selectedButton == 0)
				{
					StateManager.CURRENT_LEVEL = 0;
					StateManager.setState(GameState.LOADING);
				}
				if(selectedButton == 1)
				{
					StateManager.setState(GameState.EDITOR);
				}
				if(selectedButton == 2)
				{
					AL.destroy();
					Display.destroy();
					System.exit(0);
				}
			}	

			// Mouse hover over button
			for(Button b : buttonList)
			{
				if(b.getName().equals("Start"))
				{
					//System.out.println(b.getX() + " " + b.getY() + " " + (HEIGHT - Mouse.getY() - MOVEMENT_Y));
					if(checkCollision(b.getX(), b.getY(), b.getWidth(), b.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY()-MOVEMENT_Y, 2, 2))
					{
						selectedButton = 0;
					}
				}
				if(b.getName().equals("Editor"))
				{
					if(checkCollision(b.getX(), b.getY(), b.getWidth(), b.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY()-MOVEMENT_Y, 2, 2))
					{
						selectedButton = 1;
					}
				}
				if(b.getName().equals("Exit"))
				{
					if(checkCollision(b.getX(), b.getY(), b.getWidth(), b.getHeight(), Mouse.getX()-MOVEMENT_X, HEIGHT - Mouse.getY()-MOVEMENT_Y, 2, 2))
					{
						selectedButton = 2;
					}
				}
			}
			//System.out.println(selectedButton);
			
			// Update button image
			switch (selectedButton) {
			case 0:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button_Selected"));
					if(b.getName().equals("Editor"))
						b.setImage(quickLoaderImage("intro/Editor_Button"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button"));
				}
				break;
			case 1:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button"));
					if(b.getName().equals("Editor"))
						b.setImage(quickLoaderImage("intro/Editor_Button_Selected"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button"));
				}
				break;
			case 2:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button"));
					if(b.getName().equals("Editor"))
						b.setImage(quickLoaderImage("intro/Editor_Button"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button_Selected"));
				}
				break;
			default:
				break;
			}
		}
	}
	
	// Check if a button is clicked by the user, if so do an action
	private void updateButton()
	{
		if(Mouse.next())
		{
			if(menuUI.isButtonClicked("Start"))
			{
				StateManager.CURRENT_LEVEL = 0;
				StateManager.setState(GameState.LOADING);
			}
				
			if(menuUI.isButtonClicked("Editor"))
			{
				StateManager.gameState = GameState.EDITOR;
			}
				
			if(menuUI.isButtonClicked("Exit"))
			{
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public void enterMainMenu()
	{
		StateManager.CURRENT_LEVEL = 0;
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
		StateManager.ENVIRONMENT_SETTING = "";
		Mouse.setGrabbed(true);
		Mouse.setCursorPosition((int)getLeftBorder() + WIDTH/2, (int)(getTopBorder() + (HEIGHT * 0.66f)));
		enableMouse = false;
		selectedButton = 0;
		
		showAtStart = false;
		button_moveY = (HEIGHT * 0.35f) + 1;
		scaleWidth = 0;
		scaleHeight = 0;
		mainTheme.stop();
		
		lights.clear();
	}
}
