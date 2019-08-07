package object.collectable;
import static framework.helper.Graphics.*;

public class Collectable_LMG extends Collectable_Basic{
	
	private static final long serialVersionUID = -4954129554422707626L;

	public Collectable_LMG(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/weapon_lmg_right");
	}
	
	public void draw(){
		drawQuadImage(image, x, y, width, height);
	}
}
