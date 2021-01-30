package framework.ui;

import framework.core.service.TilePlacementService;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import static framework.helper.Collection.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import framework.core.Handler;
import framework.core.StateManager;

import static framework.helper.Collection.loadCustomFont;
import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeadUpDisplay {
	
	private TrueTypeFont font;
	private Font awtFont;

	private int r;
	private int g;
	private int b;
	private int alpha;

	private Handler handler;

	private List<Button> buttonList;
	private int offsetTop, offsetBottom;
	private boolean showAll;
	private Button currentButton;

	private Image objectToPlaceOnMap, builderGrid;
	private boolean toggleMouse;
	private StateManager stateManager;
	private TilePlacementService tilePlacementService;
	
	public HeadUpDisplay(Handler handler, int fontSize, StateManager manager){
		this.stateManager = manager;
		this.handler = handler;
		this.tilePlacementService = new TilePlacementService(handler);
		this.buttonList = new ArrayList<>();
		this.awtFont = loadCustomFont("font/SIMPLIFICA.ttf", fontSize, Font.PLAIN);
		this.font = new TrueTypeFont(awtFont, false);
		setColors();
		this.builderGrid = quickLoaderImage("hud/builder_grid");
		this.toggleMouse = false;
		this.offsetTop = HEIGHT / 10;
		this.offsetBottom = HEIGHT / 10;

		buttonList.add(new Button("MainSelect", quickLoaderImage("hud/sign"), WIDTH / 16, (HEIGHT / 2) - (WIDTH / 48), WIDTH / 24, WIDTH / 24));
		buttonList.add(new Button("Field", quickLoaderImage("hud/field"), WIDTH / 16, (HEIGHT / 2) - (WIDTH / 48) - offsetTop, WIDTH / 24, WIDTH / 24));
		offsetTop += offsetTop;
		buttonList.add(new Button("Carott", quickLoaderImage("hud/sign_carotts"), WIDTH / 16, (HEIGHT / 2) - (WIDTH / 48) - offsetTop, WIDTH / 24, WIDTH / 24));
		buttonList.add(new Button("Tree00", quickLoaderImage("hud/sign_tree_00"), WIDTH / 16, (HEIGHT / 2) - (WIDTH / 48) + offsetBottom, WIDTH / 24, WIDTH / 24));
		offsetBottom += offsetBottom;
		buttonList.add(new Button("Tree01", quickLoaderImage("hud/sign_tree_01"), WIDTH / 16, (HEIGHT / 2) - (WIDTH / 48) + offsetBottom, WIDTH / 24, WIDTH / 24));
	}
	
	public void update() {
		int mX = Mouse.getX();
		int mY = HEIGHT - Mouse.getY();

		hoverOverMain(mX,mY);

		if(currentButton != null){
			if(currentButton.getName().equals("Field")){
				objectToPlaceOnMap = quickLoaderImage("hud/plane_field");
			}
			if(currentButton.getName().equals("Tree00")){
				objectToPlaceOnMap = quickLoaderImage("trees/tree_00");
			}
			if(currentButton.getName().equals("Tree01")){
				objectToPlaceOnMap = quickLoaderImage("trees/tree_01");
			}
			if(currentButton.getName().equals("Carott")){
				objectToPlaceOnMap = quickLoaderImage("hud/carotts");
			}
		}

		// end placing
		if(Mouse.isButtonDown(1)){
			objectToPlaceOnMap = null;
			currentButton = null;
			stateManager.getGame().setMouseReleased(false);
			showAll = false;
			toggleMouse = false;
		}

		// place tile on map layer 0
		if(objectToPlaceOnMap != null){
			if(Mouse.isButtonDown(0) && toggleMouse){
				toggleMouse = false;
				int mx = Mouse.getX();
				int my = Mouse.getY();
				Vector2f mouseGrid = convertMouseCoordsToIsometricGrid(mx, my);
				int transformedX = (int) getIsometricCoordinates((int)mouseGrid.x, (int)mouseGrid.y).x; // get real coordinates
				int transformedY = (int) getIsometricCoordinates((int)mouseGrid.x, (int)mouseGrid.y).y;
				tilePlacementService.update(mouseGrid, transformedX, transformedY, currentButton.getName());
			}
			if(!Mouse.isButtonDown(0)){
				toggleMouse = true;
			}
		}
	}
	
	public void draw(){
		for(Button button : buttonList){
			if(showAll){
				button.draw();
			}else if(button.getName().equals("MainSelect")){
				button.draw();
			}
		}

		// selected tile / object
		if(objectToPlaceOnMap != null){
			for(int i = 0; i < TILES_WIDTH; i++){
				for(int j = 0; j < TILES_HEIGHT; j++){
					drawQuadImageStatic(builderGrid, i * 64 + MOVEMENT_X - (WIDTH/2), j * 32 + MOVEMENT_Y, 64, 64);
				}
			}

			switch (currentButton.getName()){
				case "Field":
				case "Carott":
					drawQuadImageStatic(objectToPlaceOnMap,Mouse.getX() - 32, HEIGHT - Mouse.getY() - 16, 64, 32);
					break;
				default:
					drawQuadImageStatic(objectToPlaceOnMap,Mouse.getX() - 32, HEIGHT - Mouse.getY() - 48, 64, 64);
					break;
			}
		}
	}

	public boolean checkIfButtonIsPressed(int mouseX, int mouseY){
		for(Button button : buttonList){
			if(button.getBounds().contains(mouseX, mouseY)){
				if(button.getName().equals("MainSelect")){
					showAll = (showAll == false) ? true : false;
				}
				currentButton = button;
				return true;
			}
		}
		return false;
	}

	public void hoverOverMain(int mouseX, int mouseY){
		for(Button button : buttonList){
			if(button.getBounds().contains(mouseX, mouseY) && button.getName().equals("MainSelect")){
				// do button anim
			}
		}
	}
	
	public void drawString(int x, int y, String text, Color color){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(r, g, b, alpha);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public void setColors() {
		r = 255;
		g = 255;
		b = 255;
		alpha = 255;
	}

	public Image getObjectToPlaceOnMap() {
		return objectToPlaceOnMap;
	}

	public void setObjectToPlaceOnMap(Image objectToPlaceOnMap) {
		this.objectToPlaceOnMap = objectToPlaceOnMap;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
}
