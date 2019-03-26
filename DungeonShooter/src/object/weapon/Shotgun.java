package object.weapon;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import UI.HeadUpDisplay;
import data.Handler;
import data.ParticleEvent;
import object.Tile;
import object.entity.Player;

import static helpers.Graphics.*;
import static helpers.Leveler.TILES_HEIGHT;
import static helpers.Setup.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

public class Shotgun extends Weapon{
	
	private float angle, destX, destY;
	private Image default_right;
	private CopyOnWriteArrayList<Laser> list;
	private Sound laserShotSound;
	private float laserSpawnX, laserSpawnY;
	
	public Shotgun(float x, float y, float width, float height, Player player, Handler handler, Image left, Image right)
	{
		super(x, y, width, height, player, handler, left, right);
		this.default_right = right; //quickLoaderImage("player/weapon_right");
		this.default_left =  left; //quickLoaderImage("player/weapon_left");
		
		this.list = new CopyOnWriteArrayList<>();
		this.angle = 0;
		
		try {
			this.laserShotSound = new Sound("sound/shotgun_sound.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// Check Laser - Map collision
	public void update() 
	{
		this.x = player.getX() + 32;
		this.y = player.getY() + 25;
		
		for(Laser l : list)
		{
			l.update();
			
			// check range over time
			if(System.currentTimeMillis() - l.getTimeAlive() > 280)
			{
				l.removeLight();
				list.remove(l);
			}
			
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
//			for(Enemy g : handler.enemyList)
//			{
//				
//			}
				
		}
	}

	public void draw() 
	{
		// Draw laser shot	
		for(Laser l : list)
		{
			l.draw();
		}

		drawQuadImageRotLeft(default_right, player.getX(), player.getY(), width, height,angle);
	
	}
	
	public void shoot()
	{
		if(HeadUpDisplay.shotsLeft > 0)HeadUpDisplay.shotsLeft--;
		// walk right

		list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY - 40	, 12, 6, 30, "red", angle)); // top
		list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY - 20	, 12, 6, 30, "red", angle)); // top
		list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY		, 12, 6, 30, "red", angle)); // normal
		list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY + 20	, 12, 6, 30, "red", angle)); // bottom
		list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY + 40	, 12, 6, 30, "red", angle)); // bottom
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
	
	@Override
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

	public CopyOnWriteArrayList<Laser> getList() {
		return list;
	}

	public void setList(CopyOnWriteArrayList<Laser> list) {
		this.list = list;
	}
}
