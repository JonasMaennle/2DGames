package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_HelmetBattery extends Collectable_Basic{

	public Collectable_HelmetBattery(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		image = quickLoaderImage("player/energy_stone");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
