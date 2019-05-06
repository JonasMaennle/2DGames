package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Goal extends Collectable_Basic{

	public Collectable_Goal(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		image = quickLoaderImage("tiles/goal");
	}

	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
