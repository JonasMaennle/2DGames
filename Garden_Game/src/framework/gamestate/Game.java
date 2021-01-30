
package framework.gamestate;

import framework.core.BackgroundHandler;
import framework.core.Camera;
import framework.core.GameScreen;
import framework.core.Handler;
import framework.core.StateManager;
import framework.ui.HeadUpDisplay;
import object.farming.Field;
import object.player.Player;
import object.player.playertask.CutTree;
import object.player.playertask.FarmingRoutine;
import object.player.playertask.MakeWoodRoutine;
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
	private boolean mouseReleased,mouseLeftReleased;
	private Image cursor, cursorAxe, currentCursor, cursorHoe;
	
	public Game(StateManager stateManager, Handler handler, BackgroundHandler backgroundHandler) {
		super(stateManager, handler, backgroundHandler);
		this.camera = new Camera();
		this.hud = new HeadUpDisplay(handler, 40, stateManager);
		this.mouseReleased = true;
		this.mouseLeftReleased = true;
		this.cursor = quickLoaderImage("hud/cursor");
		this.cursorAxe = quickLoaderImage("hud/axe");
		this.cursorHoe = quickLoaderImage("hud/hoe");
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
		this.currentCursor = this.cursor;
		Tree tree = null;
		Field field = null;
		if(handler.getSelectedPlayer() != null) tree = checkForCuttingTree();
		if(handler.getSelectedPlayer() != null) field = checkForField();


		if(Mouse.isButtonDown(1) && mouseReleased && handler.getSelectedPlayer() != null && hud.getObjectToPlaceOnMap() == null){
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			Vector2f mouseTile = convertMouseCoordsToIsometricGrid(mouseX, mouseY);
			Vector2f mouseCoords = convertMouseCoordsToMapCoords(mouseX, mouseY);

			// free tree
			if(handler.getSelectedPlayer().getTaskList().size() > 0 && handler.getSelectedPlayer().getCurrentTask() instanceof MakeWoodRoutine){
				MakeWoodRoutine makeWoodRoutine = (MakeWoodRoutine) handler.getSelectedPlayer().getCurrentTask();
				makeWoodRoutine.getTree().setBlocked(false);
			}

			handler.getSelectedPlayer().deleteAllTasks();
			handler.getSelectedPlayer().setCarryable(null);

			if(tree != null){
				tree.setBlocked(true);
				handler.getSelectedPlayer().addTask(new MakeWoodRoutine(handler, handler.getSelectedPlayer(), new CutTree(handler.getSelectedPlayer(), tree), new WalkTo(tree.getRoot().x, tree.getRoot().y, handler.getSelectedPlayer()), tree));
			}else if(field != null){
				// start farming routine
				handler.getSelectedPlayer().addTask(new FarmingRoutine(handler.getSelectedPlayer(), handler, new WalkTo(field.getX(), field.getY() + 32, handler.getSelectedPlayer()), field));
			}else{
				if(handler.getGraph().getNode((int)mouseTile.x, (int)mouseTile.y) != null){
					handler.getSelectedPlayer().addTask(new WalkTo(mouseCoords.x, mouseCoords.y, handler.getSelectedPlayer()));
				}
			}

			mouseReleased = false;
		}
		if(!Mouse.isButtonDown(1)){
			mouseReleased = true;
		}
		if(!Mouse.isButtonDown(0)){
			mouseLeftReleased = true;
		}

		int mX = Mouse.getX();
		int mY = HEIGHT - Mouse.getY();

		if(Mouse.isButtonDown(0) && mouseLeftReleased){
			mouseLeftReleased = false;
			if(hud.checkIfButtonIsPressed(mX, mY))
				return;

			if(hud.getObjectToPlaceOnMap() != null)
				return;

			if(handler.getSelectedPlayer() != null)
				hud.setShowAll(false);

			handler.setSelectedPlayer(null);
			for(Player player : handler.getPlayerList()){
				int x = (int) (Mouse.getX() - (MOVEMENT_X * SCALE)  );
				int y = (int) (HEIGHT - Mouse.getY() - (MOVEMENT_Y * SCALE));
				if(player.getTotalBounds().contains(x,y)){
					handler.setSelectedPlayer(player);
				}
			}
		}
	}

	// change cursor to hoe
	private Field checkForField(){
		int x = (int) (Mouse.getX() - (MOVEMENT_X * SCALE)  );
		int y = (int) (HEIGHT - Mouse.getY() - (MOVEMENT_Y * SCALE));

		for(Field field : handler.getFieldList()){
			if(field.getBounds().contains(x,y)){
				this.currentCursor = this.cursorHoe;
				return field;
			}
		}
		return null;
	}

	private Tree checkForCuttingTree(){
		Tree tree = null;
		int x = (int) (Mouse.getX() - (MOVEMENT_X * SCALE)  );
		int y = (int) (HEIGHT - Mouse.getY() - (MOVEMENT_Y * SCALE));
		this.currentCursor = this.cursor;

		for(Tree t : handler.getTreeList()){
			if(t.getBounds().contains(x, y) && !t.isBlocked() && !t.isSapling()){
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

	public boolean isMouseReleased() {
		return mouseReleased;
	}

	public void setMouseReleased(boolean mouseReleased) {
		this.mouseReleased = mouseReleased;
	}
}
