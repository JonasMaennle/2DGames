package framework.gamestate;

import static framework.helper.Collection.BATTERY_CHARGE;
import static framework.helper.Collection.PLAYER_HP;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.core.WaveManager;
import framework.ui.HeadUpDisplay;

public class Arena {
	
	private Handler handler;
	private BackgroundHandler backgroundHandler;
	private Camera camera;
	private HeadUpDisplay hud;
	private WaveManager waveManager;
	
	private int lightReductionTime;
	private long timer1, timer2;
	
	public Arena(Handler handler, StateManager manager) {
		this.handler = handler;
		this.backgroundHandler = new BackgroundHandler();
		this.camera = new Camera(handler.getCurrentEntity());
		this.hud = new HeadUpDisplay(handler, 16, manager);
		
		this.timer1 = System.currentTimeMillis();
		this.lightReductionTime = 500; // time in ms
		this.waveManager = new WaveManager(handler, manager);
	}
	
	public void update() {
		camera.update();
		handler.update();
		hud.update();
		waveManager.update();
		
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

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public WaveManager getWaveManager() {
		return waveManager;
	}

	public void setWaveManager(WaveManager waveManager) {
		this.waveManager = waveManager;
	}
}
