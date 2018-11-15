package object;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import static helpers.Graphics.*;
import static helpers.Leveler.getLevelHeight;
import static helpers.Setup.TILE_SIZE;

import java.awt.Rectangle;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager;
import Gamestate.StateManager.GameState;
import data.Camera;
import data.Handler;
import data.ParticleEvent;
import data.Tile;

public class Speeder implements Entity{
	
	private float x, y, speed, velX, velY, gravity, angle;
	private int width, height, frameCount;
	private Image imageRight, imagePlayer;
	private Handler handler;
	private boolean enabled, alive, jumping;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom, rampSensor, hitBox;
	private final int MAX_SPEED = 10;
	private Explosion exposion;
	
	public Speeder(float x, float y, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = 256;
		this.height = 128;
		this.velX = 0;
		this.velY = 0;
		this.speed = 3.5f;
		this.gravity = 8;
		this.enabled = false;
		this.alive = true;
		this.handler = handler;
		this.angle = 0;
		this.jumping = false;
		this.frameCount = 100;
		
		this.imageRight = quickLoaderImage("player/endor_speeder_right");
		this.imagePlayer = quickLoaderImage("player/endor_speeder_right_player");
		
		this.rectLeft = new Rectangle(1,1,1,1);
		this.rectRight = new Rectangle(1,1,1,1);
		this.rectTop = new Rectangle(1,1,1,1);
		this.rectBottom = new Rectangle(1,1,1,1);
		
		this.rampSensor = new Rectangle(1,1,1,1);
		this.hitBox = new Rectangle(1,1,1,1);
	}

	public void update() 
	{
		if(enabled)
		{
			if(velX > 5)	
				velX = 5;
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
			// Jump
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !jumping)
			{
				jumping = true;
			}
			
			jump();
			
			x += velX * speed;
			y += velY;
			
			mapCollision();
			hitEnemy();
		}
	}

	public void draw() 
	{
		//drawBounds();
		if(!alive)
			exposion.draw();
		else{
			drawQuadImageRot2(imageRight, x, y, width, height, angle);
			if(enabled)
				drawQuadImageRot2(imagePlayer, x + 50, y - 35, 110, 125, angle);
		}
	}
	
	private void mapCollision()
	{
		updateBounds();

		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), t.getWidth(), t.getHeight());
			
			// check hitbox
			if(r.intersects(hitBox))
			{
				if(t.getType() == TileType.TreeStump_Left || t.getType() == TileType.TreeStump_Right)
					die();
			}
			
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
//			if(r.intersects(rectLeft))
//			{
//				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
//				{
//					angle = 0;
//					velX = 0;
//					x = (float) (r.getX() + r.getWidth() - 20);
//				}
//			}
//			if(r.intersects(rectRight))
//			{
//				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
//				{
//					angle = 0;
//					velX = 0;
//					x = (float) (r.getX() - width) + 20;
//				}
//			}
			if(r.intersects(rectBottom))
			{
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End && t.getType() != TileType.TreeStump_Left && t.getType() != TileType.TreeStump_Right && t.getType() != TileType.TreeStump_Center)
				{
					angle = 0;
					velY = 0;
					jumping = false;
					y = (float) (r.getY() - height+1);
				}
			}
		}
	}
	
	private void jump()
	{
		if(jumping)
		{
			if(jumping && frameCount >= 0)
			{
				
				velY -= frameCount * 0.2;
				frameCount -= 4;
				if(velY > MAX_SPEED)
				{
					velY = MAX_SPEED;
				}
			}
		}else{
			frameCount = 120;
		}
	}
	
	private void hitEnemy()
	{
		//handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 100, "gray", "normal"));
		for(GunganEnemy g : handler.gunganList)
		{
			Rectangle enemyBounds = new Rectangle((int)g.getX(), (int)g.getY(), g.getWidth(), g.getHeight());
			if(enemyBounds.intersects(hitBox))
			{
				handler.addParticleEvent(new ParticleEvent((int)g.getX(), (int)g.getY()+(g.getHeight()/2), 100, "red", "small"));
				handler.gunganList.remove(g);
			}
		}
	}
	
	private void die()
	{
		alive = false;
		exposion = new Explosion((int)x + width/3, (int)y + 32, 256, 40);
		playerExitSpeeder();
		enabled = false;
		StateManager.setState(GameState.DEAD);
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x + 20, (int)y + 4, 4, height - 18); // left
		this.rectRight.setBounds((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		this.rectTop.setBounds((int)x + 24, (int)y, width -48, 4); // top
		this.rectBottom.setBounds((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
		
		this.rampSensor.setBounds((int)x + (width/2)-2, (int)y + height - 16, 4, 4); // sensor
		this.hitBox.setBounds((int)x+20, (int)y+55, width-80, 35); // hit box
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad((int)x + 20, (int)y + 4, 4, height - 18); // left
		drawQuad((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		drawQuad((int)x + 24, (int)y, width -48, 4); // top
		drawQuad((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
		
		drawQuad((int)x + (width/2)-2, (int)y + height - 16, 4, 4); // sensor
		drawQuad((int)x+20, (int)y+55, width-80, 35);
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
	
	public boolean isOutOfMap()
	{
		if((getLevelHeight() * TILE_SIZE) < y)
			return true;
		return false;
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
