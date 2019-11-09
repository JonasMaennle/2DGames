package framework.core;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

public class BackgroundHandler {
	
	private Image backgroundImage;
	private int width, height;
	
	public BackgroundHandler(Image image, int width, int height){
		this.backgroundImage = image;
		this.width = width;
		this.height = height;
	}
	
	public void draw(){
		drawQuadImageStatic(backgroundImage, 0, 0, width, height);
	}
}
