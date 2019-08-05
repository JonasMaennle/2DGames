package object.weapon;

import framework.core.Handler;
import object.player.Player;
import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;
import java.util.Random;
import org.newdawn.slick.Image;

public class Weapon_LMG extends Weapon_Basic{
	
	private transient Image weaponRight, weaponLeft;
	private Random rand;
	private long timer1, timer2;
	
	public Weapon_LMG(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		player.setSpeed(3f);
		
		this.weaponRight = quickLoaderImage("player/weapon_lmg_right");
		this.weaponLeft = quickLoaderImage("player/weapon_lmg_left");
		
		
		this.rand = new Random();
		
		this.bulletDamage = 20;
		this.bulletSpeed = 18;
		this.max_ammo = 300;
		this.weaponDelta = 70;
		this.weaponDeltaMAX = 70;
		this.bulletSpeedMAX = 18;
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
		if(timer1 - timer2 > weaponDelta){
			AMMO_LEFT--;
			float destYOffeset = rand.nextFloat() * 32 - 16;
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
				
				bulletList.add(new Bullet_Basic(centX + 4, centY - (angleOffset * 100), 6, 6, destX, destY + destYOffeset, "right", bulletSpeed, angle));
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
				
				bulletList.add(new Bullet_Basic(centX - 4, centY - (angleOffset * 100), 6, 6, destX, destY + destYOffeset, "left", bulletSpeed, angle));
			}	
			timer2 = timer1;
		}
	}
	
	@Override
	public void wipe(){
		bulletList.clear();
		player.setSpeed(4f);
	}
}
