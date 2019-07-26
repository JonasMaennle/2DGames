package framework.ui;

import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.WIDTH;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;

import framework.core.Handler;
import framework.core.StateManager;

public class InformationManager {
	
	private Handler handler;
	private CopyOnWriteArrayList<IngameMessage> messageList;
	private boolean show1;
	
	public InformationManager(Handler handler) {
		this.handler = handler;
		this.messageList = new CopyOnWriteArrayList<>();
		this.show1 = false;
	}
	
	public void update() {
		switch (StateManager.CURRENT_LEVEL) {
		case 1:
			// find the glowing stone to escape !
			if(handler.getMaxEnemies() - 1 == handler.getEnemiesLeft() && !show1) {
				show1 = true;
				messageList.add(new IngameMessage(WIDTH/2 - MOVEMENT_X - 220, HEIGHT/2 - 256 - MOVEMENT_Y, "find the glowing stone to escape !", new org.newdawn.slick.Color(255,5,255), 18, 3500));
			}	

			break;
		case 2:
			
			break;

		default:
			break;
		}
	}

	public void draw() {	
		// draw messages
		messageFadeOut();		
	}
	
	public void createNewMessage(float x, float y, String text, Color color, int textSize, int ms){
		messageList.add(new IngameMessage(x, y, text, color, textSize, ms));
	}
	
	private void messageFadeOut(){
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
	
	public void resetAll() {
		messageList.clear();
	}
}
