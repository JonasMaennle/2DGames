package object;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.MOVEMENT_Y;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.shader.Light;
import static framework.helper.Collection.*;

public class Player implements GameEntity{
	
	private float x, y;
	private int width, height;
	private float speed, velX, velY;;
	private Handler handler;
	private Image image, boundImage;
	private float playerRotation;
	private Light light;
	
	public Player(int x, int y, Handler handler) {
		this.x = x;
		this.y = y;
		this.handler = handler;
		
		this.width = 64;
		this.height = 64;
		this.image = quickLoaderImage("player/player");
		this.boundImage = quickLoaderImage("player/Player_tmp");
		this.speed = 4f;
		this.velX = 0;
		this.velY = 0;
		
		this.playerRotation = 0;
		
		this.light = new Light(new Vector2f(0, 0), 5, 17, 25, 8);
		//lights.add(light);
	}

	@Override
	public void update() {
		velX = velX * 0.99f;
		velY = velY * 0.99f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			playerRotation += 2;

		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			playerRotation -= 2;
			

		// only positive values between 0 - 360
		if(playerRotation < 0) {
			playerRotation = 360 + playerRotation;
		}
		playerRotation = playerRotation % 360;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			velX *= 0.95f;
			velY *= 0.95f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float sinFactor = (float) (Math.PI * playerRotation / 180.0);
			velX = (float) Math.sin(sinFactor);
					
			float cosFactor = (float) (Math.PI * playerRotation / 180.0);
			velY = (float) Math.cos(cosFactor) * -1;
		}
		

		x += velX * speed;
		y += velY * speed;	
		
		setLightToNose();
	}

	@Override
	public void draw() {

		//drawQuadImage(image, x, y, width, height);
		drawQuadImageRotCenter(image, x, y, width, height, playerRotation);
		renderSingleLightStatic(shadowObstacleList, light);
		// drawBounds
		// drawQuadImage(boundImage, x, y, width, height);
	}
	
	private void setLightToNose() {
		float radius = width / 2;
		float t = (float) Math.toRadians(playerRotation);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		light.setLocation(new Vector2f(x + width/2 + xT + MOVEMENT_X, y + height/2 + yT + MOVEMENT_Y));
	}

	@Override
	public float getX() { return x; }

	@Override
	public float getY() { return y; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public Vector2f[] getVertices() {
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}

	@Override
	public Rectangle getBounds() { return new Rectangle((int)x, (int)y, width, height); }

	@Override
	public Rectangle getTopBounds() { return new Rectangle((int)x, (int)y, width, 8); }

	@Override
	public Rectangle getBottomBounds() { return new Rectangle((int)x, (int)y + height - 8, width, 8); }

	@Override
	public Rectangle getLeftBounds() { return new Rectangle((int)x, (int)y + 8, 8, height - 16); }

	@Override
	public Rectangle getRightBounds() { return new Rectangle((int)x + width - 8, (int)y + 8, 8, height- 16); }
}
