package framework.gamestate;

import static framework.helper.Collection.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.path.PathfindingThread;
import framework.ui.HeadUpDisplay;

import static framework.helper.Graphics.*;

public class Loadingscreen {
	
	private StateManager manager;
	private HeadUpDisplay hud;
	private Handler handler;
	private long timer1, timer2;
	private boolean setup;
	private int loandingTime;
	
	// loading screen
	private Image image;
	
	public Loadingscreen(StateManager manager, Handler handler){
		this.manager = manager;
		this.handler = handler;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.setup = false;
		this.hud = new HeadUpDisplay(handler, 24, manager);
		this.loandingTime = 500; // loading screen time
		this.image = quickLoaderImage("background/Filter");
	}
	
	public void update(){
		if(!setup){
			reset();
			setup = true;
			timer2 = System.currentTimeMillis();
		}
		
		manager.getGame().update();
		
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > loandingTime){
			StateManager.gameState = GameState.GAME;
			setup = false;
			timer2 = timer1;
			handler.getPlayer().setSpeed(4);
		}	
	}
	
	public void render(){
		// draw loading screen
		drawQuadImageStatic(image, 0, 0, 2048, 2048);
		
		hud.drawString(WIDTH/2 - 64, HEIGHT/2 - 12, "loading...", new Color(255, 255, 255));
	}
	
	private void reset(){
		//handler.setFogFilter(0);
		lights.clear();
		PathfindingThread.running = false;
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.loadLevel();
		handler.setFogFilter(0);
		manager.getGame().setShowLevelMessage(true);
		
		handler.getPlayer().setSpeed(0);
		AMMO_LEFT = 999;
	}
}
