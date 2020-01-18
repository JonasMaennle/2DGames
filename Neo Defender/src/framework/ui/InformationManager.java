package framework.ui;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.loadCustomFont;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

// Singleton class
public class InformationManager {
	
	private static InformationManager single_instance = null;
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
	
	public static InformationManager getInstance() {
		if(single_instance == null)
			single_instance = new InformationManager();
		return single_instance;
	}
	
	public void createNewMessage(float x, float y, String text, Color color, int ms){
		messageList.add(new IngameMessage(x, y, text, color, ms, score_font_ttf));
	}
	
	public void resetAll() {
		messageList.clear();
	}
	
	// inner message class
	public class IngameMessage {
		
		private float x, y;
		private String text;
		private Color color, color2;
		private TrueTypeFont font;
		private int durationTime;
		private long startTime;
		private float timeFactor;
		
		private int r, g, b, alpha;
		
		public IngameMessage(float x, float y, String text, Color color, int time, TrueTypeFont font){
			this.x = x;
			this.y = y;
			this.text = text;
			this.color = color;
			this.durationTime = time;
			this.startTime = 0;

			this.r = color.getRed();
			this.g = color.getGreen();
			this.b = color.getBlue();
			
			this.font = font;
		}
		
		public void draw(){
			
			//System.out.println(durationTime - (System.currentTimeMillis() - startTime));
			timeFactor = (float)(durationTime - (System.currentTimeMillis() - startTime)) / durationTime;
			timeFactor *= 255;
			
			alpha = (int)timeFactor;
			//System.out.println(alpha);
			color = new Color(r, g, b, alpha);
			
			glEnable(GL_BLEND);
			glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			font.drawString(x + MOVEMENT_X, y + MOVEMENT_Y, text, color);
			color2 = new Color(255, 255, 255, 255);
			font.drawString(x, y, "", color2);

			GL11.glDisable(GL_BLEND);
		}

		public int getTime() {
			return durationTime;
		}

		public void setTime(int time) {
			this.durationTime = time;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
	}
}
