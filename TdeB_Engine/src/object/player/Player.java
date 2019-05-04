package object.player;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import object.collectable.Collectable_Basic;
import object.collectable.Collectable_Flamethrower;
import object.collectable.Collectable_Helmet;
import object.collectable.Collectable_HelmetBattery;
import object.collectable.Collectable_LMG;

public class Player implements GameEntity{
	
	private int width, height, velX, velY;
	private float x, y, speed;
	private String direction;
	private Image idle_left, idle_right;
	private Handler handler;
	
	private Weapon_Basic weapon;
	private boolean isShooting;
	
	private Animation walkRight, walkLeft;
	
	public Player(int x, int y, Handler handler){
		this.handler = handler;
		
		this.x = x;
		this.y = y;
		this.width = 32;
		this.height = 32;
		this.speed = 4f;
		this.direction = "right";
		this.isShooting = false;

		this.weapon = new Weapon_Pistol(16, 8, this, handler);
		
		this.idle_left = quickLoaderImage("player/player_idle_left");
		this.idle_right = quickLoaderImage("player/player_idle_right");
		this.walkLeft = new Animation(loadSpriteSheet("player/player_walk_left", TILE_SIZE, TILE_SIZE), 200);
		this.walkRight = new Animation(loadSpriteSheet("player/player_walk_right", TILE_SIZE, TILE_SIZE), 200);		
		
		//lights.add(playerLight);
	}

	public void update() {
		velX = 0;
		velY = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			velX += 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			velX -= 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			velY = 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			velY = -1;

		// Shoot
		if(Mouse.isButtonDown(0) && !isShooting){
			if(weapon instanceof Weapon_Pistol){
				isShooting = true;
				weapon.shoot();
			}	
			if(weapon instanceof Weapon_Flamethrower)
				weapon.shoot();
			
			if(weapon instanceof Weapon_LMG){
				weapon.shoot();
			}
		}	
		if(!Mouse.isButtonDown(0)){
			isShooting = false;
		}
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
		collectableCollision();
		
		updateDirection();
		weapon.update();
	}

	public void draw() {

		switch (direction) {
		case "right":
			if(velX == 0 && velY == 0)
				drawQuadImage(idle_right, x, y, width, height);
			else
				drawAnimation(walkRight, x, y, width, height);
			break;
		case "left":
			if(velX == 0 && velY == 0)
				drawQuadImage(idle_left, x, y, width, height);
			else
				drawAnimation(walkLeft, x, y, width, height);
			break;
		default:
			System.out.println("Player velocityX out of range!");
			break;
		}
		weapon.draw();
	}
	
	private void mapCollision() {
		for(GameEntity ge : handler.obstacleList){
			// top
			if(ge.getBottomBounds().intersects(getTopBounds())){
				velY = 0;
				y = (float) (ge.getY() + ge.getHeight());
			}
			// bottom
			if(ge.getTopBounds().intersects(getBottomBounds())){	
				velY = 0;
				y = (float) (ge.getY() - TILE_SIZE);
			}		
			// left
			if(ge.getRightBounds().intersects(getLeftBounds())){
				velX = 0;
				x = (float) (ge.getX() + ge.getWidth());
			}
			// right
			if(ge.getLeftBounds().intersects(getRightBounds())){
				velX = 0;
				x = (float) (ge.getX() - TILE_SIZE);
			}	
		}
	}
	
	private void collectableCollision(){
		for(Collectable_Basic c : handler.collectableList){
			if(c.getBounds().intersects(getBounds())){
				// Helmet
				if(c instanceof Collectable_Helmet && !c.isFound()){
					((Collectable_Helmet) c).setPlayer(this);
					c.setFound(true);
					handler.initFilter(6);
				}
				// Helmet Battery
				if(c instanceof Collectable_HelmetBattery && !c.isFound()){
					BATTERY_CHARGE += 50;
					handler.collectableList.remove(c);
				}
				// Flamethrower
				if(c instanceof Collectable_Flamethrower && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Flamethrower(32, 16, this, handler);
					handler.collectableList.remove(c);
				}
				// LMG
				if(c instanceof Collectable_LMG && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_LMG(32, 16, this, handler);
					handler.collectableList.remove(c);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void drawBounds(){
		drawQuad((int)x, (int)y, 16, TILE_SIZE); // left
		drawQuad((int)x + TILE_SIZE - 16,(int) y, 16, TILE_SIZE); // right
		
		drawQuad((int)x,(int) y, TILE_SIZE, 16); // top
		drawQuad((int)x,(int) y + TILE_SIZE - 16, TILE_SIZE, 16); // bottom
	}
	
	private void updateDirection(){
		float mouseX = Mouse.getX() - MOVEMENT_X;
		
		if(mouseX > x)
			direction = "right";
		if(mouseX < x)
			direction = "left";
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
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Rectangle getTopBounds() {
		return new Rectangle((int)x + 8,(int) y, TILE_SIZE - 8, 16);
	}

	@Override
	public Rectangle getBottomBounds() {
		return new Rectangle((int)x + 8,(int) y + TILE_SIZE - 16, TILE_SIZE - 8, 16);
	}

	@Override
	public Rectangle getLeftBounds() {
		return new Rectangle((int)x, (int)y + 8, 16, TILE_SIZE - 8);
	}

	@Override
	public Rectangle getRightBounds() {
		return new Rectangle((int)x + TILE_SIZE - 16,(int) y + 8, 16, TILE_SIZE - 8);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public Weapon_Basic getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon_Basic weapon) {
		this.weapon = weapon;
	}

	public Animation getWalkRight() {
		return walkRight;
	}

	public Animation getWalkLeft() {
		return walkLeft;
	}

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
