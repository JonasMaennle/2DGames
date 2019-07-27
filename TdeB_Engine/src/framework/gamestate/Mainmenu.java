package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;

import static framework.helper.Collection.*;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.helper.Collection;
import framework.ui.Button;
import framework.ui.UI;


public class Mainmenu {
	
	private Image background;
	private UI ui;
	private int buttonWidth, buttonHeight, buttonY, buttonYOffset;
	private Credits credits;
	private StateManager manager;
	private boolean resetLoading;
	
	public Mainmenu(StateManager manager) {
		this.ui = new UI();
		this.background = quickLoaderImage("hud/menu_screenshot");
		this.manager = manager;
		this.resetLoading = true;
		
		this.buttonWidth = 256;
		this.buttonHeight = 64;
		
		this.buttonY = 96;
		this.buttonYOffset = 128;
		
		ui.addButton("Start", "hud/button_start", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		buttonY += buttonYOffset;
		ui.addButton("Arena", "hud/button_arena", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		buttonY += buttonYOffset;
		ui.addButton("Credits", "hud/button_credits", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		buttonY += buttonYOffset;
		ui.addButton("Exit", "hud/button_exit", WIDTH/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
	}
	
	public void update() {
		if(resetLoading){
			manager.setLoadingscreen(new Loadingscreen(manager, manager.getHandler()));
			resetLoading = false;
		}

		// Keyboard input
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
		
		mouseHoverOverButton();

		if(credits == null) {
			// check if button is clicked
			for(Button b : ui.getButtonList()) {
				// Start
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Start")) {
					resetLoading = true;
					Collection.resetPlayerStats();
					StateManager.CURRENT_LEVEL = 0;
					StateManager.gameState = GameState.LOADING;
				}
				// Arena
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Arena")) {
					manager.getArena().getWaveManager().reset();
					Collection.resetPlayerStats();
					StateManager.CURRENT_LEVEL = 0;
					StateManager.gameState = GameState.ARENA;
				}
				// Credits
				if(ui.isButtonClicked(b.getName()) && b.getName().equals("Credits")) {
					credits = new Credits(manager);
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
	
	private void mouseHoverOverButton() {
		// create Rect for Mouse coords
		Rectangle mouse = new Rectangle((int)(Mouse.getX() - MOVEMENT_X), (int)(HEIGHT - Mouse.getY() - MOVEMENT_Y), 1, 1);
		
		for(Button b : ui.getButtonList()) {		
			if(b.getBounds().intersects(mouse)) {
				switch (b.getName()) {
				case "Start":
					b.setImage(quickLoaderImage("hud/button_start_selected"));
					break;
				case "Credits":
					b.setImage(quickLoaderImage("hud/button_credits_selected"));
					break;
				case "Exit":
					b.setImage(quickLoaderImage("hud/button_exit_selected"));
					break;
				case "Arena":
					b.setImage(quickLoaderImage("hud/button_arena_selected"));
					break;
				default:
					break;
				}
				
			}else {
				switch (b.getName()) {
				case "Start":
					b.setImage(quickLoaderImage("hud/button_start"));
					break;
				case "Credits":
					b.setImage(quickLoaderImage("hud/button_credits"));
					break;
				case "Exit":
					b.setImage(quickLoaderImage("hud/button_exit"));
					break;
				case "Arena":
					b.setImage(quickLoaderImage("hud/button_arena"));
					break;
				default:
					break;
				}
			}
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
