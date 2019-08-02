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
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.entity.GameEntity;
import framework.helper.Collection;
import framework.shader.Light;
import object.collectable.Collectable_Ammo;
import object.collectable.Collectable_Basic;
import object.collectable.Collectable_Flamethrower;
import object.collectable.Collectable_Goal;
import object.collectable.Collectable_Health;
import object.collectable.Collectable_Helmet;
import object.collectable.Collectable_HelmetBattery;
import object.collectable.Collectable_Icethrower;
import object.collectable.Collectable_LMG;
import object.collectable.Collectable_LaserShotgun;
import object.collectable.Collectable_Minigun;
import object.collectable.Collectable_Railgun;
import object.collectable.Collectable_Shield;
import object.collectable.Collectable_Shotgun;
import object.collectable.Collectable_SpeedForWeapon;
import object.collectable.Collectable_SpeedUp;
import object.weapon.Weapon_Basic;
import object.weapon.Weapon_Flamethrower;
import object.weapon.Weapon_Icethrower;
import object.weapon.Weapon_LMG;
import object.weapon.Weapon_LaserShotgun;
import object.weapon.Weapon_Minigun;
import object.weapon.Weapon_Pistol;
import object.weapon.Weapon_Railgun;
import object.weapon.Weapon_Shotgun;

public class Player implements GameEntity{
	
	private int width, height, velX, velY;
	private float x, y, speed;
	private String direction;
	private Image idle_left, idle_right;
	private Handler handler;
	private int helmetBightness;
	
	private Shield shield;
	private Weapon_Basic weapon;
	private boolean isShooting, hasHelmet, speedUp;
	private long speedUpTimestamp;
	private long maxSpeedUpTime;
	
	private Light weaponBackgroundLight;
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
		this.hasHelmet = false;
		this.speedUp = false;
		
		this.maxSpeedUpTime = 20000;
		this.helmetBightness = 25;

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

		// check AMMO amount
		if(AMMO_LEFT <= 0) {
			AMMO_LEFT = 999;
			weapon.wipe();
			weapon = new Weapon_Pistol(16, 8, this, handler);
		}
		
		// speed up
		if(speedUp) {
			if(System.currentTimeMillis() - speedUpTimestamp < maxSpeedUpTime) {
				this.speed = 5.5f;
				float number = (float)(System.currentTimeMillis() - speedUpTimestamp) / maxSpeedUpTime;
				number = (1 - number);
				Collection.SPEEDBAR = number;
			}else {
				this.speed = 4f;
				Collection.SPEEDBAR = 0;
				speedUp = false;
			}
		}
		
		// Shoot
		if(Mouse.isButtonDown(0) && !isShooting){
			if(weapon instanceof Weapon_Pistol){
				isShooting = true;
				weapon.shoot();
			}	
			if(weapon instanceof Weapon_Flamethrower)
				weapon.shoot();
			
			if(weapon instanceof Weapon_Icethrower)
				weapon.shoot();
			
			if(weapon instanceof Weapon_LMG)
				weapon.shoot();
				
			if(weapon instanceof Weapon_Railgun)
				weapon.shoot();
			
			if(weapon instanceof Weapon_Minigun)
				weapon.shoot();
			
			if(weapon instanceof Weapon_Shotgun) {
				weapon.shoot();
				isShooting = true;
			}
			
			if(weapon instanceof Weapon_LaserShotgun) {
				weapon.shoot();
				isShooting = true;
			}
			
		}	
		if(!Mouse.isButtonDown(0)){
			isShooting = false;
		}
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
		collectableCollision();
		
		if(!weapon.isEmpowered())
			weaponBackgroundLight = null;
		
		// update || remove shield
		if(shield != null) {
			shield.update();
			if(shield.getEngeryLeft() <= 0) {
				shield.die();
				shield = null;
			}
		}
		
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
			
			if(weaponBackgroundLight != null)weaponBackgroundLight.setLocation(new Vector2f(x + width/2 + MOVEMENT_X, y + MOVEMENT_Y + height/2));
			break;
		case "left":
			if(velX == 0 && velY == 0)
				drawQuadImage(idle_left, x, y, width, height);
			else
				drawAnimation(walkLeft, x, y, width, height);
			
			if(weaponBackgroundLight != null)weaponBackgroundLight.setLocation(new Vector2f(x + width/2 + MOVEMENT_X, y + MOVEMENT_Y + height/2));
			break;
		default:
			System.out.println("Player velocityX out of range!");
			break;
		}
		
		if(weaponBackgroundLight != null)renderSingleLightStatic(shadowObstacleList, weaponBackgroundLight);
		weapon.draw();
		
		if(shield != null)
			shield.draw();
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
					hasHelmet = true;
					BATTERY_CHARGE = 96;
					handler.setFogFilter(8);
					handler.getInfo_manager().createNewMessage(x - 64, y - 64, "Helmet  found", new org.newdawn.slick.Color(244,211,65), 18, 2000);
				}
				// Helmet Battery
				if(c instanceof Collectable_HelmetBattery && !c.isFound()){
					int batteryTMP = (int)BATTERY_CHARGE;
					BATTERY_CHARGE += 96;
					if(BATTERY_CHARGE >= 96)
						BATTERY_CHARGE = 96;
					handler.getInfo_manager().createNewMessage(x - 100, y - 64, "Energy  Stone  found   + " + (96 - batteryTMP), new org.newdawn.slick.Color(50,255,28), 18, 2000);
					handler.collectableList.remove(c);
					Collection.lights.remove(c.getLight());
				}
				// HP Stone
				if(c instanceof Collectable_Health && !c.isFound()){
					Collection.lights.remove(c.getLight());
					int hpTMP = PLAYER_HP;
					PLAYER_HP += 50;
					if(PLAYER_HP >= 96)
						PLAYER_HP = 96;
					handler.getInfo_manager().createNewMessage(x - 100, y - 64, "HP  Stone  found   +  " + (96 - hpTMP), new org.newdawn.slick.Color(183,3,3), 18, 2000);
					handler.collectableList.remove(c);
				}
				// Speed Stone
				if(c instanceof Collectable_SpeedUp && !c.isFound()){
					speedUp = true;
					speedUpTimestamp = System.currentTimeMillis();
					
					handler.getInfo_manager().createNewMessage(x - 80, y - 64, "Speed  up  found", new org.newdawn.slick.Color(20,80,255), 18, 2000);
					Collection.lights.remove(c.getLight());
					handler.collectableList.remove(c);
				}
				// Shield
				if(c instanceof Collectable_Shield && !c.isFound()){
					
					if(shield != null)
						shield.die();
					shield = new Shield((int)x, (int)y, this);
					handler.getInfo_manager().createNewMessage(x - 70, y - 64, "Shield  found", new org.newdawn.slick.Color(0,255,230), 18, 2000);
					Collection.lights.remove(c.getLight());
					handler.collectableList.remove(c);
				}
				// Goal
				if(c instanceof Collectable_Goal && !c.isFound()){
					StateManager.gameState = GameState.LOADING;
					handler.collectableList.remove(c);
				}
				
				// Weapon Speed
				if(c instanceof Collectable_SpeedForWeapon && !c.isFound()){
					Collection.lights.remove(c.getLight());
					// check if weapon was already upgraded
					if(weapon.getBulletSpeed() == weapon.getBulletSpeedMAX()) {
						
						this.weaponBackgroundLight = new Light(new Vector2f(x, y), 25, 2, 24, 2);
						
						weapon.setEmpowered(true);
						weapon.setBulletSpeed(weapon.getBulletSpeed() * 2);
						weapon.setWeaponDelta(weapon.getWeaponDeltaMAX() / 2);
						weapon.setBulletDamage((int)(weapon.getBulletDamage() * 1.5f));
						handler.getInfo_manager().createNewMessage(x - 128, y - 192, "Weapon  improved", new org.newdawn.slick.Color(255,0,247), 30, 2000);
					}

					handler.collectableList.remove(c);
				}
				
				
				// Flamethrower
				if(c instanceof Collectable_Flamethrower && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Flamethrower(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 100, y - 64, "Flamethrower  found", new org.newdawn.slick.Color(255, 102, 0), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// Icethrower
				if(c instanceof Collectable_Icethrower && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Icethrower(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 100, y - 64, "Icethrower  found", new org.newdawn.slick.Color(0, 20, 255), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// LMG
				if(c instanceof Collectable_LMG && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_LMG(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "Machine  Gun  found", new org.newdawn.slick.Color(200, 200, 200), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// Railgun
				if(c instanceof Collectable_Railgun && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Railgun(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "Laser  Rifle  found", new org.newdawn.slick.Color(255, 0, 0), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// Shotgun
				if(c instanceof Collectable_Shotgun && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Shotgun(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "Shotgun  found", new org.newdawn.slick.Color(200, 200, 200), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// LaserShotgun
				if(c instanceof Collectable_LaserShotgun && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_LaserShotgun(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "Laser  Shotgun  found", new org.newdawn.slick.Color(255, 0, 0), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// Minigun
				if(c instanceof Collectable_Minigun && !c.isFound()){
					this.weapon.wipe();
					this.weapon = new Weapon_Minigun(32, 16, this, handler);
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "Minigun  found", new org.newdawn.slick.Color(255, 102, 0), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
				}
				// AMMO
				if(c instanceof Collectable_Ammo && !c.isFound()){
					handler.getInfo_manager().createNewMessage(x - 96, y - 64, "+ " + (weapon.getMax_ammo() - AMMO_LEFT) + "  Ammo  found", new org.newdawn.slick.Color(200, 200, 200), 18, 2000);
					handler.collectableList.remove(c);
					AMMO_LEFT = weapon.getMax_ammo();
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

	public boolean isHasHelmet() {
		return hasHelmet;
	}

	public void setHasHelmet(boolean hasHelmet) {
		this.hasHelmet = hasHelmet;
	}

	public int getHelmetBightness() {
		return helmetBightness;
	}

	public void setHelmetBightness(int helmetBightness) {
		this.helmetBightness = helmetBightness;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}
}
