package gamestate;

import org.lwjgl.input.Keyboard;
import static helper.Graphics.*;

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
	
	public Game(Handler handler)
	{
		this.handler = handler;
		this.camera = new Camera(handler.getCurrentEntity());
		this.backgroundHandler = new BackgroundHandler();
		this.filter = quickLoaderImage("background/Filter");
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
		
		// render filter for topFilterObstacle (objects in the fog of war)
		for(GameEntity g : topFilterObstacle)
		{
			GL11.glColor4f(0, 0, 0, 1f);
			drawQuadImageStatic(filter, g.getX() + MOVEMENT_X, g.getY() + MOVEMENT_Y, 32, 32);
			GL11.glColor4f(1, 1, 1, 1);
		}
		topFilterObstacle.clear();
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
