package object.weapon;

import static framework.helper.Graphics.quickLoaderImage;

import framework.shader.Light;

public class Laser_SimpleBlue extends Laser_Basic{

	public Laser_SimpleBlue(float x, float y, int width, int height, float velX, float velY, int speed, float angle, Light light) {
		super(x, y, width, height, velX, velY, speed, angle, light);

		this.image = quickLoaderImage("player/bullet_blue");
	}
	
	@Override
	public void draw() {
		// draw nothing
	}
}
