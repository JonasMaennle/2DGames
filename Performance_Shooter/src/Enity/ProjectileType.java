package Enity;

import org.newdawn.slick.Image;
import static helpers.Graphics.*;

public enum ProjectileType {

	LaserSmall(quickLoaderImage("laser_small"), 10, 600);
	
	public Image image;
	public int damage;
	public float speed;
	
	ProjectileType(Image image, int damage, float speed)
	{
		this.image = image;
		this.damage = damage;
		this.speed = speed;
	}
}
