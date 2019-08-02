package object.weapon;

import static framework.helper.Collection.AMMO_LEFT;
import static framework.helper.Collection.HEIGHT;
import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.getLeftBorder;
import static framework.helper.Collection.getRightBorder;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.quickLoaderImage;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.helper.Color;
import object.enemy.Enemy_Basic;
import object.player.Player;

public class Weapon_LaserShotgun extends Weapon_Basic {

	private Image weaponRight, weaponLeft;

	public Weapon_LaserShotgun(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);

		this.weaponRight = quickLoaderImage("player/weapon_lasershotgun_right");
		this.weaponLeft = quickLoaderImage("player/weapon_lasershotgun_left");

		this.max_ammo = 50;
		this.bulletDamage = 50;
		this.bulletSpeed = 16;
		this.bulletSpeedMAX = 16;
	}

	public void update() {
		
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
			drawQuadImageRotLeft(weaponRight, x - 14, y - 2, width, height, angle);
		} else {
			drawQuadImageRotRight(weaponLeft, x, y - 2, width, height, angle - 180);
		}

		for (Bullet_Basic bullet : bulletList) {
			bullet.draw();
		}
	}

	public void shoot() {

		AMMO_LEFT--;
		float angleOffset = 0;
		// walk right
		if (player.getDirection().equals("right") && destX > (player.getX() + player.getWidth() / 2)) {
			if (destX < x + (width / 2)) {
				destX = getRightBorder() - destX;
			}

			angleOffset = 0;
			if (angle >= 310) {
				angleOffset = 360 - angle;
			} else {
				angleOffset = -angle;
			}
			angleOffset /= 20;

			bulletList.add(new Bullet_Laser(x + 2, y + (height / 2) + angleOffset * -5, 20, 6, destX, destY - 40, "right", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x + 2, y + (height / 2) + angleOffset * -5, 20, 6, destX, destY - 20 , "right", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x + 2, y + (height / 2) + angleOffset * -5, 20, 6, destX, destY, "right", bulletSpeed, angle, new Color(20, 0, 0))); // center
			bulletList.add(new Bullet_Laser(x + 2, y + (height / 2) + angleOffset * -5, 20, 6, destX, destY + 20, "right", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x + 2, y + (height / 2) + angleOffset * -5, 20, 6, destX, destY + 40, "right", bulletSpeed, angle, new Color(20, 0, 0)));
		}

		// walk left
		if (player.getDirection().equals("left") && destX < (player.getX() + player.getWidth() / 2)) {
			if (destX > x + (width / 2)) {
				destX = getLeftBorder() + destX;
			}

			angleOffset = 0;
			if (angle <= 230 && angle >= 180) {
				angleOffset = angle - 180;
			} else if (angle < 180) {
				angleOffset = -(180 - angle);
			}
			angleOffset /= 20;
			
			bulletList.add(new Bullet_Laser(x, y + (height / 2) + angleOffset * -8, 20, 6, destX, destY - 40, "left", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x, y + (height / 2) + angleOffset * -8, 20, 6, destX, destY - 20, "left", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x, y + (height / 2) + angleOffset * -8, 20, 6, destX, destY, "left", bulletSpeed, angle, new Color(20, 0, 0))); // center
			bulletList.add(new Bullet_Laser(x, y + (height / 2) + angleOffset * -8, 20, 6, destX, destY + 20, "left", bulletSpeed, angle, new Color(20, 0, 0)));
			bulletList.add(new Bullet_Laser(x, y + (height / 2) + angleOffset * -8, 20, 6, destX, destY + 40, "left", bulletSpeed, angle, new Color(20, 0, 0)));
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
