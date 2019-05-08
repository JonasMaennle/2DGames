package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;

import framework.core.BackgroundHandler;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.path.PathfindingThread;

import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

public class Deathscreen {
	
	private Handler handler;
	private BackgroundHandler backgroundHandler;
	private StateManager manager;
	private Image image;
	
	public Deathscreen(Handler handler, StateManager manager){
		this.handler = handler;
		this.manager = manager;
		this.backgroundHandler = new BackgroundHandler();	
		this.image = quickLoaderImage("hud/text_died");
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
		}
	}
	
	public void resetGame(){
		handler.setFogFilter(0);
		PathfindingThread.running = false;
		PLAYER_HP = 96;
		BATTERY_CHARGE = 96;
		AMMO_LEFT = 999;
		
		lights.clear();
		manager.getGame().setShowLevelMessage(true);
		manager.getGame().getHud().resetColors();
		StateManager.CURRENT_LEVEL = 0;
		StateManager.gameState = GameState.GAME;
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		
		drawQuadImageStatic(image, (WIDTH - image.getWidth()) / 2, 200, image.getWidth(), image.getHeight());
	}
}
