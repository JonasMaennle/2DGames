package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.arena.ScoreScreen;
import framework.core.BackgroundHandler;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.helper.Collection;
import framework.path.PathfindingThread;
import framework.ui.Button;
import framework.ui.UI;

import static framework.helper.Graphics.*;

import java.awt.Rectangle;

import static framework.helper.Collection.*;

public class Deathscreen {
	
	private Handler handler;
	private BackgroundHandler backgroundHandler;
	private StateManager manager;
	private Image image, image_arena;
	private UI ui, ui_story;
	
	public Deathscreen(Handler handler, StateManager manager){
		this.handler = handler;
		this.manager = manager;
		this.ui = new UI();
		this.ui_story = new UI();
		this.backgroundHandler = new BackgroundHandler();	
		this.image = quickLoaderImage("hud/text_died");
		this.image_arena = quickLoaderImage("hud/text_died_arena");
		this.ui.addButton("Score", "hud/button_score", (WIDTH / 2) + 32, HEIGHT - 85, 256, 64);
		this.ui.addButton("Back", "hud/button_menu", (WIDTH / 2) - 256 - 32, HEIGHT - 85, 256, 64);
		
		this.ui_story.addButton("Back", "hud/button_menu", (WIDTH / 2) - 128, HEIGHT - 85, 256, 64);
	}
	
	public void update(){
		
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
			
			if(Keyboard.getEventKey() == Keyboard.KEY_R){
				resetGame();
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_M){
				Collection.MOVEMENT_X = 0;
				Collection.MOVEMENT_Y = 0;
				StateManager.gameState = GameState.MAINMENU;
			}
		}
		
		mouseHoverOverButton();
		
		if(StateManager.gameMode == GameState.ARENA) {
			
			if(ui.isButtonClicked("Score")) {
				manager.getScoreScreen().reset();
				manager.setScoreScreen(new ScoreScreen(handler));
				StateManager.gameState = GameState.SCOREBOARD;
			}
			if(ui.isButtonClicked("Back")) {
				manager.getScoreScreen().reset();
				StateManager.gameState = GameState.MAINMENU;
				return;
			}
		}else {
			if(ui_story.isButtonClicked("Back")) {
				Collection.MOVEMENT_X = 0;
				Collection.MOVEMENT_Y = 0;
				StateManager.gameState = GameState.MAINMENU;
				return;
			}
		}
	}
	
	public void resetGame(){
		handler.setFogFilter(0);
		PathfindingThread.running = false;
		Collection.resetPlayerStats();
		
		lights.clear();
		manager.getGame().setShowLevelMessage(true);
		manager.getGame().getHud().resetColors();
		StateManager.CURRENT_LEVEL = 0;
		
		if(StateManager.gameMode == GameState.GAME) {
			StateManager.gameState = GameState.GAME;
		}else {
			manager.getArena().getWaveManager().reset();
			StateManager.gameState = GameState.ARENA;
		}
	}
	
	private void mouseHoverOverButton() {
		// create Rect for Mouse coords
		Rectangle mouse = new Rectangle((int)(Mouse.getX()), (int)(HEIGHT - Mouse.getY()), 1, 1);
		
		for(Button b : ui.getButtonList()) {		
			if(b.getBounds().intersects(mouse)) {
				switch (b.getName()) {
				case "Back":
					b.setImage(quickLoaderImage("hud/button_menu_selected"));
					break;
				case "Score":
					b.setImage(quickLoaderImage("hud/button_score_selected"));
					break;
				default:
					break;
				}
				
			}else {
				switch (b.getName()) {
				case "Back":
					b.setImage(quickLoaderImage("hud/button_menu"));
					break;
				case "Score":
					b.setImage(quickLoaderImage("hud/button_score"));
					break;
				default:
					break;
				}
			}
		}
		
		if(ui_story.getButton("Back").getBounds().intersects(mouse)) {
			ui_story.getButton("Back").setImage(quickLoaderImage("hud/button_menu_selected"));
		}else {
			ui_story.getButton("Back").setImage(quickLoaderImage("hud/button_menu"));
		}
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		
		if(StateManager.gameMode == GameState.GAME) {
			ui_story.draw();
			drawQuadImageStatic(image, (WIDTH - image.getWidth()) / 2, 200, image.getWidth(), image.getHeight());
		}else {
			drawQuadImageStatic(image_arena, (WIDTH - image.getWidth()) / 2, 200, image.getWidth(), image.getHeight());
			Collection.drawString(WIDTH/2 - 32, HEIGHT/2 + 20, "" + (Collection.ARENA_CURRENT_WAVE - 1), new Color(200, 0, 0));
			ui.draw();
		}
	}
}
