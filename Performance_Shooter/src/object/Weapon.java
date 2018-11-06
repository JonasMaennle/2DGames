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
	
	private float x, y, width, height, angle;
	private double hypo;
	private Player player;
	private Image image_right, image_left;
	private CopyOnWriteArrayList<Laser> list;
	private Sound lastShot;
	private Handler handler;
	
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
		this.x = player.getX();
		this.y = player.getY();
		
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
		for(Laser l : list)
		{
			l.draw();
		}
		
		switch (player.getCurrentAnimation()) {
		case "anim_idleRight":
			//angle =  player.getAnim_idleRight().getFrame() * 2.8f;
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+6 + player.getAnim_idleRight().getFrame() * 3.3f, width, height,angle);
			break;
		case "anim_walkRight":
			//angle = 0;
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+5, width, height, angle);
			break;
			
		case "anim_idleLeft":
			//angle = -player.getAnim_idleLeft().getFrame() * 2.8f;
			drawQuadImageRot(image_left, player.getX()-25, player.getY() + 5 + player.getAnim_idleLeft().getFrame() * 3f, width, height, angle+180);
			break;
		case "anim_walkLeft":
			//angle = 0;
			drawQuadImageRot(image_left, player.getX()-25, player.getY()+5, width, height, angle + 180);
			break;
			
		case "anim_jumpRight":
			//angle = 0;
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+5, width, height, angle);
			break;
		case "anim_jumpLeft":
			//angle = 0;
			drawQuadImageRot(image_left, player.getX()-25, player.getY()+5, width, height, angle + 180);
			break;
		default:
			break;
		}
	}
	
	public void shoot(float destX, float destY)
	{
		calcAngle(destX, destY);
		// walk right
		if(player.getDirection().equals("right"))
			list.add(new Laser(player.getX()+60, player.getY()+32, destX, destY, 30, 6, player.getDirection(), 30, "red", angle));
		if(player.getDirection().equals("left"))
			list.add(new Laser(player.getX(), player.getY()+34, destX, destY, 30, 6, player.getDirection(), 30, "red", angle));
		
		lastShot.play();
	}
	
	private void calcAngle(float destX, float destY)
	{
		//System.out.println(destY + " " + y);
		angle = -(float) Math.toDegrees(Math.atan2(destY - (y+64), destX - (x+32)));

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
