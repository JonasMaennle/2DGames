package object;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Graphics.*;
import static helpers.Setup.TILE_SIZE;

import java.awt.Rectangle;

import Enity.Entity;
import Enity.TileType;
import data.Camera;
import data.Handler;
import data.Tile;

public class Speeder implements Entity{
	
	private float x, y, speed, velX, velY, gravity, angle;
	private int width, height;
	private Image imageRight, imagePlayer;
	private Handler handler;
	private boolean enabled, alive, init;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom, rampSensor;
	
	public Speeder(float x, float y, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = 256;
		this.height = 128;
		this.velX = 0;
		this.velY = 0;
		this.speed = 4.0f;
		this.gravity = 8;
		this.enabled = false;
		this.alive = true;
		this.handler = handler;
		this.init = false;
		this.angle = 0;
		
		this.imageRight = quickLoaderImage("player/endor_speeder_right");
		this.imagePlayer = quickLoaderImage("player/endor_speeder_right_player");
		
		this.rectLeft = new Rectangle(1,1,1,1);
		this.rectRight = new Rectangle(1,1,1,1);
		this.rectTop = new Rectangle(1,1,1,1);
		this.rectBottom = new Rectangle(1,1,1,1);
		
		this.rampSensor = new Rectangle(1,1,1,1);
	}

	public void update() 
	{
		if(enabled)
		{
			if(!init)
			{
				init = true;
				y -= 64;
			}
			if(velX > 7)	
				velX = 7;
			if(velX < -2)
				velX = -2;
			
			velY = gravity;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				velX += 0.1;
			}
			if(!Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				if(velX > 0)
					velX *= 0.95f;
				if(velX < 0.1f)
					velX = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				velX -= 0.5f;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_X))
			{
				playerExitSpeeder();
			}
			
			x += velX * speed;
			y += velY;
			
			mapCollision();
			calcAngle();
		}
	}

	public void draw() 
	{
		drawQuadImageRot2(imageRight, x, y, width, height, angle);
		if(enabled)
			drawQuadImageRot2(imagePlayer, x + 50, y - 35, 110, 125, angle);
		
		//drawBounds();
	}
	
	private void mapCollision()
	{
		updateBounds();

		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), t.getWidth(), t.getHeight());
			
			if(r.intersects(rampSensor))
			{
				if(t.getType() == TileType.Ramp_Start)
				{
					angle = 340;
					//System.out.println(y + " " + ((x+TILE_SIZE - t.getX())/2));
					y = (t.getY() - TILE_SIZE) - ((x+(width/2) - t.getX())/2);
					return;
				}else if(t.getType() == TileType.Ramp_End)
				{
					angle = 340;
					//System.out.println(y + " " + ((x+TILE_SIZE - t.getX())));
					y = (t.getY() - TILE_SIZE) - ((x+(width/2) - t.getX()))-32;
					return;
				}
			}
			
			if(r.intersects(rectTop))
			{
				angle = 0;
				velY = gravity;
				y = (float) (r.getY() + t.getHeight());
				return;
			}
			if(r.intersects(rectLeft))
			{
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					angle = 0;
					velX = 0;
					x = (float) (r.getX() + r.getWidth() - 20);
				}
			}
			if(r.intersects(rectRight))
			{
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					angle = 0;
					velX = 0;
					x = (float) (r.getX() - width) + 20;
				}
			}
			if(r.intersects(rectBottom))
			{
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					angle = 0;
					velY = 0;
					y = (float) (r.getY() - height+1);
				}
			}
		}
	}
	
	private void calcAngle()
	{
		
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x + 20, (int)y + 4, 4, height - 18); // left
		this.rectRight.setBounds((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		this.rectTop.setBounds((int)x + 24, (int)y, width -48, 4); // top
		this.rectBottom.setBounds((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
		
		this.rampSensor.setBounds((int)x + (width/2)-2, (int)y + height - 16, 4, 4); // sensor
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad((int)x + 20, (int)y + 4, 4, height - 18); // left
		drawQuad((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		drawQuad((int)x + 24, (int)y, width -48, 4); // top
		drawQuad((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
		
		drawQuad((int)x + (width/2)-2, (int)y + height - 16, 4, 4); // sensor
	}
	
	private void playerExitSpeeder()
	{
		enabled = false;
		handler.setCurrentEntity(handler.player);
		handler.getStatemanager().getGame().setCamera(new Camera(handler.getCurrentEntity()));
		handler.player.setX(x + width/2 - TILE_SIZE);
		handler.player.setY(y - TILE_SIZE/2);
		handler.getStatemanager().getGame().getBackgroundHandler().setEntity(handler.getCurrentEntity());
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
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}

	@Override
	public void damage(int amount) 
	{
		
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setEnabled(boolean enabled) 
	{
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
