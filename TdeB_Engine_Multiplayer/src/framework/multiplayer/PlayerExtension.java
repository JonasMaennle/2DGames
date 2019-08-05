package framework.multiplayer;

import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.loadSpriteSheet;
import static framework.helper.Graphics.quickLoaderImage;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import object.player.Player;
import object.weapon.Bullet_Basic;
import object.weapon.Bullet_Laser;
import object.weapon.Weapon_Flamethrower;
import object.weapon.Weapon_Icethrower;
import object.weapon.Weapon_LMG;
import object.weapon.Weapon_LaserShotgun;
import object.weapon.Weapon_Minigun;
import object.weapon.Weapon_Pistol;
import object.weapon.Weapon_Railgun;
import object.weapon.Weapon_Shotgun;

public class PlayerExtension extends Player implements Serializable{

	private static final long serialVersionUID = -287827503604601538L;
	private int playerID;
	private transient Image weaponImageLeft, weaponImageRight;
	private float weaponAngle;
	private String weaponClassName;
	
	private float weaponX, weaponY, weaponWidth, weaponHeight;
	private CopyOnWriteArrayList<Bullet_Basic> bulletList;

	public PlayerExtension(int x, int y, Handler handler, int playerID) {
		super(x, y, handler);
		
		this.playerID = playerID;
		this.weaponAngle = 0;
		this.weaponClassName = "";
		this.bulletList = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public void update() {
		

	}
	
	@Override
	public void draw() {
		loadImages();
		
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
		
		drawBullets();
		drawWeaponImage();
	}
	
	private void drawWeaponImage() {

		if(direction.equals("right")){
			drawQuadImageRotLeft(weaponImageRight, weaponX, weaponY, weaponWidth, weaponHeight, weaponAngle);
		}else{
			drawQuadImageRotRight(weaponImageLeft, weaponX, weaponY, weaponWidth, weaponHeight, weaponAngle - 180);
		}
	}
	
	private void drawBullets() {
		for(Bullet_Basic b : bulletList) {
			if(b.getImage() == null && b instanceof Bullet_Basic)
				b.setImage(quickLoaderImage("player/bullet_basic"));
			if(b.getImage() == null && b instanceof Bullet_Laser)
				b.setImage(quickLoaderImage("player/laser_red"));
			
			b.draw();
		}
	}

	private void loadImages() {
		// player walk anim and images
		if(idle_right == null)
			this.idle_right = quickLoaderImage("player/player_idle_right");
		if(idle_left == null)
			this.idle_left = quickLoaderImage("player/player_idle_left");
		if(walkLeft == null)
			this.walkLeft = new Animation(loadSpriteSheet("player/player_walk_left", TILE_SIZE, TILE_SIZE), 200);
		if(walkRight == null)
			this.walkRight = new Animation(loadSpriteSheet("player/player_walk_right", TILE_SIZE, TILE_SIZE), 200);	
		
		// weapon pistol images
		if(weaponImageLeft == null)
			this.weaponImageLeft = quickLoaderImage("player/weapon_pistol_left");
		if(weaponImageRight == null)
			this.weaponImageRight = quickLoaderImage("player/weapon_pistol_right");
		
		try {
			String fullName = "object.weapon." + weaponClassName;
			
			Class<?> clazz = Class.forName(fullName);
			Constructor<?> constructor = clazz.getConstructor(Integer.TYPE, Integer.TYPE, Player.class, Handler.class);
			Object instance = constructor.newInstance(new Integer((int)weaponWidth), new Integer((int)weaponHeight), handler.getPlayer(), handler);
			
			if(instance instanceof Weapon_Pistol) {
				weaponImageLeft = ((Weapon_Pistol) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Pistol) instance).getWeaponRight();
			}else if(instance instanceof Weapon_LMG) {
				weaponImageLeft = ((Weapon_LMG) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_LMG) instance).getWeaponRight();
			}else if(instance instanceof Weapon_Minigun) {
				weaponImageLeft = ((Weapon_Minigun) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Minigun) instance).getWeaponRight();
			}else if(instance instanceof Weapon_Railgun) {
				weaponImageLeft = ((Weapon_Railgun) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Railgun) instance).getWeaponRight();
			}else if(instance instanceof Weapon_Shotgun) {
				weaponImageLeft = ((Weapon_Shotgun) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Shotgun) instance).getWeaponRight();
			}else if(instance instanceof Weapon_LaserShotgun) {
				weaponImageLeft = ((Weapon_LaserShotgun) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_LaserShotgun) instance).getWeaponRight();
			}else if(instance instanceof Weapon_Flamethrower) {
				weaponImageLeft = ((Weapon_Flamethrower) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Flamethrower) instance).getWeaponRight();
			}else if(instance instanceof Weapon_Icethrower) {
				weaponImageLeft = ((Weapon_Icethrower) instance).getWeaponLeft();
				weaponImageRight = ((Weapon_Icethrower) instance).getWeaponRight();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public float getWeaponAngle() {
		return weaponAngle;
	}

	public void setWeaponAngle(float weaponAngle) {
		this.weaponAngle = weaponAngle;
	}

	public float getWeaponX() {
		return weaponX;
	}

	public void setWeaponX(float weaponX) {
		this.weaponX = weaponX;
	}

	public float getWeaponY() {
		return weaponY;
	}

	public void setWeaponY(float weaponY) {
		this.weaponY = weaponY;
	}

	public float getWeaponWidth() {
		return weaponWidth;
	}

	public void setWeaponWidth(float weaponWidth) {
		this.weaponWidth = weaponWidth;
	}

	public float getWeaponHeight() {
		return weaponHeight;
	}

	public void setWeaponHeight(float weaponHeight) {
		this.weaponHeight = weaponHeight;
	}

	public String getWeaponClassName() {
		return weaponClassName;
	}

	public void setWeaponClassName(String weaponClassName) {
		this.weaponClassName = weaponClassName;
	}

	public CopyOnWriteArrayList<Bullet_Basic> getBulletList() {
		return bulletList;
	}

	public void setBulletList(CopyOnWriteArrayList<Bullet_Basic> bulletList) {
		this.bulletList = bulletList;
	}
}
