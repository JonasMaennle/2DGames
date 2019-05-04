package object.player;

import framework.core.Handler;

import static framework.helper.Collection.getLeftBorder;
import static framework.helper.Collection.getRightBorder;
import static framework.helper.Graphics.*;

import java.util.Random;

import org.newdawn.slick.Image;

public class Weapon_LMG extends Weapon_Basic{
	
	private Image weaponRight, weaponLeft;
	private Random rand;
	private long timer1, timer2;
	
	public Weapon_LMG(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		player.setSpeed(player.getSpeed() * 0.75f);
		
		this.weaponRight = quickLoaderImage("player/weapon_lmg_right");
		this.weaponLeft = quickLoaderImage("player/weapon_lmg_left");
		
		this.bulletDamage = 12;
		this.bulletSpeed = 18;
		this.rand = new Random();
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
		if(timer1 - timer2 > 70){
			
			float destYOffeset = rand.nextFloat() * 32 - 16;
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
				
				bulletList.add(new Bullet_Basic(x + 4, y + (height/2)- 2 + angleOffset* -5, 6, 6, destX, destY + destYOffeset, "right", bulletSpeed, angle));
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
				
				bulletList.add(new Bullet_Basic(x + 4, y + (height/2) - 2 + angleOffset* -8, 6, 6, destX, destY + destYOffeset, "left", bulletSpeed, angle));
			}
			
			timer2 = timer1;
		}
	}
	
	@Override
	public void wipe(){
		bulletList.clear();
		player.setSpeed(player.getSpeed() / 0.75f);
	}
}
