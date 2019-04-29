package object.weapon;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.Entity;
import data.Handler;
import data.ParticleEvent;
import object.Tile;
import object.enemy.Enemy;
import object.entity.Player;

import static helpers.Leveler.TILES_HEIGHT;
import static helpers.Setup.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

public class Weapon implements Entity{
	
	protected float x, y, width, height, angle, destX, destY;
	protected Player player;
	protected Image default_right, default_left;
	protected CopyOnWriteArrayList<Laser> list;
	protected Sound laserShotSound;
	protected Handler handler;
	protected float laserSpawnX, laserSpawnY;
	
	public Weapon(float x, float y, float width, float height, Player player, Handler handler, Image left, Image right)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = player;
		this.handler = handler;
		this.default_right = right; //quickLoaderImage("player/weapon_right");
		this.default_left =  left; //quickLoaderImage("player/weapon_left");
		
		this.list = new CopyOnWriteArrayList<>();
		this.angle = 0;
		
		try {
			this.laserShotSound = new Sound("sound/blaster_sound.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// Check Laser - Map collision
	public void update() 
	{
		this.x = player.getX() + 28;
		this.y = player.getY() + 28;
		
		for(Laser l : list)
		{
			l.update();
			// Check Laser collision with tiles
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					l.removeLight();
					list.remove(l);
					handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 10, 0, "gray", "normal"));
				}
				if(l.isOutOfMap())
				{
					l.removeLight();
					list.remove(l);
				}
			}
			// Check Laser collision with enemies
			for(Enemy g : handler.enemyList)
			{
				if(checkCollision(g.getX(), g.getY(), g.getWidth(), g.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					g.damage(7); // Gungan got 56 HP
					l.removeLight();
					list.remove(l);
				}
			}
		}
	}

	public void draw() 
	{
		// Draw laser shot	
		for(Laser l : list)
		{
			l.draw();
		}

		//drawQuadImageRotRight(default_left, player.getX()-92, player.getY()+6, width, height, angle - 180);

	}
	
	public void shoot()
	{
//		if(destX < x + (width/2))
//		{
//			destX = getRightBorder() - destX;
//		}
		list.add(new Laser(x, y, destX, destY, 24, 4, 30, "red", angle));
		laserShotSound.play();	
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	public void calcAngle(float destX, float destY)
	{
		angle = (float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
	    
		this.destX = destX;
		this.destY = destY;
		//System.out.println("Angle: " + angle);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	
	public void addToProjectileList()
	{
		for(Laser l: list)
		{
			projectileList.add(l);
		}
	}
	
	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	public int getWidth() 
	{
		return (int) width;
	}

	@Override
	public int getHeight() 
	{
		return (int) height;
	}

	@Override
	public void setWidth(int width) 
	{
		this.width = width;
	}

	@Override
	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) 
	{
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public float getVelX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVelY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOutOfMap() {
		if((TILES_HEIGHT * TILE_SIZE) < y)
			return true;
		return false;
	}
	
	
}