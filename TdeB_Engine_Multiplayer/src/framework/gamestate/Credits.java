package framework.gamestate;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;

import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.core.StateManager;
import framework.ui.Button;
import framework.ui.HeadUpDisplay;
import framework.ui.UI;

public class Credits {
	
	private Image background;
	private UI ui;
	private StateManager manager;
	private HeadUpDisplay hud;
	private int messageY;
	
	public Credits(StateManager manager) {
		this.ui = new UI();
		this.hud = new HeadUpDisplay(manager.getHandler(), 16, manager);
		this.background = quickLoaderImage("hud/menu_screenshot");
		this.manager = manager;
		this.ui.addButton("Menu", "hud/button_menu", 64, 64, 256, 64);
		this.messageY = 200;
	}
	
	public void update() {

		if(ui.isButtonClicked("Menu")){
			manager.setMenu(new Mainmenu(manager));
		}	
		
		mouseHoverOverButton();
	}
	
	public void draw() {
		drawQuadImageStatic(background, 0, 0, 1024, 1024);
		ui.draw();
		
		// write credits
		hud.drawString(64, messageY, "Created  and  Directed  by: ", new Color(243, 243, 243));
		hud.drawString(400, messageY, "Jonas  Maennle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 64, "Executive  Producer: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 64, "Jonas  Maennle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 128, "Lead  Gameplay  Designer: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 128, "Jonas  Maennle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 196, "Sidekick: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 196, "Jana  Wiegert", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 256, "Special  THANKS  to  all  Testplayers!", new Color(243, 243, 243));
		hud.drawString(400, messageY + 256, "", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 320, ">>  Special THANKS  to  Scut  for  the  Amazing  Tileset", new Color(243, 243, 243));
		hud.drawString(64, messageY + 320+64, ">>  View  his  Profile  at   https://scut.itch.io/", new Color(243, 243, 243));
	}
	
	private void mouseHoverOverButton() {
		// create Rect for Mouse coords
		Rectangle mouse = new Rectangle((int)(Mouse.getX() - MOVEMENT_X), (int)(HEIGHT - Mouse.getY() - MOVEMENT_Y), 1, 1);
		
		for(Button b : ui.getButtonList()) {		
			if(b.getBounds().intersects(mouse)) {
				b.setImage(quickLoaderImage("hud/button_menu_selected"));
			}else {
				b.setImage(quickLoaderImage("hud/button_menu"));
			}
		}
	}
}
