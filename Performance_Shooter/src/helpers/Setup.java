package helpers;

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
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import shader.Light;

public class Setup {

	// Game Settings
	public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static int tileCounter = 0;
	
	public static final int TILE_SIZE = 64;
	public static ArrayList<Light> lights = new ArrayList<Light>();
	public static float MOVEMENT_X, MOVEMENT_Y = 0;
	
	public static void beginSession()
	{
		Display.setTitle("StarWars Shooter");
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
			
			//Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
			Display.setFullscreen(true);
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
	}
	
	public static boolean checkCollision(float x1, float y1, float width1, float height1, float x2, float y2, float width2, float height2)
	{
		if(x1 + width1 > x2 && x1 < x2 + width2 && y1 + height1 > y2 && y1 < y2 + height2)
			return true;
		
		return false;
	}
	
	public static double roundNumber(double betrag) 
    { 
      double round = Math.round(betrag*10000); 
      round = round / 10000; 
      round = Math.round(round*1000); 
      round = round / 1000; 
      round = Math.round(round*100); 
      return round / 100; 
    }
	
	public static float getLeftBoarder()
	{
		return MOVEMENT_X * -1;
	}
	
	public static float getRightBoarder()
	{
		return (MOVEMENT_X * -1) + WIDTH;
	}
	
	public static float getTopBoarder()
	{
		return (MOVEMENT_Y * -1);
	}
	
	public static float getBottomBoarder()
	{
		return (MOVEMENT_Y * -1) + HEIGHT;
	}
}
