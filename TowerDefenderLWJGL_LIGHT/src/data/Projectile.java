package data;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import object.enemy.Enemy;
import shader.Light;

import static helpers.Clock.*;
import static helpers.Artist.*;

public abstract class Projectile implements Entity{
	
	private Texture texture;
	private float x, y, speed, xVelocity, yVelocity;
	private int width, height;
	private int damage;
	private Enemy target;
	private boolean alive;
	protected Light light;
	
	public Projectile(ProjectileType type, Enemy target, float x, float y, int width, int height)
	{
		this.texture = type.texture;
		this.x = x;
		this.y = y;
		this.speed = type.speed;
		this.damage = type.damage;
		this.target = target;
		this.width = width;
		this.height = height;
		this.alive = true;
		this.xVelocity = 0;
		this.yVelocity = 0;
		
		calculateDirection();
	}
	
	private void calculateDirection()
	{
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(target.getX() - x - TILE_SIZE / 4 + TILE_SIZE/2);
		float yDistanceFromTarget = Math.abs(target.getY() - y - TILE_SIZE / 4 + TILE_SIZE/2);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		xVelocity = xPercentOfMovement;
		yVelocity = totalAllowedMovement - xPercentOfMovement;
		
		// set direction based on position of target relative to tower
		if(target.getX() < x)
			xVelocity *= -1;
		
		if(target.getY() < y)
			yVelocity *= -1;	
	}
	
	// deals damage to enemy
	public void damage()
	{
		target.damage(damage);
		alive = false;
	}
	
	public void update()
	{
		if(alive)
		{	
			calculateDirection();
			x += xVelocity * speed * delta();
			y += yVelocity * speed * delta();
			// set light location
			if(light != null)
				light.setLocation(new Vector2f(x + width / 2, y + height / 2));
			
			if(checkCollision(x, y, width, height, target.getX(), target.getY(), target.getWidth(), target.getHeight()))
			{
				damage();
			}
			
			if(isProjectileOutOfMap())
				alive = false;
			
			draw();
		}else{
			if(light != null)
				lights.remove(light);
		}
	}
	
	public void draw()
	{
		drawQuadTex(texture, x, y, width, height);
	}
	
	private boolean isProjectileOutOfMap()
	{
		if(x > WIDTH || x < 0 || y > HEIGHT || y < 0)
			return true;
		return false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isAlive() {
		return alive;
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
	
	public Enemy getTarget()
	{
		return target;
	}
	
	public void setAlive(boolean status)
	{
		this.alive = status;
	}
}
