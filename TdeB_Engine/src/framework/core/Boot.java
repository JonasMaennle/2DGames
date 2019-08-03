package framework.core;

import static framework.helper.Collection.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.TrueTypeFont;

import framework.helper.Collection;

/**
 * Export as .jar Checkliste
 *  - windowMode = false;
 *  - Raspberry PI IP change
 *  - leveler disable map parsing
 */

public class Boot {
	
	public Boot(){
		//Mouse.setGrabbed(true);
		beginSession();
		StateManager statemanager = new StateManager();
		Mouse.setGrabbed(true);
		// Game loop
		while(!Display.isCloseRequested()){
			statemanager.update();
			Display.update();
			Display.sync(60);
		}	
		Display.destroy();
	}

	public static void main(String[] args) {
		new Boot();
	}
	
	private void beginSession(){	
		boolean windowMode = false;
		
		if(windowMode) {
			Collection.WIDTH = 960;
			Collection.HEIGHT = 640;
		}else {
			Collection.WIDTH =  (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			Collection.HEIGHT =  (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			Collection.SET_FULL_SCREEN = true;
		}
		
		// no 4K support yet
		if(WIDTH > 1920){
			WIDTH = 1920;
			HEIGHT = 1080;
			SET_FULL_SCREEN = false;
		}
		
		System.setProperty( "org.lwjgl.opengl.Window.undecorated" , "true" ) ;
		Display.setTitle("");
		
		//Display.setLocation((Display.getDisplayMode().getWidth()-WIDTH) / 2, 0);
		try {
			@SuppressWarnings("unused")
			DisplayMode displayMode;
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			
			for (int i = 0; i < modes.length; i++)
	         {
	             if (modes[i].getWidth() == WIDTH
	             && modes[i].getHeight() == HEIGHT
	             && modes[i].isFullscreenCapable())
	               {
	                    displayMode = modes[i];
	               }
	         }
			
			if(!SET_FULL_SCREEN)Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
			Display.setFullscreen(SET_FULL_SCREEN);
			Display.create(new PixelFormat(0, 16, 1));
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		Display.setVSyncEnabled(true); // <- geilo
		
		
		Collection.awtFont = loadCustomFont("font/Pixel-Miners.ttf", fontSize);
		Collection.font = new TrueTypeFont(awtFont, false);
	}
}
