package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Ammo extends Collectable_Basic{

	public Collectable_Ammo(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/ammo");
	}
	
	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
}
