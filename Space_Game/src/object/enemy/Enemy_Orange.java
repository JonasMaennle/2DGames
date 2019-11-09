package object.enemy;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Graphics.quickLoaderImage;
import static framework.helper.Collection.*;

import org.lwjgl.util.vector.Vector2f;

import framework.core.Handler;
import framework.shader.Light;

public class Enemy_Orange extends Enemy_Basic{

	public Enemy_Orange(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);		
		this.image = quickLoaderImage("enemy/enemy_orange");
		this.light = new Light(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y), 100, 40, 0, 25);
		lights.add(light);
	}
}
