package framework.core;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

public class BackgroundHandler {
	
	private Image sky;
	
	public BackgroundHandler(){
		this.sky = quickLoaderImage("background/sky");
	}
	
	public void draw(){
		drawQuadImageStatic(sky, 0, 0, 2048, 2048);
	}
}
