
package framework.gamestate;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.ui.HeadUpDisplay;
import object.player.playertask.WalkTo;
import object.trees.Tree;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import javax.swing.tree.TreeCellRenderer;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;

public class Game extends GameScreen{

	private Camera camera;
	private HeadUpDisplay hud;
	private boolean mouseReleased;
	private Image cursor, cursorAxe, currentCursor;
	
	public Game(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);
		this.camera = new Camera();
		this.hud = new HeadUpDisplay(handler, 40, stateManager);
		this.mouseReleased = true;
		this.cursor = quickLoaderImage("hud/cursor");
		this.cursorAxe = quickLoaderImage("hud/axe");
		this.currentCursor = cursor;
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

		drawQuadImageStatic(currentCursor, Mouse.getX() - 12, HEIGHT - Mouse.getY() - 12, currentCursor.getWidth(), currentCursor.getHeight());
	}

	private void checkForUserInput(){
		Tree tree = checkForCuttingTree();

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

	private Tree checkForCuttingTree(){
		Tree tree = null;
		int x = (int) (Mouse.getX() - (MOVEMENT_X * SCALE)  );
		int y = (int) (HEIGHT - Mouse.getY() - (MOVEMENT_Y * SCALE));
		this.currentCursor = this.cursor;

		for(Tree t : handler.getTreeList()){
			if(t.getBounds().contains(x, y)){
				// mouse over tree trunk
				this.currentCursor = this.cursorAxe;
				tree = t;
			}
		}
		return tree;
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
