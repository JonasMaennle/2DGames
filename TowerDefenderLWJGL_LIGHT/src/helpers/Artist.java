package helpers;

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
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import data.Entity;
import shader.Light;
import shader.Shader;

public class Artist {
	
	// Game Settings
	//public static final int WIDTH = 1088 + (64 * 3), HEIGHT = 704, GAME_WIDTH = 1088;
	public static final int WIDTH = 1280 + (64 * 3), HEIGHT = 960, GAME_WIDTH = 1280;
	public static final int START_TILE_X = 2, START_TILE_Y = 0; // Choose Start Tile on Map
	
	public static final int TILE_SIZE = 64;
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
	public static void beginSession()
	{
		Display.setTitle("Sickes Games");
		//Display.setLocation((Display.getDisplayMode().getWidth()-WIDTH) / 2, 0);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
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
		glVertex2f(x, y);
		glVertex2f(x + width, y); 
		glVertex2f(x+ width, y + height);
		glVertex2f(x, y + height);
		glEnd();
	}
	
	public static void drawQuadTex(Texture tex, float x, float y, float width, float height)
	{	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind();
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
	
	public static Texture quickLoad(String name)
	{
		Texture tex = null;
		//tex = loadTexture("res/" + name + ".png", "PNG");
		tex = loadTexture("" + name + ".png", "PNG");
		return tex;
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
