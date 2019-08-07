package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_Flamethrower extends Collectable_Basic{
	
	private static final long serialVersionUID = -7370607442180278426L;

	public Collectable_Flamethrower(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		image = quickLoaderImage("player/weapon_flamethrower_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
