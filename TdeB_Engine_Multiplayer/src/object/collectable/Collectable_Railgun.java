package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Railgun extends Collectable_Basic{

	private static final long serialVersionUID = 6896615003574066747L;

	public Collectable_Railgun(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/weapon_railgun_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
