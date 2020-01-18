package framework.gamestate;

import org.lwjgl.input.Keyboard;
import framework.core.BackgroundHandler;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.helper.Collection;

public class Deathscreen extends GameScreen{
	
	public Deathscreen(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);
	}
	
	public void update(){
		super.update();
		
		while(Keyboard.next()){
			if(Keyboard.getEventKey() == Keyboard.KEY_R){
				// Reset Game
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_M){
				Collection.MOVEMENT_X = 0;
				Collection.MOVEMENT_Y = 0;
				StateManager.gameState = GameState.MAINMENU;
			}
		}
	}
	
	public void render(){
		super.render();
	}
}
