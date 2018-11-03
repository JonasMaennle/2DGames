package helpers;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Enity.Entity;
import shader.Light;
import shader.Shader;

public class Artist {
	
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
	
	public static void drawQuad(float x, float y, float width, float height)
	{
		glBegin(GL_QUADS);
		glVertex2f(x + MOVEMENT_X, y + MOVEMENT_Y);
		glVertex2f(x + width + MOVEMENT_X, y + MOVEMENT_Y); 
		glVertex2f(x+ width + MOVEMENT_X, y + height + MOVEMENT_Y);
		glVertex2f(x + MOVEMENT_X, y + height + MOVEMENT_Y);
		glEnd();
	}
	
	public static void drawQuadTex(Texture tex, float x, float y, float width, float height)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x, y, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadImage(Image img, float x, float y, float width, float height)
	{	
		
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		img.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x + MOVEMENT_X, y + MOVEMENT_Y, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	// No movement relative to Display
	public static void drawQuadImageStatic(Image img, float x, float y, float width, float height)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		img.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x, y, 0);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	// Rotate Y-Axis
	public static void drawQuadTexRotY(Texture tex, float x, float y, float width, float height, float angle)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind();
		glTranslatef(x + width/2, y + height/2, 0);
		glRotatef(angle, 0, 1, 1);
		glTranslatef(- width / 2, - height / 2, 0);
				
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadTexRot(Texture tex, float x, float y, float width, float height, float angle)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind();
		glTranslatef(x + width/2, y + height/2, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(- width / 2, - height / 2, 0);
				
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadImageRot(Image image, float x, float y, float width, float height, float angle)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		image.bind();
		glTranslatef(x + (width / 2) + MOVEMENT_X, y + height + MOVEMENT_Y, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(- width / 2, - height / 2, 0);
				
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	public static void drawAnimation(Animation anim, float x, float y, float width, float height)
	{
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // WICHTIG! -> wenn Texture/Image mit transarentem Hintergrund gemalt werden soll
		anim.draw(x + MOVEMENT_X, y + MOVEMENT_Y, width, height);
		glDisable(GL_BLEND);
	}
	
	public static void drawAnimationCenter(Animation anim, float x, float y, float width, float height)
	{
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // WICHTIG! -> wenn Texture/Image mit transarentem Hintergrund gemalt werden soll
		anim.draw(x, y, width, height);
		glDisable(GL_BLEND);
	}
	
	public static Texture loadTexture(String path, String fileType)
	{
		Texture tex = null;
		//System.out.println(path);
		// -> new
		ClassLoader cl = Artist.class.getClassLoader();
		InputStream in = cl.getResourceAsStream(path);

		try {
			tex = TextureLoader.getTexture(fileType, in);
		} catch (IOException e) {
			System.out.println("ERRRROR");
			e.printStackTrace();
		}
		return tex;
	}
	
	public static Image quickLoaderImage(String name)
	{
		Texture tex = quickLoad(name);
		return new Image(tex);
	}
	
	public static Texture quickLoad(String name)
	{
		Texture tex = null;
		//tex = loadTexture("res/" + name + ".png", "PNG");
		tex = loadTexture("" + name + ".png", "PNG");
		return tex;
	}
	
	public static SpriteSheet loadSpriteSheet(String name, int tileWidth, int tileHeight) // idr. 64, 64
	{
		SpriteSheet tempSheet = null;
		try {
			tempSheet = new SpriteSheet(name + ".png", tileWidth, tileHeight);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return tempSheet;
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
	
	public static void renderLightEntity(ArrayList<?> objectList, Shader shader)
	{
		//System.out.println(lights.size());
		@SuppressWarnings("unchecked")
		ArrayList<Entity> entityList = (ArrayList<Entity>) objectList;
		for (Light light : lights) 
		{
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			for (Entity e : entityList) 
			{
				Vector2f[] vertices = e.getVertices();
				for (int i = 0; i < vertices.length; i++) 
				{
					Vector2f currentVertex = vertices[i];
					Vector2f nextVertex = vertices[(i + 1) % vertices.length];
					Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
					Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
					Vector2f lightToCurrent = Vector2f.sub(currentVertex,
							light.location, null);
					if (Vector2f.dot(normal, lightToCurrent) > 0) 
					{
						Vector2f point1 = Vector2f.add(
								currentVertex,
								(Vector2f) Vector2f.sub(currentVertex, light.location, null).
								scale(800), 
								null
								);
						Vector2f point2 = Vector2f.add(
								nextVertex,
								(Vector2f) Vector2f.sub(nextVertex, light.location, null).
								scale(800), 
								null
								);
						glBegin(GL_QUADS);
						{
							glVertex2f(currentVertex.getX(), currentVertex.getY());
							glVertex2f(point1.getX(), point1.getY());
							glVertex2f(point2.getX(), point2.getY());
							glVertex2f(nextVertex.getX(), nextVertex.getY());
						}
						glEnd();
					}
				}
			}
			
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			shader.useProgram();
			glUniform2f(
					glGetUniformLocation(shader.getProgram(), "lightLocation"),
					light.location.getX(), HEIGHT - light.location.getY());
			glUniform3f(
					glGetUniformLocation(shader.getProgram(), "lightColor"),
					light.red, light.green, light.blue);
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			
			glBegin(GL_QUADS);
			{
				glVertex2f(0, 0);
				glVertex2f(0, HEIGHT);
				glVertex2f(WIDTH, HEIGHT);
				glVertex2f(WIDTH, 0);
			}
			glEnd();

			glDisable(GL_BLEND);
			shader.unUse();
			glClear(GL_STENCIL_BUFFER_BIT);
		}
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
}
