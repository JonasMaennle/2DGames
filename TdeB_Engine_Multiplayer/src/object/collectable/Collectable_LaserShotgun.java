package object.collectable;

import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class Collectable_LaserShotgun extends Collectable_Basic{

	public Collectable_LaserShotgun(int x, int y, int width, int height) {
		super(x, y, width, height);

		image = quickLoaderImage("player/weapon_lasershotgun_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
