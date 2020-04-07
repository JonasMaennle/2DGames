package framework.core;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

public class BackgroundHandler {
	
	private Image black;
	private int width, height;
	
	public BackgroundHandler(int width, int height, Handler handler){
		this.black = quickLoaderImage("background/background");
		this.width = width;
		this.height = height;
	}
	
	public void draw(){
		drawQuadImageStatic(black, 0, 0, width * 2, height * 2);
	}
}
