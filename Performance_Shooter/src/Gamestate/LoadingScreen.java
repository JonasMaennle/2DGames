package Gamestate;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;

import Gamestate.StateManager.GameState;

import static helpers.Artist.*;

public class LoadingScreen {

	private Image background, laser_hand, laser_saber;
	private boolean loadingDone;
	private StateManager statemanager;
	private float saberLength;
	
	public LoadingScreen(StateManager statemanager)
	{
		this.background = quickLoaderImage("intro/Background_Space");
		this.laser_hand = quickLoaderImage("intro/Laser_Loading_Hand");
		this.laser_saber = quickLoaderImage("intro/Laser_Loading_Saber");
		this.statemanager = statemanager;
		this.loadingDone = false;
		this.saberLength = 0;
	}
	
	public void update()
	{
		Mouse.setGrabbed(true);
		if(!loadingDone)
		{
			loadingDone = true;
			statemanager.loadLevel();
		}
		if(saberLength > (WIDTH/3))
		{
			loadingDone = false;
			StateManager.gameState = GameState.GAME;
			Mouse.setGrabbed(false);
			Mouse.setCursorPosition((int) (Display.getX() + (Display.getWidth()* 0.75f)), (int)getTopBoarder() + (HEIGHT / 2));
			saberLength = 0;
		}
		saberLength += 100; // += 2 normal value <-
		draw();
	}
	
	public void draw()
	{
		drawQuadImageStatic(background, 0, 0, 2048, 2048);
		drawQuadImageStatic(laser_hand, WIDTH/4, HEIGHT/2 - 36, 150, 75);
		drawQuadImageStatic(laser_saber, WIDTH/4 + 150, HEIGHT/2 - 36, saberLength, 75);
	}
	
	public boolean getLoadingDone()
	{
		return loadingDone;
	}
}
