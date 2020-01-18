package object.weapon;

import framework.core.Handler;
import framework.entity.LaserType;
import framework.shader.Light;

public class Laser_SimpleBlue extends Laser_Basic{

	public Laser_SimpleBlue(float x, float y, int width, int height, float velX, float velY, int speed, float angle, Light light, Handler handler) {
		super(x, y, width, height, velX, velY, speed, angle, light, handler);

		this.laserType = LaserType.SIMPLE_BLUE;
	}
}
