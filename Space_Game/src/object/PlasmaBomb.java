package object;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.geom.Circle;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.shader.Light;
import object.enemy.Enemy_Basic;

import static framework.helper.Collection.*;

public class PlasmaBomb implements GameEntity{
	
	private float x, y;
	private Light light;
	private float radius;
	private boolean exploded;
	private long t1, t2;
	private int size;
	private Handler handler;
	
	public PlasmaBomb(float x2, float y2, int size, Handler handler) {
		this.x = x2;
		this.y = y2;
		this.exploded = false;
		this.handler = handler;
		this.size = size;

		this.radius = 50;
		this.light = new Light(new Vector2f(0,0), 200, 200, 255, radius);
		lightsTopLevel.add(light);
	}

	@Override
	public void update() {
		if(radius >= 1.25f) {
			radius *= 0.96f;
		}else {
			lightsTopLevel.remove(light);
			exploded = true;
			
			// damage enemies in range
			for(Enemy_Basic enemy : handler.getEnemyList()) {
				if(getBounds().intersects(enemy.getBounds())) {
					enemy.setHp(enemy.getHp() - 64);
				}
			}
		}

		this.light.setRadius(radius);
		this.light.setLocation(new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y));
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x - size / 2,(int) y - size / 2, size, size);
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

	public boolean isExploded() {
		return exploded;
	}

	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}

