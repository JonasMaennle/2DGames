package gamestate;

import org.lwjgl.input.Keyboard;
import static helper.Graphics.*;
import static core.Constants.*;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;

import static core.Constants.MOVEMENT_X;
import static core.Constants.MOVEMENT_Y;
import static helper.Collection.*;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import Entity.GameEntity;
import core.BackgroundHandler;
import core.Camera;
import core.Handler;

public class Game {
	
	private Handler handler;
	private Camera camera;
	private BackgroundHandler backgroundHandler;
	private Image filter;
	private float scale = 1.7f;
	private float filterValue = 0.01f;
	private float[][] filt;
	
	public Game(Handler handler)
	{
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();
		this.filter = quickLoaderImage("background/Filter");
		
		init();
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
		
		

		
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++)
		{
			for(int x = 0; x < WIDTH/TILE_SIZE; x++)
			{
				GL11.glColor4f(0, 0, 0, filt[y][x]);
				drawQuadImageStatic(filter, (x*32), (y*32), 32, 32);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}

	}
	
	void init()
	{
		filt = new float[HEIGHT/TILE_SIZE + 1][WIDTH/TILE_SIZE]; 
		
		
		for(int y = 0; y < HEIGHT/TILE_SIZE + 1; y++)
		{
			for(int x = 0; x < WIDTH/TILE_SIZE; x++)
			{
				filt[y][x] = 1;
			}
		}
		
		// render filter for topFilterObstacle (objects in the fog of war)
		for(int i = 0; i < HEIGHT/2/TILE_SIZE; i++)
		{
			filt = drawCircle(WIDTH/2/TILE_SIZE, HEIGHT/2/TILE_SIZE, i, filt);
			filterValue *= scale;
		}
	}
	
	float[][] drawCircle(int x, int y, int r, float[][] array) {
	    double PI = 3.1415926535;
	    double i, angle, x1, y1;

	    for (i = 0; i < 360; i += 1) {
	        angle = i;
	        x1 = r * Math.cos(angle * PI / 180);
	        y1 = r * Math.sin(angle * PI / 180);

	        int ElX = (int) Math.round(x + x1);
	        int ElY = (int) Math.round(y + y1);
	        
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
