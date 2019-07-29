package framework.arena;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.WIDTH;
import static framework.helper.Collection.loadCustomFont;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import framework.core.BackgroundHandler;
import framework.core.Handler;
import framework.helper.Collection;
import framework.ui.UI;

public class ScoreScreen {
	
	private BackgroundHandler backgroundHandler;
	private ScoreClient client;
	private UI ui;
	private boolean isSending;
	private boolean sendClicked;
	
	private Font awtFont;
	private TrueTypeFont font;
	
	public ScoreScreen(Handler handler) {
		this.backgroundHandler = new BackgroundHandler();
		this.client = new ScoreClient(handler);
		this.ui = new UI();
		this.ui.addButton("Send", "hud/button_score", (WIDTH / 2) - 128, HEIGHT/2, 256, 64);
		this.isSending = false;
		this.sendClicked = false;
		
		this.awtFont = loadCustomFont("font/Pixel-Miners.ttf", 16);
		this.font = new TrueTypeFont(awtFont, false);
	}
	
	public void update() {
		
		while(Keyboard.next()){
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}	
		
		if(ui.isButtonClicked("Send")) {
			isSending = true;
		}
		
		if(isSending && !Mouse.isButtonDown(0) && !sendClicked) {
			client.setMyEntry(new ScoreEntry(Collection.ARENA_CURRENT_WAVE - 1, readName()));
			Thread sender = new Thread(client);
			sender.start();
			isSending = false;
			sendClicked = true;
		}
	}
	
	private void showScoreBoard() {

		ArrayList<ScoreEntry> list = client.getScoreList();
		
		Collections.sort(list, Comparator.comparingInt(ScoreEntry::getWave).reversed());
		
		for(int i = 0; i < list.size(); i++) {
			if(i < 10)
				drawString(64, 64 + (i * 64), "Wave: " + list.get(i).getWave() + "\tName: " + list.get(i).getName(), new Color(200, 200, 200));
		}
	}

	private String readName() {
		Scanner sc = new Scanner(System.in);
		String name = "name";
		System.out.print("pls enter name: ");
		name = sc.nextLine();	
		sc.close();
		return name;
	}
	
	public void render() {
		backgroundHandler.draw();
		ui.draw();
		showScoreBoard();
		
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
}
