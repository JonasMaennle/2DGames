package gamestate;

import org.lwjgl.input.Keyboard;
import static helper.Graphics.*;
import static core.Constants.*;

import static helper.Collection.*;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import core.BackgroundHandler;
import core.Camera;
import core.Handler;

public class Game {
	
	private Handler handler;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	
	private Image filter;
	private float filterScale = 0.05f;
	private float filterValue = 0.0f;
	private float[][] alphaFilter;
	
	public Game(Handler handler)
	{
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();
		this.filter = quickLoaderImage("background/Filter");
		
		initFilter();
	}
	
	public void update(){
		
		camera.update();
		handler.update();
		
		while(Keyboard.next())
		{
			// Exit game
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				AL.destroy();
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public void render(){
		backgroundHandler.draw();
		handler.draw();
		
		renderLightEntity(shadowObstacleList);
		
		// draw filter
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
				GL11.glColor4f(0, 0, 0, alphaFilter[y][x]);
				drawQuadImageStatic(filter, (x*32), (y*32), 32, 32);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}
	}
	
	private void initFilter(){
		alphaFilter = new float[HEIGHT/TILE_SIZE + 1][(WIDTH/TILE_SIZE) + 1]; 		
		// fill array with value = 1
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++){
			for(int x = 0; x < WIDTH/TILE_SIZE + 1; x++){
				alphaFilter[y][x] = 1;
			}
		}
		
		// render filter for topFilterObstacle (objects in the fog of war)
		for(int i = 0; i < WIDTH/TILE_SIZE; i++){
			alphaFilter = drawCircle(WIDTH/2/TILE_SIZE, HEIGHT/2/TILE_SIZE, i, alphaFilter);
			if(i > 1)filterValue += filterScale;
		}
	}
	
	private float[][] drawCircle(int x, int y, int r, float[][] array) {
	    double angle, x1, y1;

	    int arrayWidth = array[0].length;
	    int arrayHeight = array.length;
	    
	    for (int i = 0; i < 360; i++) {
	        angle = i;
	        x1 = r * Math.cos(angle * Math.PI / 180);
	        y1 = r * Math.sin(angle * Math.PI / 180);

	        int ElX = (int) Math.round(x + x1);
	        int ElY = (int) Math.round(y + y1);
	        if(ElX < arrayWidth && ElX >= 0 && ElY < arrayHeight && ElY >= 0)
	        	array[ElY][ElX] = filterValue;
	    }  
	    return array;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
