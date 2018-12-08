package object;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import Enity.Entity;
import object.entity.Player;

import static helpers.Graphics.*;

public class Jetpack implements Entity{

	private float x, y, fuel, gravity;
	private int width, height;
	private Image image_left,image_fuel, image_background;
	private Image image_right;
	private String direction;
	private Player player;
	private boolean isFlying;
	private final float FUEL_DRAIN = 0.02f;
	private Animation fire_left, fire_right;
	
	public Jetpack(float x, float y, Player player)
	{
		this.x = x;
		this.y = y;
		this.width = 64;
		this.height = 128;
		this.direction = player.getDirection();
		this.player = player;
		this.fuel = 20;
		this.isFlying = false;
		
		this.image_right = quickLoaderImage("player/jetpack_right");
		this.image_left = quickLoaderImage("player/jetpack_left");
		this.image_fuel = quickLoaderImage("player/jetpack_fuel");
		this.image_background = quickLoaderImage("player/jetpack_background");
		
		this.fire_left = new Animation(loadSpriteSheet("player/jetpack_fire_left", 64, 128), 50);
		this.fire_right = new Animation(loadSpriteSheet("player/jetpack_fire_right", 64, 128), 50);
	}
	
	public void update() 
	{
		direction = player.getDirection();
		x = player.getX();
		y = player.getY();
		
		if(isFlying)
		{
			if(fuel > 0)
				fuel -= FUEL_DRAIN;
		}
		
		if(fuel <= 0)
		{
			isFlying = false;
			player.setVelY(4);
		}
	}
	
	public void fly()
	{
		isFlying = true;
		if(gravity > -2)
			gravity -= 0.025f;
		player.setVelY(gravity);
	}
	
	public void fallDown()
	{
		if(gravity < 4)
		{
			gravity += 0.025f;
		}
		player.setVelY(gravity);
	}

	public void draw() 
	{
		if(direction.equals("left"))
		{
			if(isFlying)
				drawAnimation(fire_right, x, y, width, height);
			drawQuadImage(image_background, x + 58, y + 24, image_background.getWidth(), 20);
			drawQuadImage(image_fuel, x + 58, y + 24, image_background.getWidth(), fuel);
			drawQuadImage(image_right, x, y, width, height);
		}else if(direction.equals("right"))
		{
			if(isFlying)
				drawAnimation(fire_left, x, y, width, height);
			drawQuadImage(image_background, x + 4, y + 24, image_background.getWidth(), 20);
			drawQuadImage(image_fuel, x + 4, y + 24, image_background.getWidth(), fuel);
			drawQuadImage(image_left, x, y, width, height);
		}
	}

	public float getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = fuel;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}
}
