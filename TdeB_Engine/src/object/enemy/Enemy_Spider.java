package object.enemy;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.shader.Light;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class Enemy_Spider extends Enemy_Basic{
	
	private Image vertLeft_0, vertLeft_1, vertRight_0, vertRight_1;
	private Light eyeLight;
	private int eyeX, eyeY;

	public Enemy_Spider(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Spider_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Spider_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.vertLeft_0 = quickLoaderImage("enemy/Enemy_Spider_Basic_Left_0");
		this.vertRight_0 = quickLoaderImage("enemy/Enemy_Spider_Basic_Right_0");
		this.vertLeft_1 = quickLoaderImage("enemy/Enemy_Spider_Basic_Left_1");
		this.vertRight_1 = quickLoaderImage("enemy/Enemy_Spider_Basic_Right_1");
		eyeLight = new Light(new Vector2f(0, 0), 10, 0, 0, 25);
		lights.add(eyeLight);
	
	}
	
	public void update(){
		super.update();
		
		// calc eyelight position
		if(direction.equals("left")){
			if(moveLeft.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 13);
				eyeY = (int) (y + MOVEMENT_Y + 5);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 13);
				eyeY = (int) (y + MOVEMENT_Y + 9);
			}
		}else{
			if(moveRight.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 17);
				eyeY = (int) (y + MOVEMENT_Y + 9);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 17);
				eyeY = (int) (y + MOVEMENT_Y + 5);
			}
		}
		eyeLight.setLocation(new Vector2f(eyeX, eyeY));
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
			if(moveRight.getFrame() == 0){
				return getImageVertices((int)x, (int)y, vertRight_0);
			}
			else{
				return getImageVertices((int)x, (int)y, vertRight_1);
			}
		}else{
			if(moveLeft.getFrame() == 0){
				return getImageVertices((int)x, (int)y, vertLeft_1);
			}		
			else{
				return getImageVertices((int)x, (int)y, vertLeft_0);
			}			
		}
	}
	
	@Override
	public void die(){
		lights.remove(eyeLight);
	}
}
