package object.player;
import static framework.helper.Graphics.*;

import org.lwjgl.util.vector.Vector2f;
import static framework.helper.Collection.*;

import framework.shader.Light;

public class Bullet_Laser extends Bullet_Basic{

	public Bullet_Laser(float x, float y, int width, int height, float destX, float destY, String direction, int speed, float angle) {
		super(x, y, width, height, destX, destY, direction, speed, angle);
		image = quickLoaderImage("player/laser_red");
		
		this.light = new Light(new Vector2f(0, 0), 20, 0, 0, 15);
		lights.add(light);
	}
	
	public void update(){
		super.update();
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + 6));
	}

}
