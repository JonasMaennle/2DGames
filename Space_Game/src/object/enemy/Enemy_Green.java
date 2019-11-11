package object.enemy;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.lights;
import static framework.helper.Graphics.quickLoaderImage;

import org.lwjgl.util.vector.Vector2f;

import framework.core.Handler;
import framework.entity.EnemyType;
import framework.shader.Light;
import object.weapon.Laser_SimpleGreen;

public class Enemy_Green extends Enemy_Basic{

	private long t1, t2;
	
	public Enemy_Green(int x, int y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		this.image = quickLoaderImage("enemy/enemy_green");
		this.enemyType = EnemyType.ENEMY_GREEN;
		this.light = new Light(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y), enemyType.getEnemyColor().getRed(), enemyType.getEnemyColor().getGreen(), enemyType.getEnemyColor().getBlue(), enemyType.getLightSize());
		lights.add(light);
		
		this.max_speed = 3;
	}
	
	public void update() {
		super.update();
		if(getPlayerDistance(x, y, handler.getPlayer().getX(), handler.getPlayer().getY()) < 500) {
			shoot();
		}
	}
	
	private void shoot() {
		t1 = System.currentTimeMillis();
		if(t1 - t2 > 750) {
			t2 = t1;
			handler.getLaserList().add(new Laser_SimpleGreen(calcNoseCoords()[0], calcNoseCoords()[1], 8, 8, calcVelX(), calcVelY(), (int)speed + 6, 0, new Light(new Vector2f(0,0), enemyType.getEnemyColor().getRed(), enemyType.getEnemyColor().getGreen(), enemyType.getEnemyColor().getBlue(), 75)));
		}
	}
	
	private float calcVelX() {
		float sinFactor = (float) (Math.PI * angle / 180.0);
		return (float) Math.sin(sinFactor);
	}
	
	private float calcVelY() {
		float cosFactor = (float) (Math.PI * angle / 180.0);
		return (float) Math.cos(cosFactor) * -1;
	}
	
	private float[] calcNoseCoords() {
		float radius = width / 2;
		float t = (float) Math.toRadians(angle);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		
		float[] values = new float[2];
		values[0] = x + width/2 + xT;
		values[1] = y + height/2 + yT;
		
		return values;
	}
	
	private int getPlayerDistance(float eX, float eY, float playerX, float playerY) {
		return (int) Math.sqrt(Math.pow(eX - playerX, 2) + Math.pow(eY - playerY, 2));
	}
}
