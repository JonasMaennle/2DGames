package object.collectable;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.lights;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

import org.lwjgl.util.vector.Vector2f;

import framework.shader.Light;

public class Collectable_Shield extends Collectable_Basic{

	public Collectable_Shield(int x, int y, int width, int height) {
		super(x, y, width, height);
		image = quickLoaderImage("player/shield");
		light = new Light(new Vector2f(x, y), 0, 100, 95, 18);
		lights.add(light);
	}
	
	public void draw() {
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + height/2));
		drawQuadImage(image, x, y, width, height);
	}
}
