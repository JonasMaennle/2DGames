package object.player;
import static framework.helper.Graphics.*;

public class Bullet_Laser extends Bullet_Basic{

	public Bullet_Laser(float x, float y, int width, int height, float destX, float destY, String direction, int speed, float angle) {
		super(x, y, width, height, destX, destY, direction, speed, angle);
		image = quickLoaderImage("player/laser_red");
	}

}
