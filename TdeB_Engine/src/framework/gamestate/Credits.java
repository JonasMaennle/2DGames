package framework.gamestate;

import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import framework.core.StateManager;
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
		this.hud = new HeadUpDisplay(manager.getHandler(), 16);
		this.background = quickLoaderImage("hud/menu_screenshot");
		this.manager = manager;
		this.ui.addButton("Menu", "hud/button_menu", 64, 64, 256, 64);
		this.messageY = 200;
	}
	
	public void update() {

		if(ui.isButtonClicked("Menu")){
			manager.setMenu(new Mainmenu(manager));
		}	
	}
	
	public void draw() {
		drawQuadImageStatic(background, 0, 0, 1024, 1024);
		ui.draw();
		
		// write credits
		hud.drawString(64, messageY, "Created  and  Directed  by: ", new Color(243, 243, 243));
		hud.drawString(400, messageY, "Jonas  Männle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 64, "Executive  Producer: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 64, "Jonas  Männle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 128, "Lead  Gameplay  Designer: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 128, "Jonas  Männle", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 196, "Unnecessary  Sidekick: ", new Color(243, 243, 243));
		hud.drawString(400, messageY + 196, "Jana  Wiegert", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 256, "Special  THANKS  to  all  Testplayers!", new Color(243, 243, 243));
		hud.drawString(400, messageY + 256, "", new Color(243, 243, 243));
		
		hud.drawString(64, messageY + 320, ">>  Special THANKS  to  Scut  for  the  Amazing  Tileset", new Color(243, 243, 243));
		hud.drawString(64, messageY + 320+64, ">>  View  his  Profile  at   https://scut.itch.io/", new Color(243, 243, 243));
	}
}
