
package framework.gamestate;

import framework.core.BackgroundHandler;
import static framework.helper.Collection.*;
import framework.core.Camera;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.ui.HeadUpDisplay;
import object.MiniMap;

public class Game extends GameScreen{

	private Camera camera;
	private HeadUpDisplay hud;
	private MiniMap miniMap;
	
	public Game(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);
		this.camera = new Camera(handler.getPlayer());
		this.hud = new HeadUpDisplay(handler, 40, stateManager);
		this.miniMap = new MiniMap(WIDTH - 256 - 8, 8, 256, 256, 20, handler);
	}
	
	public void update(){
		super.update();
		
		camera.update();
		handler.update();
		hud.update();
		miniMap.update();
	}
	
	public void render(){
		super.render();
		hud.draw();
		miniMap.draw();
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
}
