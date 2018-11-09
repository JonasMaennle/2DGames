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
		
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.buttonList = menuUI.getButtonList();
		this.button_moveY = HEIGHT + 10;
		
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
			
			if(button_moveY > (HEIGHT * 0.3f))
			{
				button_moveY -= 1;
			}else{
				if(enableMouse)
				{
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(WIDTH/2, HEIGHT/2);
					enableMouse = false;
				}
			}
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
