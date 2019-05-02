package framework.helper;

import static framework.helper.Collection.*;
import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import framework.entity.GameEntity;
import framework.shader.Light;

public class Graphics {
	
	public static Image quickLoaderImage(String name){
		//Texture tex = quickLoad(name); old
		Image image = null;
		
		try {
			image = new Image("" + name + ".png"); // "res/"
		} catch (SlickException e) {
			e.printStackTrace();
		}
		if(image == null)System.out.println("Image cloud not load!!!");
		return image;
	}
	
	public static SpriteSheet loadSpriteSheet(String name, int tileWidth, int tileHeight){ // idr. 64, 64
		SpriteSheet tempSheet = null;
		try {
			tempSheet = new SpriteSheet(name + ".png", tileWidth, tileHeight);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return tempSheet;
	}
	
	// Draw non image-based quad
	public static void drawQuad(float x, float y, float width, float height){
		glBegin(GL_QUADS);
		glVertex2f(x + MOVEMENT_X, y + MOVEMENT_Y);
		glVertex2f(x + width + MOVEMENT_X, y + MOVEMENT_Y); 
		glVertex2f(x+ width + MOVEMENT_X, y + height + MOVEMENT_Y);
		glVertex2f(x + MOVEMENT_X, y + height + MOVEMENT_Y);
		glEnd();
	}
	
	// Default draw method
	public static void drawQuadImage(Image img, float x, float y, float width, float height){	
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
	public static void drawQuadImageStatic(Image img, float x, float y, float width, float height){	
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
	
	public static void drawAnimation(Animation anim, float x, float y, float width, float height){
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // WICHTIG! -> wenn Texture/Image mit transarentem Hintergrund gemalt werden soll
		anim.draw(x + MOVEMENT_X, y + MOVEMENT_Y, width, height);
		glDisable(GL_BLEND);
	}
	
	// Rotate center
	public static void drawQuadImageRotCenter(Image image, float x, float y, float width, float height, float angle){	
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
	
	// Rotate right
	public static void drawQuadImageRotRight(Image image, float x, float y, float width, float height, float angle){	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		image.bind();
		glTranslatef(x + ((width / 3) * 2) + MOVEMENT_X, y + height + MOVEMENT_Y, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(- width / 1.1f, - height / 2, 0);
			
		glBegin(GL_QUADS);
		
		// left top
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		//right top
		glTexCoord2f(1, 0);
		glVertex2f(width, 0);
		// right bottom
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		// left bottom
		glTexCoord2f(0, 1);
		glVertex2f(0, height);
		
		glEnd();
		
		glLoadIdentity();
		glDisable(GL_BLEND);
	}
	
	// Rotate left
	public static void drawQuadImageRotLeft(Image image, float x, float y, float width, float height, float angle){	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		image.bind();
		glTranslatef(x + (width /3) + MOVEMENT_X, y + height + MOVEMENT_Y, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(- width / 5, - height / 2, 0);
				
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
	
	// for sun rendering
	public static void renderSingleLightStatic(CopyOnWriteArrayList<GameEntity> entityList, Light light){
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

		for (GameEntity e : entityList) 
		{
			// check if e is in range
			if(e.getX() > getLeftBorder() - WIDTH/2 && e.getX() < getRightBorder() + WIDTH/2) // 
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
		}
			
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			light.getShader().useProgram();
			glUniform2f(
					glGetUniformLocation(light.getShader().getProgram(), "lightLocation"),
					light.location.getX(), HEIGHT - light.location.getY());
			glUniform3f(
					glGetUniformLocation(light.getShader().getProgram(), "lightColor"),
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
			light.getShader().unUse();
			glClear(GL_STENCIL_BUFFER_BIT);
	}
	
	
	// render all lights from light list
	public static void renderLightEntity(CopyOnWriteArrayList<GameEntity> entityList){
		//timerStart();
		//System.out.println(lights.size());
		for (Light light : lights) 
		{
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

			for (GameEntity e : entityList) 
			{
				// check if e is in range
				if(e.getX() > getLeftBorder() - WIDTH/2 && e.getX() < getRightBorder() + WIDTH/2)
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
			}
			
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
			glStencilFunc(GL_EQUAL, 0, 1);
			glColorMask(true, true, true, true);

			light.getShader().useProgram();
			glUniform2f(
					glGetUniformLocation(light.getShader().getProgram(), "lightLocation"),
					light.location.getX(), HEIGHT - light.location.getY());
			glUniform3f(
					glGetUniformLocation(light.getShader().getProgram(), "lightColor"),
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
			light.getShader().unUse();
			glClear(GL_STENCIL_BUFFER_BIT);
		}
		//timerEnd();
	}
}
