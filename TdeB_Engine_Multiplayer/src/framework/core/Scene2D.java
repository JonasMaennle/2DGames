package framework.core;

import static framework.helper.Collection.BATTERY_CHARGE;
import static framework.helper.Collection.PLAYER_HP;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import framework.core.StateManager.GameState;
import framework.ui.HeadUpDisplay;

public abstract class Scene2D {
	
	protected Handler handler;
	protected BackgroundHandler backgroundHandler;
	protected Camera camera;
	protected HeadUpDisplay hud;
	protected StateManager statemanager;
	
	protected int lightReductionTime;
	protected long timer1, timer2;
	
	// build basic scene
	public Scene2D(Handler handler, StateManager statemanager) {
		this.handler = handler;
		this.statemanager = statemanager;
		
		this.backgroundHandler = new BackgroundHandler();
		this.camera = new Camera(handler.getCurrentEntity());
		this.hud = new HeadUpDisplay(handler, 16, statemanager);
		
		this.timer1 = System.currentTimeMillis();
		this.lightReductionTime = 500; // time between energy reduction in ms
	}
	
	public void update() {
		camera.update();
		handler.update();
		hud.update();
		
		// Player dead?
		if(PLAYER_HP <= 0){
			StateManager.gameState = GameState.DEATHSCREEN;
			return;
		}
		
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}	
		checkBattery();
	}
	
	public void render() {
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

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public BackgroundHandler getBackgroundHandler() {
		return backgroundHandler;
	}

	public void setBackgroundHandler(BackgroundHandler backgroundHandler) {
		this.backgroundHandler = backgroundHandler;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public HeadUpDisplay getHud() {
		return hud;
	}

	public void setHud(HeadUpDisplay hud) {
		this.hud = hud;
	}

	public StateManager getStatemanager() {
		return statemanager;
	}

	public void setStatemanager(StateManager statemanager) {
		this.statemanager = statemanager;
	}
}
