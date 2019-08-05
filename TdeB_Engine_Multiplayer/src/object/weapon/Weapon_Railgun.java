package object.weapon;

import static framework.helper.Collection.AMMO_LEFT;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.quickLoaderImage;

import framework.core.Handler;
import framework.helper.Color;
import object.player.Player;

public class Weapon_Railgun extends Weapon_Basic{

	private long timer1, timer2;
	
	public Weapon_Railgun(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		
		this.weaponRight = quickLoaderImage("player/weapon_railgun_right");
		this.weaponLeft = quickLoaderImage("player/weapon_railgun_left");
		
		this.max_ammo = 50;
		this.bulletDamage = 128;
		this.bulletSpeed = 24;
		this.weaponDelta = 250;
		this.weaponDeltaMAX = 250;
		this.bulletSpeedMAX = 24;
	}

	public void update(){
		super.update();
		
		if(player.getDirection().equals("right")){
			x = player.getX() + 10;
			y = player.getY() + 5;
		}else{
			x = player.getX() - 4;
			y = player.getY() + 5;
		}
	}
	
	public void draw(){
		if(player.getDirection().equals("right")){
			drawQuadImageRotLeft(weaponRight, x, y, width, height, angle);
		}else{
			drawQuadImageRotRight(weaponLeft, x, y, width, height, angle - 180);
		}
		
		for(Bullet_Basic bullet : bulletList){
			bullet.draw();
		}
	}
	
	public void shoot(){
		
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > weaponDelta){
			AMMO_LEFT--;
			float angleOffset = 0;
			// walk right
			if(player.getDirection().equals("right")){
				
				angleOffset = 0;
				if(angle >= 270){
					angleOffset = 360 - angle; 
				}else{
					angleOffset = -angle;
				}
				angleOffset /= 360;

				bulletList.add(new Bullet_Laser(centX - 6, centY - (angleOffset * 100), 20, 6, destX, destY, "right", bulletSpeed, angle, new Color(20, 0, 0)));
			}
			// walk left
			if(player.getDirection().equals("left")){
				
				angleOffset = 0;
				if(angle <= 270 && angle >= 180){
					angleOffset = angle - 180; 
				}else if(angle < 180){
					angleOffset = -(180 - angle);
				}	
				angleOffset /= 360;
				
				bulletList.add(new Bullet_Laser(centX - 12, centY - (angleOffset * 100), 20, 6, destX, destY, "left", bulletSpeed, angle, new Color(20, 0, 0)));
			}
			timer2 = timer1;
		}
	}
	
	@Override
	public void wipe(){
		for(Bullet_Basic b : bulletList){
			b.die();
		}
		bulletList.clear();
	}
}
