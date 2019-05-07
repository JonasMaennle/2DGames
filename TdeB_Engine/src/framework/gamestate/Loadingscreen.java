package framework.gamestate;

import static framework.helper.Collection.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.path.PathfindingThread;
import object.player.Player;
import object.player.Weapon_Flamethrower;

import static framework.helper.Graphics.*;

public class Loadingscreen {
	
	private StateManager manager;
	private Handler handler;
	private long timer1, timer2;
	private boolean setup;
	
	// loading screen
	private Image image, image_player;
	private Weapon_Flamethrower weapon;
	private Player player;
	
	public Loadingscreen(StateManager manager, Handler handler){
		this.manager = manager;
		this.handler = handler;
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.setup = false;
		
		this.image = quickLoaderImage("background/Filter");
		this.image_player = quickLoaderImage("player/player_idle_right");
		this.player = new Player(300 - 12, 150 - 20, handler);
		this.weapon = new Weapon_Flamethrower(32, 16, player, handler);
	}
	
	public void update(){
		if(!setup){
			reset();
			setup = true;
			timer2 = System.currentTimeMillis();
		}

		manager.getGame().update();
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 4000){
			weapon.wipe();
			StateManager.gameState = GameState.GAME;
			setup = false;
			timer2 = timer1;
		}	
	}
	
	public void render(){
		// draw loading screen
		drawQuadImageStatic(image, 0, 0, 2048, 2048);
		drawQuadImageStatic(image_player, WIDTH/2, HEIGHT/2, TILE_SIZE, TILE_SIZE);
		if(Mouse.isButtonDown(0)){
			weapon.shoot();
		}
		
		weapon.update();
		weapon.draw();
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
	}
}
