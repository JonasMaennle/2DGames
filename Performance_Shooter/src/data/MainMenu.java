package data;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import UI.UI;
import helpers.StateManager;
import helpers.StateManager.GameState;

import static helpers.Artist.*;

public class MainMenu {
	
	private Texture background;
	private UI menuUI;
	
	public MainMenu()
	{
		background = quickLoad("mainmenu");
		menuUI = new UI();
		menuUI.addButton("Play", "playButton", WIDTH / 2 - 128, (int) (HEIGHT * 0.45f));
		menuUI.addButton("Editor", "editorButton", WIDTH / 2 - 128, (int) (HEIGHT * 0.55f));
		menuUI.addButton("Quit", "quitButton", WIDTH / 2 - 128, (int) (HEIGHT * 0.65f));
	}
	
	public void update()
	{
		drawQuadTex(background, 0, 0, (int)(WIDTH * 1.6), 1024);
		menuUI.draw();
		updateButton();
	}
	
	// Check if a button is clicked by the user, if so do an action
	private void updateButton()
	{
		if(Mouse.isButtonDown(0))
		{
			if(menuUI.isButtonClicked("Play"))
				StateManager.setState(GameState.GAME);
			
			if(menuUI.isButtonClicked("Quit"))
				System.exit(0);
		}
	}
}
