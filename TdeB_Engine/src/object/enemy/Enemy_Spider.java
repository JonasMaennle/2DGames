package object.enemy;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.shader.Light;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

public class Enemy_Spider extends Enemy_Basic{
	
	private Light eyeLight;
	private int eyeX, eyeY;
	private int hpFactor;
	Image img;

	public Enemy_Spider(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Spider_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Spider_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.hpFactor = 4;
		this.hp *= hpFactor;
		this.eyeX = (int) x;
		this.eyeY = (int) y;
		
		eyeLight = new Light(new Vector2f(x, y), 20, 0, 0, 20);
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
		
		if(direction.equals("left")){
			
			if(moveLeft.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 18),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 11),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 0)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 0, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 4)
				};
			}
		}else{
			if(moveRight.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 4)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 0),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 4),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 11),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 17),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 31),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 24),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 15),
						new Vector2f(x + MOVEMENT_X + 31, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 0)
				};
			}
		}
	}
	
	@Override
	public void die(){
		lights.remove(eyeLight);
	}
	
	@Override
	public void isPlayerInRange(int borderOffset){
		if(x > handler.getPlayer().getX() - 350 && x < handler.getPlayer().getX() + 350){
			if(y > handler.getPlayer().getY() - 350 && y < handler.getPlayer().getY() + 350){
				speed = 2;
			}
		}else
			return;
	}
}
