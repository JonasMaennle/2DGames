package object.weapon;

import java.util.Random;

import static framework.helper.Collection.AMMO_LEFT;
import static framework.helper.Graphics.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import object.Particle;
import object.enemy.Enemy_Basic;
import object.player.Player;

public class Weapon_Flamethrower extends Weapon_Basic{
	
	private CopyOnWriteArrayList<Particle> fireList;
	private Random rand;
	private transient Image weaponRight, weaponLeft;

	public Weapon_Flamethrower(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		
		this.fireList = new CopyOnWriteArrayList<>();
		this.rand = new Random();	
		
		this.weaponRight = quickLoaderImage("player/weapon_flamethrower_right");
		this.weaponLeft = quickLoaderImage("player/weapon_flamethrower_left");
		
		this.bulletDamage = 2;
		this.max_ammo = 999;
		this.bulletSpeed = 150; // range in this case
		this.bulletSpeedMAX = 150;
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
		
		// update fire
		for(Particle p : fireList){
			p.update();
				
			for(Enemy_Basic e : handler.enemyList){
				if(e.getBounds().intersects(p.getBounds())){
					p.die();
					//fireList.remove(p);
					e.setHp(e.getHp()-bulletDamage);
					
					// trigger enemy if enemy is out of range
					if(e.getSpeed() == 0)
						e.setSpeed(2);
				}
			}
			
			if(isBehindPlayer(p)){
				p.die();
				fireList.remove(p);
			}

			if(calcDistance(p.getX(), p.getY(),this.x, this.y) > rand.nextInt(50) + bulletSpeed){
				p.die();
				fireList.remove(p);
			}
			
			if(p.getVelX() * p.getSpeed() < 1 && p.getVelX() * p.getSpeed() > -1){
				p.die();
				fireList.remove(p);
			}
			
			for(GameEntity ge : handler.obstacleList){
				if(ge.getBounds().intersects(p.getBounds())){
					p.die();
					fireList.remove(p);
				}
			}		
		}
	}
	
	public void draw(){
		
		// draw fire
		for(Particle p : fireList){
			p.draw();
		}		

		if(player.getDirection().equals("right")){
			drawQuadImageRotLeft(weaponRight, x, y, width, height, angle);
		}else{
			drawQuadImageRotRight(weaponLeft, x, y, width, height, angle - 180);
		}
	}
	
	public void shoot(){
		
		addFireParticle();
		addFireParticle();
		addFireParticle();
		addFireParticle();
		addFireParticle();
		
		AMMO_LEFT--;
	}
	
	private void addFireParticle(){
		float velX = 0;
		float velY = 0;
		float angleOffset = 0;
		
		if(player.getDirection().equals("right")){
			angleOffset = 0;
			if(angle >= 270){
				angleOffset = 360 - angle; 
			}else{
				angleOffset = -angle;
			}
			angleOffset /= 360;
			
			velX = (rand.nextFloat() * 3 + 1) - (Math.abs(angleOffset*10)) * (1 - Math.abs(angleOffset));
			velY = (rand.nextFloat() * 1.0f - 0.5f) - (angleOffset*10);
			
			//System.out.println(velX + "            " + velY);
			              // Particle(int x,          int y,                               int width,           int height,          float velX,            float velY,loat speed,     String color,int lightProbability)
			fireList.add(new Particle((int)centX + 12, (int)(centY - (angleOffset * 100)), rand.nextInt(4) + 4, rand.nextInt(4) + 4, velX + player.getVelX(), velY, rand.nextInt(4)+4, "orange", rand.nextInt(100)));
		}else{
			angleOffset = 0;
			if(angle <= 270 && angle >= 180){
				angleOffset = angle - 180; 
			}else if(angle < 180){
				angleOffset = -(180 - angle);
			}	
			angleOffset /= 360;
			
			velX = -((rand.nextFloat() * 3 + 1) - (Math.abs(angleOffset*10)));
			velY = (rand.nextFloat() * 1.0f - 0.5f) - (angleOffset*10);
			
			fireList.add(new Particle((int)centX - 12, (int)(centY - (angleOffset * 100)), rand.nextInt(4) + 4, rand.nextInt(4) + 4, velX + player.getVelX(), velY, rand.nextInt(5)+4, "orange", rand.nextInt( 100)));
		}
		//                Particle( x,  y, width, height, velX, velY, speed, String color, angle){
	}
	
	private float calcDistance(float x, float y, float destX, float destY){
		float xDistanceFromTarget = Math.abs(destX - x);
		float yDistanceFromTarget = Math.abs(destY - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		
		return totalDistanceFromTarget;
	}
	
	private boolean isBehindPlayer(Particle p){
		if(player.getDirection().equals("right")){
			if(x-player.getWidth() > p.getX())
				return true;
		}
		if(player.getDirection().equals("left")){
			if(x+player.getWidth() < p.getX())
				return true;
		}
		return false;
	}
	
	public void wipe(){
		for(Particle p : fireList){
			p.die();
			fireList.remove(p);
		}
	}
}
