package framework.helper;

import static framework.helper.Collection.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

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
	
	// Default draw method
	public static void drawQuadImage(Image img, float x, float y, float width, float height){	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		img.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE); 
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // Removes gap between tiles when zooming
		
		glTranslatef((x + MOVEMENT_X) * SCALE, (y + MOVEMENT_Y) * SCALE, 0);

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		
		glTexCoord2f(1, 0);
		glVertex2f(width * SCALE, 0);
		
		glTexCoord2f(1, 1);
		glVertex2f(width * SCALE, height * SCALE);
		
		glTexCoord2f(0, 1);
		glVertex2f(0, height * SCALE);
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
	
	// Rotate center
	public static void drawStaticImageRotCenter(Image image, float x, float y, float width, float height, float angle){	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		image.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x + (width / 2), y + (height / 2), 0);
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
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x + (width / 2) + MOVEMENT_X, y + (height / 2) + MOVEMENT_Y, 0);
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
	
	public static void drawQuadImageRotCenter(Image image, float x, float y, float width, float height, float angle, float alpha){	
		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1, 1, 1, alpha);
		image.bind();
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE); // Removes weird line above texture
		GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTranslatef(x + (width / 2) + MOVEMENT_X, y + (height / 2) + MOVEMENT_Y, 0);
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
		GL11.glColor4f(255, 255, 255, 255);
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
		
		for (Light light : lightsSecondLevel){
			// light.setLocation(new Vector2f(light.getLocation().x * SCALE + MOVEMENT_X, light.getLocation().y * SCALE + MOVEMENT_Y));
			int outOfScreenBorder = 64;
			// test if light is on screen
			if(light.getLocation().getX()-MOVEMENT_X < getRightBorder() + outOfScreenBorder && light.getLocation().getX()-MOVEMENT_X > getLeftBorder() - outOfScreenBorder && light.getLocation().getY()-MOVEMENT_Y > getTopBorder() - outOfScreenBorder && light.getLocation().getY()-MOVEMENT_Y < getBottomBorder() + outOfScreenBorder){
				
				glColorMask(false, false, false, false);
				glStencilFunc(GL_ALWAYS, 1, 1);
				glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

				for (GameEntity e : entityList) {
					
					// check if e is in range
					if(e.getX() > getLeftBorder() - outOfScreenBorder && e.getX() < getRightBorder() + outOfScreenBorder && e.getY() > getTopBorder() - outOfScreenBorder && e.getY() < getBottomBorder() + outOfScreenBorder){
						Vector2f[] vertices = e.getVertices();
						for (int i = 0; i < vertices.length; i++) {
							Vector2f currentVertex = vertices[i];
							Vector2f nextVertex = vertices[(i + 1) % vertices.length];
							Vector2f edge = Vector2f.sub(nextVertex, currentVertex, null);
							Vector2f normal = new Vector2f(edge.getY(), -edge.getX());
							Vector2f lightToCurrent = Vector2f.sub(currentVertex,
									light.location, null);
							if (Vector2f.dot(normal, lightToCurrent) > 0) {			
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
		}
	}
	
	// render all lights from light list - toplayer
	public static void renderLightList(CopyOnWriteArrayList<Light> lights){
		
		for (Light light : lights){		
				glColorMask(false, false, false, false);
				glStencilFunc(GL_ALWAYS, 1, 1);
				glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

				
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
	}
}
