package framework.gamestate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.ui.HeadUpDisplay;
import static framework.helper.Collection.*;

public class Game {
	
	private Handler handler;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private HeadUpDisplay hud;
	private long timer1, timer2;
	private int lightReductionTime;
	
	public Game(Handler handler){
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();	
		this.hud = new HeadUpDisplay(handler);
		
		this.timer1 = System.currentTimeMillis();
		this.lightReductionTime = 500; // time in ms
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
		
		// Player dead?
		if(PLAYER_HP <= 0){
			StateManager.gameState = GameState.DEATHSCREEN;
		}
		
		checkBattery();
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		hud.draw();
	}
	
	// check and update battery -> fog of war
	private void checkBattery(){
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > lightReductionTime && handler.getPlayer().isHasHelmet()){
			BATTERY_CHARGE -= 1;
			if(BATTERY_CHARGE <= 0)
				BATTERY_CHARGE = 0;
			
			// set fog of war
			if(BATTERY_CHARGE > 0)
				handler.setFogFilter((int)(BATTERY_CHARGE / 12) + 1);
			else
				handler.setFogFilter(0);
			
			timer2 = timer1;
		}
		handler.getPlayer().setHelmetBightness((int)((12 / BATTERY_CHARGE) * 100) - 5);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
