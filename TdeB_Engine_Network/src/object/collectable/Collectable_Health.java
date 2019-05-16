package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Health extends Collectable_Basic{

	public Collectable_Health(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/hp_stone");
	}
	
	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
}
