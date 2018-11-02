package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;

import static helpers.Artist.*;

public class Weapon implements Entity{
	
	private float x, y, width, height;
	private Player player;
	private Image image_right, image_left;
	
	public Weapon(float x, float y, float width, float height, Player player)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.player = player;
		this.image_right = quickLoaderImage("player/weapon_right");
		this.image_left = quickLoaderImage("player/weapon_left");
	}

	public void update() 
	{
		
	}

	public void draw() 
	{
		switch (player.getCurrentAnimation()) {
		case "anim_idleRight":
			drawQuadImageRot(image_right, player.getX()+28, player.getY() + player.getAnim_idleRight().getFrame() * 3.3f, width, height, player.getAnim_idleRight().getFrame() * 2.8f);
			break;
		case "anim_walkRight":
			drawQuadImageRot(image_right, player.getX()+35, player.getY()+5, width, height, 0);
			break;
			
		case "anim_idleLeft":
			drawQuadImageRot(image_left, player.getX()-18, player.getY() + player.getAnim_idleLeft().getFrame() * 3f, width, height, -player.getAnim_idleLeft().getFrame() * 2.8f);
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
