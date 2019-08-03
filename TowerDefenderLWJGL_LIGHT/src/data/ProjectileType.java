package data;

import org.newdawn.slick.opengl.Texture;
import static helpers.Artist.*;

public enum ProjectileType {

	CannonballBlue(quickLoad("cannonBulletBlue"), 10, 750),
	CannonballRed(quickLoad("cannonBulletRed"), 3, 900),
	CannonballYellow(quickLoad("cannonBulletRed"), 20, 850),
	Iceball(quickLoad("cannonBulletIce"), 5, 450);
	
	Texture texture;
	public int damage;
	float speed;
	
	ProjectileType(Texture texture, int damage, float speed)
	{
		this.texture = texture;
		this.damage = damage;
		this.speed = speed;
	}
}
