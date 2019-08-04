package object.tower;

import org.lwjgl.util.vector.Vector2f;
import static helpers.Artist.*;

import data.Projectile;
import data.ProjectileType;
import object.enemy.Enemy;
import shader.Light;

public class ProjectileCannonballBlue extends Projectile{
	
	private float x, y;
	private int width, height;
	
	public ProjectileCannonballBlue(ProjectileType type, Enemy target, float x, float y, int width, int height, Light light) 
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