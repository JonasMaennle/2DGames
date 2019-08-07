package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Ammo extends Collectable_Basic{

	private static final long serialVersionUID = -4453262991352948930L;

	public Collectable_Ammo(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/ammo");
	}
	
	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
}
