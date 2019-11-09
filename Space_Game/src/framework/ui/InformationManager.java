package framework.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Color;

import framework.core.StateManager;

public class InformationManager {
	
	private CopyOnWriteArrayList<IngameMessage> messageList;
	
	public InformationManager() {
		this.messageList = new CopyOnWriteArrayList<>();
	}
	
	public void update() {
		switch (StateManager.CURRENT_LEVEL) {
		case 1:
			// messageList.add(new IngameMessage(WIDTH/2 - MOVEMENT_X - 220, HEIGHT/2 - 256 - MOVEMENT_Y, "find the glowing stone to escape !", new org.newdawn.slick.Color(255,5,255), 18, 5000));

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
