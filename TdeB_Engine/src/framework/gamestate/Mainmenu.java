package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.ui.Button;
import framework.ui.UI;


public class Mainmenu {
	
	private Image background;
	private UI ui;
	private int buttonWidth, buttonHeight, buttonY, buttonYOffset;
	private Credits credits;
	
	public Mainmenu() {
		this.ui = new UI();
		this.background = quickLoaderImage("hud/menu_screenshot");
		
		this.buttonWidth = 256;
		this.buttonHeight = 64;
		
		this.buttonY = 160;
		this.buttonYOffset = 128;
		
		ui.addButton("Start", "hud/button_start", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		buttonY += buttonYOffset;
		ui.addButton("Credits", "hud/button_credits", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		buttonY += buttonYOffset;
		ui.addButton("Exit", "hud/button_exit", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
	}
	
	public void update() {
		
		// Keyboard input
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}

		if(credits == null) {
			// check if button is clicked
			for(Button b : ui.getButtonList()) {
				// Start
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Start")) {
					StateManager.gameState = GameState.LOADING;
				}
				// Credits
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Credits")) {
					credits = new Credits();
				}
				// Exit
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Exit")) {
					AL.destroy();
					Display.destroy();
					System.exit(0);
				}
			}
		}else {
			credits.update();
		}

	}
	
	public void render() {
		if(credits == null) {
			drawQuadImageStatic(background, 0, 0, 1024, 1024);
			ui.draw();
		}else {
			credits.draw();
		}
	}
}
