package framework.core;

import static framework.helper.Graphics.*;

import org.newdawn.slick.Image;

import framework.helper.Collection;

public class BackgroundHandler {
	
	private Image sky;
	
	public BackgroundHandler(){
		this.sky = quickLoaderImage("background/sky");
	}
	
	public void draw(){
		drawQuadImageStatic(sky, 0, 0, Collection.WIDTH * 2, Collection.HEIGHT * 2);
	}
}
