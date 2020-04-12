package framework.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import static framework.helper.Collection.*;

public abstract class GameScreen{
	
	protected StateManager stateManager;
	protected Handler handler;
	private BackgroundHandler backgroundHandler;
	private float camera_speed;
	private float cam_velX, cam_velY;
	
	public GameScreen(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		this.stateManager = stateManager;
		this.handler = handler;
		this.backgroundHandler = backgroundHandler;
		this.camera_speed = 6f;
		this.cam_velX = 0;
		this.cam_velY = 0;
	}
	
	public void update() {	
		int dWheel = Mouse.getDWheel();
		
	    if (dWheel < 0) 
	        zoomIn(-1f);
	    else if (dWheel > 0)
	        zoomIn(1f);
	    
	   cam_velX = 0;
	   cam_velY = 0;
		
		// move camera
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			cam_velX = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cam_velX = -1;

		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			cam_velY = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			cam_velY = -1;
		}
		// camera_speed = 6 - SCALE;
	
		MOVEMENT_X += cam_velX * camera_speed;
		MOVEMENT_Y += cam_velY * camera_speed;
		
		
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
	
	private void zoomIn(float amount) {
		if(amount > 0 && SCALE < 2) {
			SCALE+= amount;
			MOVEMENT_X /= 2;
			MOVEMENT_Y *= 2;
		}	
		else if(amount < 0 && SCALE > 1){
			SCALE+=amount;
			MOVEMENT_X *= 2;
			MOVEMENT_Y /= 2;
		}
	}
}
