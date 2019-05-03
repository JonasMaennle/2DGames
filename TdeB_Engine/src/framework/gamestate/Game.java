package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.Handler;
import framework.ui.HeadUpDisplay;

public class Game {
	
	private Handler handler;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private HeadUpDisplay hud;
	
	public Game(Handler handler){
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();	
		this.hud = new HeadUpDisplay(handler);
	}
	
	public void update(){
		
		camera.update();
		handler.update();
		
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		hud.draw();
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
