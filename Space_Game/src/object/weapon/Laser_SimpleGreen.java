package object.weapon;

import static framework.helper.Collection.PLAYER_HP;
import static framework.helper.Collection.PLAYER_HP_BLOCKS;
import static framework.helper.Graphics.*;

import framework.core.Handler;
import framework.entity.LaserType;
import framework.shader.Light;

public class Laser_SimpleGreen extends Laser_Basic{

	public Laser_SimpleGreen(float x, float y, int width, int height, float velX, float velY, int speed, float angle, Light light, Handler handler) {
		super(x, y, width, height, velX, velY, speed, angle, light, handler);
		this.image = quickLoaderImage("enemy/bullet_green");
		this.laserType = LaserType.SIMPLE_GREEN;
	}
	
	public void update() {
		super.update();
		if(handler.getPlayer().getBounds().intersects(getBounds()) && handler.getPlayer().getCollectable_Spike() == null) {
			PLAYER_HP -= laserType.getDamage();
			if(PLAYER_HP < 0 && PLAYER_HP_BLOCKS > 0) {
				PLAYER_HP = 32;
				PLAYER_HP_BLOCKS -= 1;
			}
			dead = true;
		}
	}
	
	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
}
