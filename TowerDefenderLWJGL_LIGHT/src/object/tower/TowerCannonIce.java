package object.tower;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;

import data.Tile;
import data.Tower;
import data.TowerType;
import object.enemy.Enemy;
import shader.Light;

public class TowerCannonIce extends Tower{

	public TowerCannonIce(TowerType type, Tile startTile, CopyOnWriteArrayList<Enemy> enemies) 
	{
		super(type, startTile, enemies);
		super.light = new Light(new Vector2f(getX() + getWidth()/2, getY() + getHeight()/2), (153.0f / 255.0f)*10.0f, (244.0f / 255.0f)*10.0f, (249.0f / 255.0f)*10.0f, 50);
	}

	@Override
	public void shoot(Enemy target) {
		super.projectiles.add(new ProjectileIceball(super.type.projectileType, super.target, super.getX(), super.getY(), 32, 32, new Light(new Vector2f(super.getX() + super.getWidth()/2, super.getY() + super.getHeight() / 2), (153.0f / 255.0f)*10.0f, (244.0f / 255.0f)*10.0f, (249.0f / 255.0f)*10.0f, 50)));
		super.target.setSpeed(super.target.getSpeed() * 0.75f);
		super.target.reduceHiddenHealth(super.type.projectileType.damage);
	}
}
