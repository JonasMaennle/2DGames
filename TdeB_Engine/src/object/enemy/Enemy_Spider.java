package object.enemy;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import framework.core.Handler;
import framework.shader.Light;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class Enemy_Spider extends Enemy_Basic{
	
	private Light eyeLight;
	private int eyeX, eyeY;
	private int hpFactor;

	public Enemy_Spider(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Spider_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Spider_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.hpFactor = 4;
		this.hp *= hpFactor;
		
		eyeLight = new Light(new Vector2f(0, 0), 20, 0, 0, 12);
		lights.add(eyeLight);
	}
	
	public void update(){
		super.update();
		
		// calc eyelight position
		if(direction.equals("left")){
			if(moveLeft.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 14);
				eyeY = (int) (y + MOVEMENT_Y + 6);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 14);
				eyeY = (int) (y + MOVEMENT_Y + 10);
			}
		}else{
			if(moveRight.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 18);
				eyeY = (int) (y + MOVEMENT_Y + 10);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 18);
				eyeY = (int) (y + MOVEMENT_Y + 6);
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
		// hp bar
		drawQuadImage(hpBar, x, y - 6, hp / hpFactor, 4);
	}
	
	@Override
	public Vector2f[] getVertices() {
		
		if(direction.equals("right")){
			return new Vector2f[] {
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 9), // left top
					new Vector2f(x + MOVEMENT_X + 5, y + MOVEMENT_Y + 32 ), // left bottom
					new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 32), // right bottom
					new Vector2f(x + MOVEMENT_X + 24, y + MOVEMENT_Y + 5), // right top
					new Vector2f(x + MOVEMENT_X + 13, y + MOVEMENT_Y + 5) // right top
			};
		}else{
			return new Vector2f[] {
					new Vector2f(x + MOVEMENT_X + 9, y + MOVEMENT_Y + 5), // left top
					new Vector2f(x + MOVEMENT_X + 9, y + MOVEMENT_Y + 32), // left bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 32), // right bottom
					new Vector2f(x + MOVEMENT_X + 28, y + MOVEMENT_Y + 9), // right top
					
					new Vector2f(x + MOVEMENT_X + 20, y + MOVEMENT_Y + 5) // right top
			};
		}
	}
	
	@Override
	public void die(){
		lights.remove(eyeLight);
	}
	
	@Override
	public void isPlayerInRange(){
		if(x > handler.getPlayer().getX() - 350 && x < handler.getPlayer().getX() + 350){
			if(y > handler.getPlayer().getY() - 350 && y < handler.getPlayer().getY() + 350){
				speed = 2;
			}
		}else
			return;
	}
}
