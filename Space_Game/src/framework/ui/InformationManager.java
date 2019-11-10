package framework.ui;

import static framework.helper.Collection.loadCustomFont;

import java.awt.Font;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class InformationManager {
	
	private CopyOnWriteArrayList<IngameMessage> messageList;
	private TrueTypeFont score_font_ttf;
	private Font score_font;
	
	public InformationManager() {
		this.messageList = new CopyOnWriteArrayList<>();
		
		// Score Font
		score_font = loadCustomFont("font/SIMPLIFICA.ttf", 40, Font.BOLD);
		score_font_ttf = new TrueTypeFont(score_font, false);
	}
	
	public void update() {}

	public void draw() {	
		for(IngameMessage m : messageList){
			// set initial timestamp
			if(m.getStartTime() == 0){
				m.setStartTime(System.currentTimeMillis());
			}
			// draw string during time, remove after
			if(System.currentTimeMillis() - m.getStartTime() < m.getTime()){
				m.draw();	
			}else{
				messageList.remove(m);
			}	
		}	
	}
	
	public void createNewMessage(float x, float y, String text, Color color, int ms){
		messageList.add(new IngameMessage(x, y, text, color, ms, score_font_ttf));
	}
	
	public void resetAll() {
		messageList.clear();
	}
}
