package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.core.BackgroundHandler;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.helper.Collection;
import framework.path.PathfindingThread;

import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

public class Deathscreen {
	
	private Handler handler;
	private BackgroundHandler backgroundHandler;
	private StateManager manager;
	private Image image, image_arena;
	
	public Deathscreen(Handler handler, StateManager manager){
		this.handler = handler;
		this.manager = manager;
		this.backgroundHandler = new BackgroundHandler();	
		this.image = quickLoaderImage("hud/text_died");
		this.image_arena = quickLoaderImage("hud/text_died_arena");
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
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		
		if(StateManager.gameMode == GameState.GAME) {
			drawQuadImageStatic(image, (WIDTH - image.getWidth()) / 2, 200, image.getWidth(), image.getHeight());
		}else {
			drawQuadImageStatic(image_arena, (WIDTH - image.getWidth()) / 2, 200, image.getWidth(), image.getHeight());
			Collection.drawString(WIDTH/2 - 32, HEIGHT/2 + 20, "" + (Collection.ARENA_CURRENT_WAVE - 1), new Color(200, 0, 0), 30);
		}
	}
}
