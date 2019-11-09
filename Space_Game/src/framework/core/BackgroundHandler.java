package framework.core;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

public class BackgroundHandler {
	
	private Image backgroundImage, black;
	private Handler handler;
	private int width, height;
	private float center_x, center_y, left_x, left_y, right_x, right_y, top_x, top_y, bottom_x, bottom_y;
	private float leftTop_x, leftTop_y, leftBottm_x, leftBottom_y, rightTop_x, rightTop_y, rightBottom_x, rightBottom_y;
	
	private static float MOVEMENT_FACTOR = 0.5f;
	
	public BackgroundHandler(Image image, int width, int height, Handler handler){
		this.backgroundImage = image;
		this.black = quickLoaderImage("background/background");
		this.handler = handler;
		this.width = width;
		this.height = height;
		
		this.center_x = 0;
		this.center_y = 0;
		
		this.left_x = center_x - width;
		this.left_y = center_y;
		
		this.right_x = center_x + width;
		this.right_y = center_y;
		
		this.top_x = center_x;
		this.top_y = center_y - height;
		
		this.bottom_x = center_x;
		this.bottom_y = center_y + height;
		
		this.leftTop_x = center_x - width;
		this.leftTop_y = center_y - height;
		
		this.leftBottm_x = center_x - width;
		this.leftBottom_y = center_y + height;
		
		this.rightTop_x = center_x + width;
		this.rightTop_y = center_x - height;
		
		this.rightBottom_x = center_x + width;
		this.rightBottom_y = center_y + height;
	}
	
	public void draw(){
		drawQuadImageStatic(black, 0, 0, width * 2, height * 2);
		
		center_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		center_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		left_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		left_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR; 
		
		right_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		right_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		top_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		top_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		bottom_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		bottom_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		leftTop_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		leftTop_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		leftBottm_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		leftBottom_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		rightTop_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		rightTop_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		rightBottom_x -= handler.getPlayer().getVelX() * MOVEMENT_FACTOR;
		rightBottom_y -= handler.getPlayer().getVelY() * MOVEMENT_FACTOR;
		
		if(center_x >= width || center_x <= -width) {
			center_x = 0;
			left_x = center_x - width;
			right_x = center_x + width;
			top_x = center_x;
			bottom_x = center_x;
			leftTop_x = center_x - width;
			leftBottm_x = center_x - width;
			rightTop_x = center_x + width;
			rightBottom_x = center_x + width;
		}
		if(center_y >= height || center_y <= -height) {
			center_y = 0;
			left_y = center_y;
			right_y = center_y;
			top_y = center_y - height;
			bottom_y = center_y + height;
			leftTop_y = center_y - height;
			leftBottom_y = center_y + height;
			rightTop_y = center_x - height;
			rightBottom_y = center_y + height;
		}
		
		drawQuadImageStatic(backgroundImage, center_x, center_y, width, height); // center
		
		drawQuadImageStatic(backgroundImage, left_x, left_y, width, height); // left
		drawQuadImageStatic(backgroundImage, right_x, right_y, width, height); // right
		drawQuadImageStatic(backgroundImage, top_x, top_y, width, height); // top
		drawQuadImageStatic(backgroundImage, bottom_x, bottom_y, width, height); // bottom
		
		drawQuadImageStatic(backgroundImage, leftTop_x, leftTop_y, width, height); // leftTop
		drawQuadImageStatic(backgroundImage, leftBottm_x, leftBottom_y, width, height); // leftBottom
		drawQuadImageStatic(backgroundImage, rightTop_x, rightTop_y, width, height); // rightTop
		drawQuadImageStatic(backgroundImage, rightBottom_x, rightBottom_y, width, height); // rightBottom
	}
}
