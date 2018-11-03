package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import Enity.Entity;
import data.Tile;

import static helpers.Artist.*;
import static helpers.Leveler.*;

import java.util.concurrent.CopyOnWriteArrayList;

public class Weapon implements Entity{
	
	private float x, y, width, height;
	private Player player;
	private Image image_right, image_left;
	private CopyOnWriteArrayList<Laser> list;
	private Sound lastShot;
	
	public Weapon(float x, float y, float width, float height, Player player)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = player;
		this.image_right = quickLoaderImage("player/weapon_right");
		this.image_left = quickLoaderImage("player/weapon_left");
		this.list = new CopyOnWriteArrayList<>();
		try {
			this.lastShot = new Sound("sound/blaster_sound.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// Check Laser - Map collision
	public void update() 
	{
		for(Laser l : list)
		{
			l.update();
			
			for(Tile tile : obstacleList)
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
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+6 + player.getAnim_idleRight().getFrame() * 3.3f, width, height, player.getAnim_idleRight().getFrame() * 2.8f);
			break;
		case "anim_walkRight":
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+5, width, height, 0);
			break;
			
		case "anim_idleLeft":
			drawQuadImageRot(image_left, player.getX()-25, player.getY() + 5 + player.getAnim_idleLeft().getFrame() * 3f, width, height, -player.getAnim_idleLeft().getFrame() * 2.8f);
			break;
		case "anim_walkLeft":
			drawQuadImageRot(image_left, player.getX()-25, player.getY()+5, width, height, 0);
			break;
			
		case "anim_jumpRight":
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+5, width, height, 0);
			break;
		case "anim_jumpLeft":
			drawQuadImageRot(image_left, player.getX()-25, player.getY()+5, width, height, 0);
			break;
		default:
			break;
		}
	}
	
	public void shoot()
	{
		// walk right
		if(player.getDirection().equals("right"))
			list.add(new Laser(player.getX()+60, player.getY()+32, 30, 6, player.getDirection(), 30));
		if(player.getDirection().equals("left"))
			list.add(new Laser(player.getX(), player.getY()+34, 30, 6, player.getDirection(), 30));
		
		lastShot.play();
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

}
