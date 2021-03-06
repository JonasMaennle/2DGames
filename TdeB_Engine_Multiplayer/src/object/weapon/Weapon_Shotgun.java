package object.weapon;

import static framework.helper.Collection.AMMO_LEFT;
import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.quickLoaderImage;

import org.lwjgl.input.Mouse;

import framework.core.Handler;
import framework.entity.GameEntity;
import object.enemy.Enemy_Basic;
import object.player.Player;

public class Weapon_Shotgun extends Weapon_Basic {

	public Weapon_Shotgun(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);

		this.weaponRight = quickLoaderImage("player/weapon_shotgun_right");
		this.weaponLeft = quickLoaderImage("player/weapon_shotgun_left");

		this.max_ammo = 50;
		this.bulletDamage = 30;
		this.bulletSpeed = 15;
		this.bulletSpeedMAX = 15;
	}

	public void update() {
		
		centX = player.getX() + player.getWidth()/2;
		centY = player.getY() + player.getHeight()/2;
		
		if(player.getDirection().equals("right")){
			x = player.getX() + 8;
			y = player.getY() + 5;
		}else{
			x = player.getX() - 4;
			y = player.getY() + 5;
		}
		calcAngle(Mouse.getX() - MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y);
		
		for(Bullet_Basic bullet : bulletList){
			bullet.update();
			
			if(bullet.isOutOfMap() || (System.currentTimeMillis() - bullet.getSpawnTime() > 250)){
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

	public void draw() {

		if (player.getDirection().equals("right")) {
			drawQuadImageRotLeft(weaponRight, x, y, width, height, angle);
		} else {
			drawQuadImageRotRight(weaponLeft, x, y, width, height, angle - 180);
		}

		for (Bullet_Basic bullet : bulletList) {
			bullet.draw();
		}
	}

	public void shoot() {

		AMMO_LEFT--;
		float angleOffset = 0;
		// walk right
		if (player.getDirection().equals("right")) {

			angleOffset = 0;
			if (angle >= 270) {
				angleOffset = 360 - angle;
			} else {
				angleOffset = -angle;
			}
			angleOffset /= 360;

			bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY - 40, "right", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY - 20 , "right", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY, "right", bulletSpeed, angle)); // center
			bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY + 20, "right", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY + 40, "right", bulletSpeed, angle));
		}

		// walk left
		if (player.getDirection().equals("left")) {

			angleOffset = 0;
			if (angle <= 270 && angle >= 180) {
				angleOffset = angle - 180;
			} else if (angle < 180) {
				angleOffset = -(180 - angle);
			}
			angleOffset /= 360;
			
			bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY - 40, "left", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY - 20, "left", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY, "left", bulletSpeed, angle)); // center
			bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY + 20, "left", bulletSpeed, angle));
			bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY + 40, "left", bulletSpeed, angle));
		}

	}

	@Override
	public void wipe() {
		for (Bullet_Basic b : bulletList) {
			b.die();
		}
		bulletList.clear();
	}
}
