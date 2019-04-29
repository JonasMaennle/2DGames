package object;

import java.awt.Rectangle;
import static helper.Graphics.*;
import static core.Constants.*;
import static helper.Collection.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Entity.GameEntity;
import core.Handler;
import shader.Light;

public class Player implements GameEntity{
	
	private int width, height;
	private float x, y, velX, velY, speed;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom;
	private Image placeholder;
	private Handler handler;
	private Light playerLight;
	
	public Player(int x, int y, Handler handler)
	{
		this.handler = handler;
		
		this.x = x;
		this.y = y;
		this.width = 32;
		this.height = 32;
		this.speed = 4f;
		
		this.placeholder = quickLoaderImage("player/Player_tmp");
		
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectRight = new Rectangle((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4);
		
		this.playerLight = new Light(new Vector2f(0, 0), 104, 224, 255, 10);
		
		lights.add(playerLight);
	}

	public void update() {
		velX = 0;
		velY = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			velX += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			velX -= 1;
		}		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			velY = 1;
		}	
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			velY = -1;
		}
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
	}

	private void mapCollision() {
		for(GameEntity t : handler.obstacleList)
		{
			updateBounds();
			if(t instanceof Tile)
			{
				// top
				if(((Tile) t).getBottomBounds().intersects(rectTop))
				{
					velY = 0;
					y = (float) (t.getY() + t.getHeight());
					updateBounds();
				}
				// bottom
				if(((Tile) t).getTopBounds().intersects(rectBottom))
				{	
					velY = 0;
					y = (float) (t.getY() - TILE_SIZE);
					updateBounds();
				}		
				// left
				if(((Tile) t).getRightBounds().intersects(rectLeft))
				{
					velX = 0;
					x = (float) (t.getX() + t.getWidth());
					updateBounds();
				}
				// right
				if(((Tile) t).getLeftBounds().intersects(rectRight))
				{
					velX = 0;
					x = (float) (t.getX() - TILE_SIZE);
					updateBounds();
				}
			}
		}
	}

	public void draw() {
		playerLight.setLocation(new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 16));
		drawQuadImage(placeholder, x, y, width, height);
	}
	
	@SuppressWarnings("unused")
	private void drawBounds(){
		drawQuad((int)x, (int)y, 16, TILE_SIZE); // left
		drawQuad((int)x + TILE_SIZE - 16,(int) y, 16, TILE_SIZE); // right
		
		drawQuad((int)x,(int) y, TILE_SIZE, 16); // top
		drawQuad((int)x,(int) y + TILE_SIZE - 16, TILE_SIZE, 16); // bottom
	}
	
	private void updateBounds(){
		this.rectLeft.setBounds((int)x, (int)y + 8, 16, TILE_SIZE - 8); // left
		this.rectRight.setBounds((int)x + TILE_SIZE - 16,(int) y + 8, 16, TILE_SIZE - 8); // right
		
		this.rectTop.setBounds((int)x + 8,(int) y, TILE_SIZE - 8, 16); // top
		this.rectBottom.setBounds((int)x + 8,(int) y + TILE_SIZE - 16, TILE_SIZE - 8, 16); // bottom
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setX(float x) {
		this.x = (int)x;
	}

	@Override
	public void setY(float y) {
		this.y = (int)y;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		return null;
	}
}
