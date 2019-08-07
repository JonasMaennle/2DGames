package object.collectable;

import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class Collectable_Minigun extends Collectable_Basic{

	private static final long serialVersionUID = 1084506539607759294L;

	public Collectable_Minigun(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/weapon_minigun_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
