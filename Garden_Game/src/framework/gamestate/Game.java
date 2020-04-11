
package framework.gamestate;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.ui.HeadUpDisplay;
import object.player.WalkTo;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import static framework.helper.Collection.*;

public class Game extends GameScreen{

	private Camera camera;
	private HeadUpDisplay hud;
	private boolean mouseReleased;
	
	public Game(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);
		this.camera = new Camera();
		this.hud = new HeadUpDisplay(handler, 40, stateManager);
		this.mouseReleased = true;
	}
	
	public void update(){
		super.update();

		checkForUserInput();

		camera.update();
		handler.update();
		hud.update();
	}
	
	public void render(){
		super.render();
		hud.draw();
	}

	private void checkForUserInput(){
		if(Mouse.isButtonDown(1) && mouseReleased){
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			Vector2f mouseTile = convertMouseCoordsToIsometricGrid(mouseX, mouseY);
			if(handler.getGraph().getNode((int)mouseTile.x, (int)mouseTile.y) != null)
				handler.getPlayer().setCurrentTask(new WalkTo(mouseX, mouseY, handler.getPlayer()));
			mouseReleased = false;
		}
		if(!Mouse.isButtonDown(1)){
			mouseReleased = true;
		}
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
