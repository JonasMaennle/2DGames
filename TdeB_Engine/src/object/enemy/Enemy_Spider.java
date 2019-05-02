package object.enemy;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class Enemy_Spider extends Enemy_Basic{
	
	private Image vertLeft_0, vertLeft_1, vertRight_0, vertRight_1;

	public Enemy_Spider(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Spider_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Spider_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.vertLeft_0 = quickLoaderImage("enemy/Enemy_Spider_Basic_Left_0");
		this.vertRight_0 = quickLoaderImage("enemy/Enemy_Spider_Basic_Right_0");
		this.vertLeft_1 = quickLoaderImage("enemy/Enemy_Spider_Basic_Left_1");
		this.vertRight_1 = quickLoaderImage("enemy/Enemy_Spider_Basic_Right_1");
	
	}
	
	public void update(){
		super.update();
	}
	
	public void draw(){	
		if(direction.equals("right")){
			drawAnimation(moveRight, x, y, width, height);
		}else{
			drawAnimation(moveLeft, x, y, width, height);
		}
	}
	
	@Override
	public Vector2f[] getVertices() {
		if(direction.equals("right")){
			if(moveRight.getFrame() == 0)
				return getImageVertices((int)x, (int)y, vertRight_0);
			else
				return getImageVertices((int)x, (int)y, vertRight_1);
		}else{
			if(moveLeft.getFrame() == 0)
				return getImageVertices((int)x, (int)y, vertLeft_1);
			else
				return getImageVertices((int)x, (int)y, vertLeft_0);
		}
	}
}
