package object;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.Entity;
import Enity.TileType;

import static helpers.Graphics.*;
import static helpers.Leveler.*;
import static helpers.Setup.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import data.Camera;
import data.Handler;
import data.ParticleEvent;
import data.Tile;

public class AT_ST_Walker implements Entity{

	private float x, y, velX, velY, gravity, speed;
	private int width, height, health;
	private long timer1, timer2;
	private Image gun_left, gun_right, healthBackground, healthForeground, healthBorder;
	private Animation anim_WalkRight, anim_WalkLeft, anim_IdleRight, anim_IdleLeft;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom;
	private boolean enabled;
	private String currentAnimation;
	private Handler handler;
	private String direction;
	private float angle, destX, destY;
	private CopyOnWriteArrayList<Laser> laserList;
	private float laserSpawnX, laserSpawnY;
	private Sound laserShotSound;
	private boolean alive;
	private CopyOnWriteArrayList<Explosion> explosionList;
	
	public AT_ST_Walker(float x, float y, Handler handler) 
	{
		this.x = x;
		this.y = y;
		this.handler = handler;
		this.width = 154;
		this.height = 256;
		this.enabled = false;
		this.gravity = 4;
		this.velX = 0;
		this.velY = 0;
		this.speed = 3.0f;
		this.currentAnimation = "anim_idleRight";
		this.direction = "right";
		this.angle = 0;
		this.destX = 0;
		this.destY = 0;
		this.laserSpawnX = 0;
		this.laserSpawnY = 0;
		this.laserList = new CopyOnWriteArrayList<>();
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.health = width - 40;
		this.alive = true;
		
		this.rectLeft = new Rectangle(1,1,1,1);
		this.rectRight = new Rectangle(1,1,1,1);
		this.rectTop = new Rectangle(1,1,1,1);
		this.rectBottom = new Rectangle(1,1,1,1);
		
		this.gun_left = quickLoaderImage("player/atst_gun_left");
		this.gun_right = quickLoaderImage("player/atst_gun_right");
		
		this.healthBackground = quickLoaderImage("enemy/healthBackground");
		this.healthForeground = quickLoaderImage("enemy/healthForeground");
		this.healthBorder = quickLoaderImage("enemy/healthBorder");
		
		this.anim_WalkRight = new Animation(loadSpriteSheet("player/atst_walkRight", 154, 256), 100);
		this.anim_WalkLeft = new Animation(loadSpriteSheet("player/atst_walkLeft", 154, 256), 100);
		this.anim_IdleRight = new Animation(loadSpriteSheet("player/atst_idleRight", 154, 256), 200);
		this.anim_IdleLeft = new Animation(loadSpriteSheet("player/atst_idleLeft", 154, 256), 200);
		
		this.explosionList = new CopyOnWriteArrayList<>();
		
		try {
			this.laserShotSound = new Sound("sound/AT_ST_Laser.wav");
		} catch (SlickException e) {
			e.printStackTrace();}
	}
	
	public void update() 
	{
		if(enabled)
		{
			velX = 0;
			velY = gravity;
			
			updateDirection(Mouse.getX() - MOVEMENT_X, Mouse.getY() - MOVEMENT_Y);
			calcAngle(Mouse.getX() - MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				velX += 1;
				currentAnimation = "anim_walkRight";
				direction = "right";
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
			{
				velX -= 1;
				currentAnimation = "anim_walkLeft";
				direction = "left";
			}
			
			if(!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D))
			{
				if(direction.equals("right"))
					currentAnimation = "anim_idleRight";
				else
					currentAnimation = "anim_idleLeft";
			}
			
			if(Mouse.isButtonDown(0))
			{
				timer1 = System.currentTimeMillis();
				if(timer1 - timer2 > 500)
				{
					timer2 = timer1;
					shoot();
				}
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_X))
			{
				playerExitATST();
			}
			
			
			x += velX * speed;
			y += velY * speed;
			
			mapCollision();
		}
		
		for(Laser l : laserList)
		{
			l.update();
			// Check Laser collision with tiles
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					laserList.remove(l);
					damageTile(tile);
					explosionList.add(new Explosion((int)tile.getX(), (int)tile.getY(), 196, 20));
				}
				if(l.isOutOfMap())
				{
					laserList.remove(l);
				}
			}
			// Check Laser collision with enemies
			for(Enemy g : handler.enemyList)
			{
				if(checkCollision(g.getX(), g.getY(), g.getWidth(), g.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					g.damage(56); // Gungan got 56 HP
					handler.addParticleEvent(new ParticleEvent((int)g.getX(), (int)g.getY()+(g.getHeight()/2), 100, "red", "small"));
					laserList.remove(l);
					explosionList.add(new Explosion((int)g.getX(), (int)g.getY(), 128, 20));
				}
			}
		}
		
		for(Explosion e : explosionList)
		{
			if(!e.isAlive())
				explosionList.remove(e);
		}
	}

	public void draw() 
	{
		for(Laser l : laserList)
		{
			l.draw();
		}
		
		switch (currentAnimation) {
		case "anim_walkRight":
			laserSpawnX = x + 100;
			laserSpawnY = y + 78;
			drawQuadImageRotLeft(gun_right, x + 90, y + 75, 70, 6, angle);
			drawAnimation(anim_WalkRight, x, y, width, height);
			break;
		case "anim_walkLeft":
			laserSpawnX = x + 5;
			laserSpawnY = y + 78;
			drawQuadImageRotRight(gun_left, x - 80, y + 75, 70, 6, angle-180);
			drawAnimation(anim_WalkLeft, x, y, width, height);
			break;
		case "anim_idleRight":
			laserSpawnX = x + 100;
			laserSpawnY = y + 78;
			drawQuadImageRotLeft(gun_right, x + 90, y + 75, 70, 6, angle);
			drawAnimation(anim_IdleRight, x, y, width, height);
			break;
		case "anim_idleLeft":
			laserSpawnX = x + 5;
			laserSpawnY = y + 78;
			drawQuadImageRotRight(gun_left, x - 80, y + 75, 70, 6, angle-180);
			drawAnimation(anim_IdleLeft, x, y, width, height);
			break;
		default:
			break;
		}
		// draw health bar
		drawQuadImage(healthBackground, x + 20, y-20, width - 40, 8);
		drawQuadImage(healthForeground, x + 20, y-20, health, 8);
		drawQuadImage(healthBorder, x + 20, y-20, width - 40, 8);
		
		for(Explosion e : explosionList)
		{
			e.draw();
		}

		//drawBounds();
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	private void calcAngle(float destX, float destY)
	{
		angle = (float) Math.toDegrees(Math.atan2(destY - (y + 75), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
	    // block angle 320 - 360 && 0 - 30
	    if(direction.equals("right"))
	    {
	    	if(angle < 320 && angle > 180)
	    	{
	    		angle = 320;
	    	}else if(angle > 30 && angle < 180)
	    	{
	    		angle = 30;
	    	}else{
	    		this.destX = destX;
	    		this.destY = destY;
	    	}
	    }
	    // block angle if 220 - 180 && 180 - 150
	    if(direction.equals("left"))
	    {
	    	if(angle > 220 && angle < 360)
	    	{
	    		angle = 220;
	    	}else if(angle < 150 && angle > 0)
	    	{
	    		angle = 150;
	    	}else{
	    		this.destX = destX;
	    		this.destY = destY;
	    	}
	    }
		//System.out.println("Angle: " + angle);
	}
	
	private void mapCollision()
	{
		updateBounds();

		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), t.getWidth(), t.getHeight());
			
			if(r.intersects(rectTop))
			{
				velY = gravity;
				y = (float) (r.getY() + t.getHeight());
				return;
			}
			if(r.intersects(rectLeft))
			{
				velX = 0;
				x = (float) (r.getX() + r.getWidth() - 20);
			}
			if(r.intersects(rectRight))
			{
				velX = 0;
				x = (float) (r.getX() - width) + 20;
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - height+1);
			}
		}
	}
	
	private void shoot()
	{
		// walk right
		if(direction.equals("right") && destX > laserSpawnX)
		{
			if(destX < x + (width/2))
			{
				destX = getRightBorder() - destX;
			}
			laserList.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 42, 4, 30, "red", angle));
			laserShotSound.play();
		}
		
		// walk left
		if(direction.equals("left") && destX < laserSpawnX)
		{
			if(destX > x + (width/2))
			{
				destX = getLeftBorder() + destX;
			}
			laserList.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 42, 4, 30, "red", angle));
			laserShotSound.play();
		}
	}
	
	private void updateDirection(float mouseX, float mouseY)
	{
		if(mouseX > x + (width/2))
			direction = "right";
		if(mouseX < x + (width/2))
			direction = "left";
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x + 20, (int)y + 4, 4, height - 18); // left
		this.rectRight.setBounds((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		this.rectTop.setBounds((int)x + 24, (int)y, width -48, 4); // top
		this.rectBottom.setBounds((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad((int)x + 20, (int)y + 4, 4, height - 18); // left
		drawQuad((int)x + width - 24, (int)y + 4, 4, height - 18); // right
		drawQuad((int)x + 24, (int)y, width -48, 4); // top
		drawQuad((int)x + 24, (int)y + height - 4, width - 48, 4); // bottom
	}
	
	private void playerExitATST()
	{
		enabled = false;
		handler.setCurrentEntity(handler.player);
		handler.getStatemanager().getGame().setCamera(new Camera(handler.getCurrentEntity()));
		handler.player.setX(x + width/4);
		handler.player.setY(y + height / 3);
		handler.getStatemanager().getGame().getBackgroundHandler().setEntity(handler.getCurrentEntity());
		if(direction.equals("left"))
			currentAnimation = "anim_idleLeft";
		else
			currentAnimation = "anim_idleRight";
	}
	
	private void damageTile(Tile tile)
	{
		tile.setHp(tile.getHp() - 100);
		tile.addIndex();
		// Remove tile if hp <= 0
		if(tile.getHp() <= 0)
		{
			handler.getMap().setTile(tile.getXPlace(), tile.getYPlace(), TileType.NULL);
			if(tile.getType() == TileType.Rock_Basic)handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 100, "gray", "normal"));
			if(tile.getType() != TileType.Rock_Basic)handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 100, "brown", "normal"));
			handler.obstacleList.remove(tile);
		}
	}

	@Override
	public void damage(int amount) 
	{
		health -= (int)(amount * 0.2f);
		if(health <= 0)
		{
			health = 0;
			alive = false;
			playerExitATST();
		}
	}
	
	public boolean isOutOfMap()
	{
		if((TILES_HEIGHT * TILE_SIZE) < y)
			return true;
		return false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setX(float x) {
		this.x = x;
		
	}

	public void setY(float y) {
		this.y = y;
		
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, width, height);
	}
}
