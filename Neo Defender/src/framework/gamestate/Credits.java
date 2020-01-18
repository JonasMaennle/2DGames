package framework.gamestate;

import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;
import org.newdawn.slick.Image;

import framework.core.StateManager;

public class Credits {
	
	private Image background;
	
	public Credits(StateManager manager) {
		this.background = quickLoaderImage("hud/menu_screenshot");
	}
	
	public void update() {

	}
	
	public void draw() {
		drawQuadImageStatic(background, 0, 0, 1024, 1024);

	}
}
