package framework.arena;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.WIDTH;
import static framework.helper.Collection.loadCustomFont;
import static framework.helper.Graphics.drawQuadImageStatic;
import static framework.helper.Graphics.quickLoaderImage;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;

import framework.core.BackgroundHandler;
import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.helper.Collection;
import framework.ui.Button;
import framework.ui.UI;

public class ScoreScreen {
	
	private Image background;
	private BackgroundHandler backgroundHandler;
	private ScoreClient client;
	private UI ui;
	
	private boolean isSending;
	private boolean sendClicked;
	private boolean sendDataByEnter;
	private boolean mouseLifted;
	
	private Font awtFont;
	private TrueTypeFont font;
	private String userInput;
	
	public ScoreScreen(Handler handler) {
		this.backgroundHandler = new BackgroundHandler();
		this.client = new ScoreClient(handler);
		
		this.ui = new UI();
		this.ui.addButton("Send", "hud/button_score", (WIDTH / 2) + 32, HEIGHT - 85, 256, 64);
		this.ui.addButton("Back", "hud/button_menu", (WIDTH / 2) - 256 - 32, HEIGHT - 85, 256, 64);
		
		this.isSending = false;
		this.sendClicked = false;
		this.sendDataByEnter = false;
		this.mouseLifted = false;
		this.userInput = "";
		
		this.background = quickLoaderImage("hud/menu_screenshot");
		this.awtFont = loadCustomFont("font/Pixel-Miners.ttf", 16);
		this.font = new TrueTypeFont(awtFont, false);
	}
	
	public void update() {
		
		mouseHoverOverButton();
		
		if(!Mouse.isButtonDown(0)) {
			mouseLifted = true;
		}
		
		while(Keyboard.next()){
			int key;
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
			
			key = Keyboard.getEventKey();
			//System.out.println(key);
			// key must be >= 16 && <= 50	
			if(Keyboard.isKeyDown(key) && !sendDataByEnter && !isSending) {
				readUserInput(key);
			}
		}	

		if(mouseLifted) {
			if(ui.isButtonClicked("Send")) {
				isSending = true;
			}
			
			if(ui.isButtonClicked("Back")) {
				reset();
				StateManager.gameState = GameState.MAINMENU;
				return;
			}
		}

		if(isSending && !Mouse.isButtonDown(0) && !sendClicked && !sendDataByEnter) {
			if(userInput.length() == 0)
				userInput = "PLAYER";
			client.setMyEntry(new ScoreEntry(Collection.ARENA_CURRENT_WAVE - 1, userInput));
			Thread sender = new Thread(client);
			sender.start();
			sendDataByEnter = true;
			isSending = false;
			sendClicked = true;
		}
	}
	
	private void showScoreBoard() {

		ArrayList<ScoreEntry> list = client.getScoreList();
		
		Collections.sort(list, Comparator.comparingInt(ScoreEntry::getWave).reversed());
		
		for(int i = 0; i < list.size(); i++) {
			if(i < 10) {
				drawString(WIDTH / 2 - 348, 85 + (i * 48), (i+1) + ".", new Color(200, 200, 200));
				drawString(WIDTH / 2 - 300, 85 + (i * 48), "Wave:   " + list.get(i).getWave() + "           Name:   " + list.get(i).getName(), new Color(200, 200, 200));
			}
		}
	}
	
	private void readUserInput(int key) {
		
		// define key range
		if((key < 16 || key > 50) && key != 14)
			return;
		
		// AE, OE, UE
		if(key >= 39 && key <= 42)
			return;
		
		//System.out.println(key);
		// back
		if(key == 14) {
			if(userInput.length() > 0) {
				userInput = userInput.substring(0, userInput.length()-1);
			}
			//System.out.println(userInput);
			return;
		}
		
		// enter
		if(key == 28) {
			sendDataByEnter = true;
			if(userInput.length() == 0)
				userInput = "PLAYER";
			client.setMyEntry(new ScoreEntry(Collection.ARENA_CURRENT_WAVE - 1, userInput));
			Thread sender = new Thread(client);
			sender.start();
			//System.out.println(userInput);
			return;
		}
		
		if(userInput.length() <15) {
			String keyName = Keyboard.getKeyName(key);
			userInput += keyName;
		}	
	}
	
	public void render() {
		backgroundHandler.draw();
		drawQuadImageStatic(background,  WIDTH/2 - background.getWidth()/2, HEIGHT/2 - background.getHeight()/4, 1024, 1024);
		ui.draw();
		showScoreBoard();
		Collection.drawString((WIDTH / 2) - 270, 16, "Highscore  List  -  TOP  10", new Color(200, 200, 200));
		// draw user input
		drawString(WIDTH / 2 + 32, HEIGHT - 160, "Your  Score:   Wave  " + (Collection.ARENA_CURRENT_WAVE-1), new Color(200, 200, 200));
		
		drawString(WIDTH / 2 + 32, HEIGHT - 128, "Enter   Name:   " + userInput, new Color(200, 200, 200));
		
		for(int i = 0; i < 15; i++) {
			drawString((WIDTH / 2) + 32 + 143 + (i*14), HEIGHT - 128 + 4, "_", new Color(200, 200, 200));
		}
		
	}
	
	private void mouseHoverOverButton() {
		// create Rect for Mouse coords
		Rectangle mouse = new Rectangle((int)(Mouse.getX()), (int)(HEIGHT - Mouse.getY()), 1, 1);
		
		for(Button b : ui.getButtonList()) {		
			if(b.getBounds().intersects(mouse)) {
				switch (b.getName()) {
				case "Back":
					b.setImage(quickLoaderImage("hud/button_menu_selected"));
					break;
				case "Send":
					b.setImage(quickLoaderImage("hud/button_score_selected"));
					break;
				default:
					break;
				}
				
			}else {
				switch (b.getName()) {
				case "Back":
					b.setImage(quickLoaderImage("hud/button_menu"));
					break;
				case "Send":
					b.setImage(quickLoaderImage("hud/button_score"));
					break;
				default:
					break;
				}
			}
		}
	}

	public boolean isSendClicked() {
		return sendClicked;
	}

	public void setSendClicked(boolean sendClicked) {
		this.sendClicked = sendClicked;
	}
	
	private void drawString(int x, int y, String text, Color color){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		font.drawString(x, y, text, color);
		color = new Color(255, 255, 255, 255);
		font.drawString(x, y, "", color);

		GL11.glDisable(GL_BLEND);	
	}
	
	public void reset() {
		Collection.MOVEMENT_X = 0;
		Collection.MOVEMENT_Y = 0;
		this.isSending = false;
		this.sendClicked = false;
		this.userInput = "";
		this.sendDataByEnter = false;
		this.mouseLifted = false;
	}
}
