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

import static helpers.Artist.*;

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
	
	public MainMenu()
	{
		this.text_big = quickLoaderImage("intro/Text_big");
		this.background_space = quickLoaderImage("intro/Background_Space");

		menuUI = new UI();
		menuUI.addButton("Start", "intro/Start_Button2", WIDTH / 2 - 175, (int)HEIGHT, 600, 150);
		menuUI.addButton("Settings", "intro/Settings_Button2", WIDTH / 2 - 175, (int) HEIGHT, 600, 150);
		menuUI.addButton("Exit", "intro/Exit_Button2", WIDTH / 2 - 175, (int) HEIGHT, 600, 150);
		
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
			this.mainTheme = new Music("sound/MainTheme.wav");
		} catch (SlickException e) {e.printStackTrace();}
		
		Mouse.setGrabbed(true);
	}
	
	public void update()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			AL.destroy();
			Display.destroy();
			System.exit(0);
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
				if(b.getName().equals("Settings"))
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
			// Update button image
			switch (selectedButton) {
			case 0:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button2_Selected"));
					if(b.getName().equals("Settings"))
						b.setImage(quickLoaderImage("intro/Settings_Button2"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button2"));
				}
				break;
			case 1:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button2"));
					if(b.getName().equals("Settings"))
						b.setImage(quickLoaderImage("intro/Settings_Button2_Selected"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button2"));
				}
				break;
			case 2:
				for(Button b : buttonList){
					if(b.getName().equals("Start"))
						b.setImage(quickLoaderImage("intro/Start_Button2"));
					if(b.getName().equals("Settings"))
						b.setImage(quickLoaderImage("intro/Settings_Button2"));
					if(b.getName().equals("Exit"))
						b.setImage(quickLoaderImage("intro/Exit_Button2_Selected"));
				}
				break;
			default:
				break;
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
				if(b.getName().equals("Settings"))
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
			
			// Enter
			if(Keyboard.isKeyDown(Keyboard.KEY_RETURN))
			{
				if(selectedButton == 0)
				{
					StateManager.setState(GameState.LOADING);
				}
				if(selectedButton == 2)
				{
					AL.destroy();
					Display.destroy();
					System.exit(0);
				}
			}	
			//System.out.println(selectedButton);
		}
	}
	
	// Check if a button is clicked by the user, if so do an action
	private void updateButton()
	{
		if(Mouse.isButtonDown(0))
		{
			if(menuUI.isButtonClicked("Start"))
			{
				StateManager.setState(GameState.LOADING);
			}
			
			if(menuUI.isButtonClicked("Settings"))
			{
				System.out.println("Settings");
			}
			
			if(menuUI.isButtonClicked("Exit"))
			{
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
}
