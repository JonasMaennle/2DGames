package object.collectable;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.lights;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

import org.lwjgl.util.vector.Vector2f;

import framework.shader.Light;

public class Collectable_SpeedForWeapon extends Collectable_Basic{

	public Collectable_SpeedForWeapon(int x, int y, int width, int height) {
		super(x, y, width, height);
		light = new Light(new Vector2f(x, y), 25, 0, 24, 8);
		lights.add(light);
		image = quickLoaderImage("player/speed");
	}
	
	public void draw() {
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + height/2));
		drawQuadImage(image, x, y, width, height);
	}
}
