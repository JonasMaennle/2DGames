package object.tower;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;

import data.Tile;
import data.Tower;
import data.TowerType;
import object.enemy.Enemy;
import shader.Light;

public class TowerCannonBlue extends Tower{
	
	public TowerCannonBlue(TowerType type, Tile startTile, CopyOnWriteArrayList<Enemy> enemies) 
	{
		super(type, startTile, enemies);
		super.light = new Light(new Vector2f(getX() + getWidth()/2, getY() + getHeight()/2), 0, 0, 10, 50);
	}

	@Override
	public void shoot(Enemy target) 
	{
		super.projectiles.add(new ProjectileCannonballBlue(super.type.projectileType, super.target, super.getX(), super.getY(), 32, 32, new Light(new Vector2f(super.getX(), super.getY()), 0, 2, 9, 10)));
		super.target.reduceHiddenHealth(super.type.projectileType.damage);
	}
}
