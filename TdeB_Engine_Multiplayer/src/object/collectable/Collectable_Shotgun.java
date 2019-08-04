package object.collectable;


import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class Collectable_Shotgun extends Collectable_Basic{

	public Collectable_Shotgun(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/weapon_shotgun_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
