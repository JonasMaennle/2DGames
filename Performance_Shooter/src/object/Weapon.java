package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.Entity;
import data.Handler;
import data.Tile;

import static helpers.Artist.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class Weapon implements Entity{
	
	private float x, y, width, height, angle, destX, destY;
	private double hypo;
	private Player player;
	private Image image_right, image_left;
	private CopyOnWriteArrayList<Laser> list;
	private Sound lastShot;
	private Handler handler;
	private float laserSpawnX, laserSpawnY;
	private String currentAnimPlayer;
	
	public Weapon(float x, float y, float width, float height, Player player, Handler handler)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = player;
		this.handler = handler;
		this.currentAnimPlayer = player.getCurrentAnimation();
		this.image_right = quickLoaderImage("player/weapon_right");
		this.image_left = quickLoaderImage("player/weapon_left");
		this.list = new CopyOnWriteArrayList<>();
		this.angle = 0;
		this.hypo = 0;
		
		try {
			this.lastShot = new Sound("sound/blaster_sound.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// Check Laser - Map collision
	public void update() 
	{
		this.x = player.getX() + 32;
		this.y = player.getY() + 25;
		
		currentAnimPlayer = player.getDirection();
		
		for(Laser l : list)
		{
			l.update();
			// Check Laser collision with tiles
			for(Tile tile : handler.obstacleList)
			{
				if(checkCollision(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					list.remove(l);
				}
				if(l.isOutOfMap())
				{
					list.remove(l);
				}
			}
			// Check Laser collision with enemies
			for(GunganEnemy g : handler.gunganList)
			{
				if(checkCollision(g.getX(), g.getY(), g.getWidth(), g.getHeight(), l.getX(), l.getY(), l.getWidth(), l.getHeight()))
				{
					g.damage(25);
					list.remove(l);
				}
			}
		}
	}

	public void draw() 
	{
		// Draw laser shot

//		
//		switch (currentAnimPlayer) {
//		case "left":
//			laserSpawnX = x - 48;
//			laserSpawnY = y;
//			drawQuadImage(image_left, x - 48, y, width, height);
//			break;
//		case "right":
//			laserSpawnX = x;
//			laserSpawnY = y;
//			drawQuadImage(image_right, x, y, width, height);
//			break;
//		default:
//			break;
//		}
		
		switch (player.getCurrentAnimation()) {
		case "anim_idleRight":
			//angle =  player.getAnim_idleRight().getFrame() * 2.8f;
			laserSpawnX = x;
			laserSpawnY = y + 4;
			drawQuadImageRotLeft(image_right, player.getX()+25 - player.getAnim_idleRight().getFrame(), player.getY()+6 + player.getAnim_idleRight().getFrame() * 2f, width, height,angle);
			break;		
		case "anim_walkRight":
			laserSpawnX = x;
			laserSpawnY = y + 4;
			drawQuadImageRotLeft(image_right, player.getX()+25, player.getY()+ 6 , width, height, angle);
			break;
		case "anim_jumpRight":
			laserSpawnX = x;
			laserSpawnY = y + 4;
			drawQuadImageRotLeft(image_right, player.getX()+25, player.getY()+6, width, height, angle);
			break;
			
		case "anim_idleLeft":
			//angle = -player.getAnim_idleLeft().getFrame() * 2.8f;
			laserSpawnX = x;
			laserSpawnY = y + 4;
			drawQuadImageRotRight(image_left, player.getX()-80, player.getY() + 6, width, height, angle+180);
			break;
		case "anim_walkLeft":
			//angle = 0;
			drawQuadImageRotRight(image_left, player.getX()-80, player.getY()+6, width, height, angle + 180);
			break;
		case "anim_jumpLeft":
			//angle = 0;
			drawQuadImageRotRight(image_left, player.getX()-80, player.getY()+6, width, height, angle + 180);
			break;
		default:
			break;
		}
		
		for(Laser l : list)
		{
			l.draw();
		}
	}
	
	public void shoot()
	{
		// walk right
		if(player.getDirection().equals("right"))
			list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 30, 6, player.getDirection(), 30, "red", angle));
		if(player.getDirection().equals("left"))
			list.add(new Laser(laserSpawnX, laserSpawnY, destX, destY, 30, 6, player.getDirection(), 30, "red", angle));
		
		lastShot.play();
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	public void calcAngle(float destX, float destY)
	{
		this.destX = destX;
		this.destY = destY;
		angle = -(float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
		//System.out.println("Angle: " + angle);
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

}
