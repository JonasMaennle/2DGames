package object.collectable;

import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class Collectable_Icethrower extends Collectable_Basic{

	private static final long serialVersionUID = -4851460615430020599L;

	public Collectable_Icethrower(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/weapon_icethrower_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
