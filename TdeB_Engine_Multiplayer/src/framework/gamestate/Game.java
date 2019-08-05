package framework.gamestate;

import framework.core.Handler;
import framework.core.Scene2D;
import framework.core.StateManager;
import static framework.helper.Collection.*;

public class Game extends Scene2D{
	
	private boolean showLevelMessage;
	
	public Game(Handler handler, StateManager manager){
		super(handler, manager);
		
		this.showLevelMessage = true;
	}
	
	public void update(){
		super.update();
		
		// add message to screen
		if(showLevelMessage){
			handler.getInfo_manager().createNewMessage(WIDTH/2 - MOVEMENT_X - 64, HEIGHT/2 - 96 - MOVEMENT_Y, "Level " + StateManager.CURRENT_LEVEL, new org.newdawn.slick.Color(255,255,255), 32, 3000);
			showLevelMessage = false;
		}
	}
	
	public void render(){
		super.render();
	}

	public void setShowLevelMessage(boolean b) {
		this.showLevelMessage = b;
	}
}
