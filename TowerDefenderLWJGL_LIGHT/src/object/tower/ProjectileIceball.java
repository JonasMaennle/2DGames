package object.tower;

import org.lwjgl.util.vector.Vector2f;
import static helpers.Artist.*;

import data.Projectile;
import data.ProjectileType;
import object.enemy.Enemy;
import shader.Light;

public class ProjectileIceball extends Projectile{
	
	private float x, y;
	private int width, height;
	
	public ProjectileIceball(ProjectileType type, Enemy target, float x, float y, int width, int height, Light light) 
	{
		super(type, target, x, y, width, height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		super.light = light;
		lights.add(light);
	}

	@Override
	public void damage()
	{
		if(super.getTarget().getSpeed() >= 50)
			super.getTarget().setSpeed(super.getTarget().getSpeed() * 0.75f);
		super.damage();
	}
	
	@Override
	public Vector2f[] getVertices() 
	{
		return  new Vector2f[] {
				new Vector2f(x, y),
				new Vector2f(x, y + height),
				new Vector2f(x + width, y + height),
				new Vector2f(x + width, y)
		};
	}
}
