package object.tower;

import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.util.vector.Vector2f;

import data.Tile;
import data.Tower;
import data.TowerType;
import object.enemy.Enemy;
import shader.Light;

public class TowerCannonYellow extends Tower{
	
	public TowerCannonYellow(TowerType type, Tile startTile, CopyOnWriteArrayList<Enemy> enemies) 
	{
		super(type, startTile, enemies);
		super.light = new Light(new Vector2f(getX() + getWidth()/2, getY() + getHeight()/2), 10, 10, 0, 50);
	}

	@Override
	public void shoot(Enemy target)
	{
		super.projectiles.add(new ProjectileCannonballYellow(super.type.projectileType, super.target, super.getX(), super.getY(), 32, 32, new Light(new Vector2f(super.getX(), super.getY()), 10, 10, 0, 10)));
		super.target.reduceHiddenHealth(super.type.projectileType.damage);
	}
	
	@Override
	public Vector2f[] getVertices() 
	{
		return new Vector2f[] {
				new Vector2f(this.getX() + 10, this.getY() + 13), // left top
				new Vector2f(this.getX() + 10, this.getY() + 53), // left bottom
				new Vector2f(this.getX() + 55, this.getY() + 53), // right bottom
				new Vector2f(this.getX() + 55, this.getY() + 13) // right top
		};
	}
}
