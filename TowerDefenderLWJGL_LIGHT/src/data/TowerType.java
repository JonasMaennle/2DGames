package data;

import org.newdawn.slick.opengl.Texture;

import static helpers.Artist.*;

public enum TowerType {
	
	CannonRed(new Texture[]{quickLoad("cannonBaseRed"), quickLoad("cannonGunRed")}, ProjectileType.CannonballRed, 3, 1000, 0.750f, 10),
	CannonBlue(new Texture[]{quickLoad("cannonBaseBlue"), quickLoad("cannonGunBlue")}, ProjectileType.CannonballBlue, 10, 1000, 2, 25),
	CannonIce(new Texture[]{quickLoad("cannonBaseIce"), quickLoad("cannonGunIce")}, ProjectileType.Iceball, 10, 1000, 3, 50),
	CannonYellow(new Texture[]{quickLoad("cannonBaseYellow"), quickLoad("cannonGunYellow")}, ProjectileType.CannonballYellow, 10, 1000, 2, 30);
	
	Texture[] texture;
	int damage, range, cost;
	float firingSpeed;
	public ProjectileType projectileType;
	
	TowerType(Texture[] texture, ProjectileType projectileType, int damage, int range, float firingSpeed, int cost)
	{
		this.texture = texture;
		this.projectileType = projectileType;
		this.damage = damage;
		this.range = range;
		this.firingSpeed = firingSpeed;
		this.cost = cost;
	}
}
