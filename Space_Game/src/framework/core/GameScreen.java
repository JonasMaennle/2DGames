package framework.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

public abstract class GameScreen{
	
	protected StateManager stateManager;
	protected Handler handler;
	private BackgroundHandler backgroundHandler;
	
	public GameScreen(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		this.stateManager = stateManager;
		this.handler = handler;
		this.backgroundHandler = backgroundHandler;
	}
	
	public void update() {
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
	public void render() {
		backgroundHandler.draw();
		handler.draw();
	}

}
