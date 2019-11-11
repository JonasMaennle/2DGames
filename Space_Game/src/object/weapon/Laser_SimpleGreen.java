package object.weapon;

import framework.entity.LaserType;
import framework.shader.Light;

public class Laser_SimpleGreen extends Laser_Basic{

	public Laser_SimpleGreen(float x, float y, int width, int height, float velX, float velY, int speed, float angle, Light light) {
		super(x, y, width, height, velX, velY, speed, angle, light);

		this.laserType = LaserType.SIMPLE_GREEN;
	}
}
