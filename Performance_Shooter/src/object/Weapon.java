package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.Entity;
import Enity.TileType;
import data.Handler;
import data.ParticleEvent;
import data.Tile;

import static helpers.Graphics.*;
import static helpers.Leveler.TILES_HEIGHT;
import static helpers.Setup.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

public class Weapon implements Entity{
	
	private float x, y, width, height, angle, destX, destY;
	private Player player;
	private Image image_right, image_left;
	private CopyOnWriteArrayList<Laser> list;
	private Sound laserShotSound;
	private Handler handler;
	private float laserSpawnX, laserSpawnY;
	
	public Weapon(float x, float y, float width, float height, Player player, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = player;
		this.handler = handler;
		this.image_right = quickLoaderImage("player/weapon_right");
		this.image_left = quickLoaderImage("player/weapon_left");
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
		this.x = player.getX() + 32;
		this.y = player.getY() + 25;
		
		for(Laser l : list)
		{
			l.update();
			// Check Laser collision with tiles
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					list.remove(l);
					if(tile.getType() == TileType.Rock_Basic)handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 10, "gray", "normal"));
					if(tile.getType() != TileType.Rock_Basic)handler.addParticleEvent(new ParticleEvent((int)tile.getX() + TILE_SIZE / 2, (int)tile.getY() + TILE_SIZE / 2, 10, "brown", "normal"));
				}
				if(l.isOutOfMap())
				{
					list.remove(l);
				}
			}
			// Check Laser collision with enemies
			for(Enemy g : handler.enemyList)
			{
				if(checkCollision(g.getX(), g.getY(), g.getWidth(), g.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					g.damage(14); // Gungan got 56 HP
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
		
		switch (player.getCurrentAnimation()) {
		case "anim_idleRight":
			//angle =  player.getAnim_idleRight().getFrame() * 2.8f;
			laserSpawnX = x;
			laserSpawnY = y + 6;
			drawQuadImageRotLeft(image_right, player.getX()+25 - player.getAnim_idleRight().getFrame(), player.getY()+6 + player.getAnim_idleRight().getFrame() * 2f, width, height,angle);
			break;		
		case "anim_walkRight":
			laserSpawnX = x;
			laserSpawnY = y + 6;
			drawQuadImageRotLeft(image_right, player.getX()+25, player.getY()+ 6 , width, height, angle);
			break;
		case "anim_jumpRight":
			laserSpawnX = x;
			laserSpawnY = y + 6;
			drawQuadImageRotLeft(image_right, player.getX()+25, player.getY()+6, width, height, angle);
			break;
			
		case "anim_idleLeft":
			//angle = -player.getAnim_idleLeft().getFrame() * 2.8f;
			laserSpawnX = x - 30;
			laserSpawnY = y + 6;
			drawQuadImageRotRight(image_left, player.getX()-92 + player.getAnim_idleRight().getFrame(), player.getY() + 6 + player.getAnim_idleLeft().getFrame() * 2f, width, height, angle-180);
			break;
		case "anim_walkLeft":
			laserSpawnX = x - 30;
			laserSpawnY = y + 6;
			drawQuadImageRotRight(image_left, player.getX()-92, player.getY()+6, width, height, angle - 180);
			break;
		case "anim_jumpLeft":
			laserSpawnX = x - 30;
			laserSpawnY = y + 6;
			drawQuadImageRotRight(image_left, player.getX()-92, player.getY()+6, width, height, angle - 180);
			break;
		default:
			break;
		}
	}
	
	public void shoot()
	{
		// walk right
		if(player.getDirection().equals("right") && destX > laserSpawnX)
		{
			if(destX < x + (width/2))
			{
				destX = getRightBorder() - destX;
			}
			list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 24, 4, 30, "red", angle));
			laserShotSound.play();
		}
		
		// walk left
		if(player.getDirection().equals("left") && destX < laserSpawnX)
		{
			if(destX > x + (width/2))
			{
				destX = getLeftBorder() + destX;
			}
			list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 24, 4, 30, "red", angle));
			laserShotSound.play();
		}
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	public void calcAngle(float destX, float destY)
	{
		angle = (float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
	    // block angle 320 - 360 && 0 - 30
	    if(player.getDirection().equals("right"))
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
	    if(player.getDirection().equals("left"))
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
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
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
	public void damage(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOutOfMap() {
		if((TILES_HEIGHT * TILE_SIZE) < y)
			return true;
		return false;
	}
}
