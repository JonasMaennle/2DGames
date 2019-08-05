package object.weapon;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import object.enemy.Enemy_Basic;
import object.player.Player;

public class Weapon_Basic implements GameEntity{

	protected float x, y, angle, destX, destY, centX, centY;
	protected int width, height;
	protected int weaponDelta;
	protected int weaponDeltaMAX;
	
	protected transient Player player;
	protected transient Handler handler;
	protected transient Image default_weapon;
	protected boolean empowered;
	
	protected int bulletSpeedMAX;
	protected int bulletSpeed;
	protected int bulletDamage;
	
	protected int max_ammo;
	protected int bottomAngleRange;
	protected int topAngleRange;
	
	protected transient Rectangle bulletSpawnPoint;
	
	protected CopyOnWriteArrayList<Bullet_Basic> bulletList;
	
	public Weapon_Basic(int width, int height, Player player, Handler handler) {
		this.player = player;
		this.x = player.getX() + player.getWidth() / 2;
		this.y = player.getY() + player.getHeight() / 2;
		this.width = width;
		this.height = height;
		
		this.angle = 0;
		this.destX = 0;
		this.destY = 0;
		
		this.empowered = false;
		
		this.max_ammo = 999;
		this.bulletSpeed = 5;
		this.bulletDamage = 16;
		
		this.handler = handler;
		this.default_weapon = quickLoaderImage("player/weapon");
		this.bulletList = new CopyOnWriteArrayList<Bullet_Basic>();
		
		this.topAngleRange = 90; // def. 50
		this.bottomAngleRange = 90; // def. 40
	}
	
	@Override
	public void update() {
		
		centX = player.getX() + player.getWidth()/2;
		centY = player.getY() + player.getHeight()/2;
		
		if(player.getDirection().equals("right")){
			x = player.getX() + 22;
			y = player.getY() + 7;
		}else{
			x = player.getX() - 4;
			y = player.getY() + 7;
		}
		calcAngle(Mouse.getX() - MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y);
		
		for(Bullet_Basic bullet : bulletList){
			bullet.update();
			
			if(bullet.isOutOfMap()){
				bullet.die();
				bulletList.remove(bullet);
			}
				
			
			for(GameEntity ge : handler.obstacleList){
				if(ge.getBounds().intersects(bullet.getBounds())){
					bullet.die();
					bulletList.remove(bullet);
				}
			}
			for(Enemy_Basic e : handler.enemyList){
				if(e.getBounds().intersects(bullet.getBounds())){
					e.setHp(e.getHp() - bulletDamage);
					bullet.die();
					bulletList.remove(bullet);
					
					// trigger enemy if enemy is out of range
					if(e.getSpeed() == 0)
						e.setSpeed(2);
				}
			}
		}
	}

	@Override
	public void draw() {
		
		for(Bullet_Basic bullet : bulletList){
			bullet.draw();
		}
		
		//drawQuadImage(default_weapon, x, y, width, height);
		if(player.getDirection().equals("right")){
			drawQuadImageRotLeft(default_weapon, x, y, width, height, angle);
		}else{
			drawQuadImageRotRight(default_weapon, x, y, width, height, angle - 180);
		}
	}
	
	public void shoot(){
		// walk right
		if(player.getDirection().equals("right")){
			bulletList.add(new Bullet_Basic(x + 2, y + (height/2) - 4, 6, 6, destX, destY, "right", bulletSpeed, angle));
		}
		
		// walk left
		if(player.getDirection().equals("left")){
			bulletList.add(new Bullet_Basic(x + width - 6, y + (height/2) - 4, 6, 6, destX, destY, "left", bulletSpeed, angle));
		}
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	protected void calcAngle(float destX, float destY){
		angle = (float) Math.toDegrees(Math.atan2(destY - (centY), destX - (centX)));

	    if(angle < 0){
	        angle += 360;
	    }
	    // block angle 320 - 360 && 0 - 30
	    if(player.getDirection().equals("right")){
	    	if(angle < (360 - topAngleRange) && angle > 180){
	    		angle = (360 - topAngleRange);
	    	}else if(angle > bottomAngleRange && angle < 180){
	    		angle = bottomAngleRange;
	    	}else{
	    		this.destX = destX;
	    		this.destY = destY;
	    	}
	    }
	    // block angle if 220 - 180 && 180 - 150
	    if(player.getDirection().equals("left")){
	    	if(angle > 180 + topAngleRange && angle < 360){
	    		angle = 180 + topAngleRange;
	    	}else if(angle < 180 - bottomAngleRange && angle > 0){
	    		angle = 180 - bottomAngleRange;
	    	}else{
	    		this.destX = destX;
	    		this.destY = destY;
	    	}
	    }
		//System.out.println("Angle: " + angle);
	}
	
	public void wipe(){
		bulletList.clear();
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
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
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
		return null;
	}

	@Override
	public Rectangle getBottomBounds() {
		return null;
	}

	@Override
	public Rectangle getLeftBounds() {
		return null;
	}

	@Override
	public Rectangle getRightBounds() {
		return null;
	}

	public int getMax_ammo() {
		return max_ammo;
	}

	public void setMax_ammo(int max_ammo) {
		this.max_ammo = max_ammo;
	}

	public int getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public int getBulletDamage() {
		return bulletDamage;
	}

	public void setBulletDamage(int bulletDamage) {
		this.bulletDamage = bulletDamage;
	}

	public int getWeaponDelta() {
		return weaponDelta;
	}

	public void setWeaponDelta(int weaponDelta) {
		this.weaponDelta = weaponDelta;
	}

	public int getWeaponDeltaMAX() {
		return weaponDeltaMAX;
	}

	public void setWeaponDeltaMAX(int weaponDeltaMAX) {
		this.weaponDeltaMAX = weaponDeltaMAX;
	}

	public int getBulletSpeedMAX() {
		return bulletSpeedMAX;
	}

	public void setBulletSpeedMAX(int bulletSpeedMAX) {
		this.bulletSpeedMAX = bulletSpeedMAX;
	}

	public boolean isEmpowered() {
		return empowered;
	}

	public void setEmpowered(boolean empowered) {
		this.empowered = empowered;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
