package object.weapon;

import static framework.helper.Collection.AMMO_LEFT;
import static framework.helper.Collection.getLeftBorder;
import static framework.helper.Collection.getRightBorder;
import static framework.helper.Graphics.drawQuadImageRotLeft;
import static framework.helper.Graphics.drawQuadImageRotRight;
import static framework.helper.Graphics.quickLoaderImage;

import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.helper.Color;
import object.player.Player;

public class Weapon_Minigun extends Weapon_Basic{

	private Image weaponRight, weaponLeft;
	private long timer1, timer2;
	
	public Weapon_Minigun(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		
		this.weaponRight = quickLoaderImage("player/weapon_minigun_right");
		this.weaponLeft = quickLoaderImage("player/weapon_minigun_left");

		this.max_ammo = 300;
		this.bulletDamage = 40;
		this.bulletSpeed = 20;
	}
	
	public void update(){
		super.update();
	}
	
	public void draw(){
		if(player.getDirection().equals("right")){
			drawQuadImageRotLeft(weaponRight, x - 14, y - 2, width, height, angle);
		}else{
			drawQuadImageRotRight(weaponLeft, x, y - 2, width, height, angle - 180);
		}
		
		for(Bullet_Basic bullet : bulletList){
			bullet.draw();
		}
	}
	
	public void shoot(){
		
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 100){
			AMMO_LEFT--;
			float angleOffset = 0;
			// walk right
			if(player.getDirection().equals("right") && destX > (player.getX()+player.getWidth()/2)){
				if(destX < x + (width/2)){
					destX = getRightBorder() - destX;
				}
				
				angleOffset = 0;
				if(angle >= 310){
					angleOffset = 360 - angle; 
				}else{
					angleOffset = -angle;
				}
				angleOffset /= 20;
				
				bulletList.add(new Bullet_Laser(x + 2,(y + (height/2) + angleOffset* -5) + 5, 10, 6, destX, destY + 5, "right", bulletSpeed, angle, new Color(40, 20, 5)));
				bulletList.add(new Bullet_Laser(x + 2,(y + (height/2) + angleOffset* -5) - 5, 10, 6, destX, destY - 5, "right", bulletSpeed, angle, new Color(40, 20, 5)));
			}
			
			// walk left
			if(player.getDirection().equals("left") && destX < (player.getX()+player.getWidth()/2)){
				if(destX > x + (width/2)){
					destX = getLeftBorder() + destX;
				}
				
				angleOffset = 0;
				if(angle <= 230 && angle >= 180){
					angleOffset = angle - 180; 
				}else if(angle < 180){
					angleOffset = -(180 - angle);
				}	
				angleOffset /= 20;
				
				bulletList.add(new Bullet_Laser(x,( y + (height/2) + angleOffset* -8) + 5, 10, 6, destX, destY + 5, "left", bulletSpeed, angle, new Color(25, 15, 5)));
				bulletList.add(new Bullet_Laser(x,( y + (height/2) + angleOffset* -8) - 5, 10, 6, destX, destY - 5, "left", bulletSpeed, angle, new Color(25, 15, 5)));
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
