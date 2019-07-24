package object.player;

import java.awt.Rectangle;

import static framework.helper.Graphics.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import framework.core.Handler;
import framework.entity.GameEntity;

public class Bomb implements GameEntity{

	private static final long serialVersionUID = 2438651146987353673L;

	private int x, y, width, height;
	private transient Animation anim;
	private Explosion explosion;
	private Handler handler;
	private boolean delete;
	private long timer;
	private boolean explosion_hasStarted;
	
	public Bomb(int x, int y, Handler handler){
		this.x = x;
		this.y = y;
		this.width = 32;
		this.height = width;
		this.handler = handler;
		this.delete = false;
		this.explosion_hasStarted = false;
		
		this.anim = new Animation(loadSpriteSheet("player/bomb", 32, 32), 100);
	}
	
	@Override
	public void update() {
		
		if(explosion != null)
			explosion.update();
	}

	@Override
	public void draw() {
		if(anim == null)
			this.anim = new Animation(loadSpriteSheet("player/bomb", 32, 32), 100);
		
		anim.stopAt(15);
		if(anim.getFrame() < 15)
			drawAnimation(anim, x, y, width, height);
		else if(!explosion_hasStarted){
			explosion_hasStarted = true;
			this.explosion = new Explosion(x, y, 2, handler);
			this.timer = System.currentTimeMillis();
		}
			
		
		if(explosion != null) {
			if(System.currentTimeMillis() - timer < 1000) {
				explosion.draw();
			}
			else
				delete = true;
		}
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setX(float x) {
		this.x = (int)x;
	}

	@Override
	public void setY(float y) {
		this.y = (int)y;
	}

	@Override
	public Vector2f[] getVertices() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x,y,width,height);
	}

	@Override
	public Rectangle getTopBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
