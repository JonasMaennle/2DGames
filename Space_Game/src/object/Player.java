package object;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.MOVEMENT_Y;

import java.awt.Point;
import java.awt.Rectangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import framework.core.Handler;
import framework.entity.GameEntity;
import framework.entity.LaserType;
import framework.shader.Light;
import object.collectable.Collectable_Basic;
import object.collectable.Collectable_Spike;
import object.weapon.Laser_SimpleBlue;

import static framework.helper.Collection.*;

public class Player implements GameEntity{
	
	private float x, y;
	private int width, height;
	private float speed, velX, velY;;
	private Handler handler;
	private Image image;
	private float playerRotation;
	private Light light;
	private int MAX_SPEED, MIN_SPEED;
	private LaserType currentLaserType;
	private long t1, t2;
	private float boostEnergy, boostEnergyMax;
	private boolean bombPlaced;
	
	private int defaultShootingDelay;
	private int shootingDelay;
	
	private Collectable_Spike collectable_Spike;
	private boolean doubleShoot;
	private boolean bulletSpeedUp;
	
	
	public Player(int x, int y, Handler handler) {
		this.x = x;
		this.y = y;
		this.handler = handler;
		
		this.width = 64;
		this.height = 64;
		this.image = quickLoaderImage("player/player_0");
		
		this.velX = 0;
		this.velY = 0;
		
		this.playerRotation = 0;
		
		this.speed = 4f;
		this.MAX_SPEED = 8;
		this.MIN_SPEED = 4;
		
		this.light = new Light(new Vector2f(0, 0), 5, 17, 25, 2);
		//lights.add(light);
		this.currentLaserType = LaserType.SIMPLE_BLUE;
		
		this.boostEnergyMax = 512;
		this.boostEnergy = boostEnergyMax;
		
		this.bombPlaced = false;
		
		this.defaultShootingDelay = 150;
		this.shootingDelay = defaultShootingDelay;
		this.doubleShoot = true;
		this.bulletSpeedUp = true;
	}

	@Override
	public void update() {
		velX = velX * 0.99f;
		velY = velY * 0.99f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) 
			playerRotation += (speed / 3);

		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			playerRotation -= (speed / 3);
			

		// only positive values between 0 - 360
		if(playerRotation < 0) {
			playerRotation = 360 + playerRotation;
		}
		playerRotation = playerRotation % 360;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			velX *= 0.95f;
			velY *= 0.95f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			velX = calcVelX();
			velY = calcVelY();
			
			if(speed < MAX_SPEED)speed *= 1.0075f;
		}else{
			if(speed > MIN_SPEED)speed *= 0.99f;
		}
		
		// shoot laser
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			shoot();
		
		// speed boost
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if(boostEnergy >= 2) {
				boostEnergy -= 2;
				if(speed < MAX_SPEED * 1.5f) {
					speed += 0.1f;
				}
			}else {
				boostEnergy = 0;
				if(speed > MAX_SPEED) speed -= 0.1f;
			}
		}else {
			if(speed > MAX_SPEED) speed -= 0.1f;
			if(boostEnergy < boostEnergyMax) boostEnergy += 0.5f;
		}
		
		// place bomb
		if(Keyboard.isKeyDown(Keyboard.KEY_B) && !bombPlaced) {
			handler.getBombList().add(new PlasmaBomb(calcTailCoords()[0], calcTailCoords()[1], 500, handler));
			bombPlaced = true;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_B) && bombPlaced) {
			bombPlaced = false;
		}

		
		x += velX * speed;
		y += velY * speed;	
		
		//light.setLocation(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y));
		setLightToNose();
		collision();
		
		if(collectable_Spike != null) {
			if(collectable_Spike.getHealthPoints() <= 0)
				collectable_Spike = null;
		}
	}

	@Override
	public void draw() {

		//drawQuadImage(image, x, y, width, height);
		renderSingleLightStatic(shadowObstacleList, light);
		drawQuadImageRotCenter(image, x, y, width, height, playerRotation);
		
		// drawBounds
		// drawQuadImage(boundImage, x, y, width, height);
		
		if(collectable_Spike != null)
			collectable_Spike.draw();
	}
	
	private void collision() {
		// collectable collision
		for(Collectable_Basic collectable : handler.getCollectableList()) {
			if(collectable.getBounds().intersects(getBounds())) {
				collectable.setFound(true);
				collectable.setPlayer(this);
				
				if(collectable instanceof Collectable_Spike && collectable_Spike == null)
					collectable_Spike = (Collectable_Spike) collectable;
			}
		}
	}
	
	private void setLightToNose() {
		light.setRadius((1 / speed) * 10);
		
		float radius = (width / 2) * 0.8f;
		float t = (float) Math.toRadians(playerRotation - 180);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		light.setLocation(new Vector2f(x + width/2 + xT + MOVEMENT_X, y + height/2 + yT + MOVEMENT_Y));
	}
	
	private float calcVelX() {
		float sinFactor = (float) (Math.PI * playerRotation / 180.0);
		return (float) Math.sin(sinFactor);
	}
	
	private float calcVelY() {
		float cosFactor = (float) (Math.PI * playerRotation / 180.0);
		return (float) Math.cos(cosFactor) * -1;
	}
	
	// [0] = x / [1] = y
	private float[] calcNoseCoords() {
		float radius = width / 2;
		float t = (float) Math.toRadians(playerRotation);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		
		float[] values = new float[2];
		values[0] = x + width/2 + xT;
		values[1] = y + height/2 + yT;
		
		return values;
	}
	
	private float[] calcTailCoords() {
		float radius = width / 2;
		float t = (float) Math.toRadians(playerRotation - 180);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		
		float[] values = new float[2];
		values[0] = x + width/2 + xT;
		values[1] = y + height/2 + yT;
		
		return values;
	}
	
	private Point calcNoseCoordsOffset(float angleDif) {
		float radius = width / 2;
		float t = (float) Math.toRadians(playerRotation - angleDif);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		
		return new Point((int)(xT + x + width/2), (int)(yT + y + height/2));
	}
	
	private void shoot() {
		t1 = System.currentTimeMillis();
		if(t1 - t2 > shootingDelay) {
			t2 = t1;
			
			switch (currentLaserType) {
			case SIMPLE_BLUE:
				int bulletSpeed = 10;
				if(bulletSpeedUp)
					bulletSpeed = 20;
				//                                              float x,             float y, int width, int height, float velX, float velY, int speed, float angle, Light light
				if(doubleShoot) {
					// left shot
					handler.getLaserList().add(new Laser_SimpleBlue(calcNoseCoordsOffset(335).x, calcNoseCoordsOffset(335).y, 8, 8, calcVelX(), calcVelY(), (int)speed + bulletSpeed, 0, new Light(new Vector2f(0,0), 51, 173, 255, 75), handler));
					// right shot
					handler.getLaserList().add(new Laser_SimpleBlue(calcNoseCoordsOffset(25).x, calcNoseCoordsOffset(25).y, 8, 8, calcVelX(), calcVelY(), (int)speed + bulletSpeed, 0, new Light(new Vector2f(0,0), 51, 173, 255, 75), handler));
				}else {
					handler.getLaserList().add(new Laser_SimpleBlue(calcNoseCoords()[0], calcNoseCoords()[1], 8, 8, calcVelX(), calcVelY(), (int)speed + 10, 0, new Light(new Vector2f(0,0), 51, 173, 255, 50), handler));
				}
					
				break;

			default:
				break;
			}
		}
	}

	@Override
	public float getX() { return x; }

	@Override
	public float getY() { return y; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public Vector2f[] getVertices() {
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y), // left top
				new Vector2f(x + MOVEMENT_X, y + MOVEMENT_Y + height), // left bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y + height), // right bottom
				new Vector2f(x + MOVEMENT_X + width, y + MOVEMENT_Y) // right top
		};
	}

	@Override
	public Rectangle getBounds() { return new Rectangle((int)x, (int)y, width, height); }

	@Override
	public Rectangle getTopBounds() { return new Rectangle((int)x, (int)y, width, 8); }

	@Override
	public Rectangle getBottomBounds() { return new Rectangle((int)x, (int)y + height - 8, width, 8); }

	@Override
	public Rectangle getLeftBounds() { return new Rectangle((int)x, (int)y + 8, 8, height - 16); }

	@Override
	public Rectangle getRightBounds() { return new Rectangle((int)x + width - 8, (int)y + 8, 8, height- 16); }

	public float getVelX() { return velX; }
	public float getVelY() { return velY; }
	
	public float getTotalSpeedX() { return velX * speed; }
	public float getTotalSpeedY() { return velY * speed; }

	public float getBoostEnergy() { return boostEnergy; }
	public float getBoostEnergyMax() { return boostEnergyMax; }

	public float getPlayerRotation() { return playerRotation; }

	public Collectable_Spike getCollectable_Spike() { return collectable_Spike; }
}
