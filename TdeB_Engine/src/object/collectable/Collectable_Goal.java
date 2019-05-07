package object.collectable;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

import org.lwjgl.util.vector.Vector2f;

import framework.shader.Light;

public class Collectable_Goal extends Collectable_Basic{
	
	private Light light;

	public Collectable_Goal(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		image = quickLoaderImage("tiles/goal");
		light = new Light(new Vector2f(0, 0), 50, 30, 2, 8);
		lights.add(light);
	}

	public void draw(){
		light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + height/2));
		drawQuadImage(image, x, y, width, height);
	}
}
