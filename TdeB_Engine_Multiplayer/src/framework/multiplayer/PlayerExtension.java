package framework.multiplayer;

import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.loadSpriteSheet;
import static framework.helper.Graphics.quickLoaderImage;

import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import object.player.Player;

public class PlayerExtension extends Player implements Serializable{

	private static final long serialVersionUID = -287827503604601538L;
	private int playerID;
	private transient Image weaponImageLeft, weaponImageRight;
	private float weaponAngle;
	
	private float weaponX, weaponY, weaponWidth, weaponHeight;

	public PlayerExtension(int x, int y, Handler handler, int playerID) {
		super(x, y, handler);
		
		this.playerID = playerID;
		this.weaponAngle = 0;
	}
	
	@Override
	public void update() {
		// update weapon position
		

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
		
		drawWeaponImage();
	}
	
	private void drawWeaponImage() {

		if(direction.equals("right")){
			drawQuadImageRotLeft(weaponImageRight, weaponX, weaponY, weaponWidth, weaponHeight, weaponAngle);
		}else{
			drawQuadImageRotRight(weaponImageLeft, weaponX, weaponY, weaponWidth, weaponHeight, weaponAngle - 180);
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
}
