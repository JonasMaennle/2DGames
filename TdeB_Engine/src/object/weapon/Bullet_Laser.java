package object.weapon;
import static framework.helper.Graphics.*;

import org.lwjgl.util.vector.Vector2f;
import static framework.helper.Collection.*;

import framework.helper.Color;
import framework.shader.Light;

public class Bullet_Laser extends Bullet_Basic{

	public Bullet_Laser(float x, float y, int width, int height, float destX, float destY, String direction, int speed, float angle, Color color) {
		super(x, y, width, height, destX, destY, direction, speed, angle);
		image = quickLoaderImage("player/laser_red");
		this.light = new Light(new Vector2f(0, 0), color.getR(), color.getG(), color.getB(), 20);
		lights.add(light);
	}
	
	public void update(){
		super.update();
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + 6));
	}
}
