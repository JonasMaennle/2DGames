package core;

import org.newdawn.slick.Image;
import static helper.Graphics.*;

public class BackgroundHandler {
	
	private Image sky;
	
	public BackgroundHandler(){
		this.sky = quickLoaderImage("background/sky");
	}
	
	public void draw(){
		drawQuadImageStatic(sky, 0, 0, 2048, 2048);
	}
}
