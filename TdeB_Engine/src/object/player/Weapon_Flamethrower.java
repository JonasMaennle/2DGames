package object.player;

import java.util.Random;
import static framework.helper.Graphics.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import object.Particle;
import object.enemy.Enemy_Basic;

public class Weapon_Flamethrower extends Weapon_Basic{
	
	private CopyOnWriteArrayList<Particle> fireList;
	private Random rand;
	private int flamethrowerRange;
	private Image weaponRight, weaponLeft;

	public Weapon_Flamethrower(int width, int height, Player player, Handler handler) {
		super(width, height, player, handler);
		
		this.fireList = new CopyOnWriteArrayList<>();
		this.rand = new Random();
		this.flamethrowerRange = 150;
		
		this.weaponRight = quickLoaderImage("player/weapon_flamethrower_right");
		this.weaponLeft = quickLoaderImage("player/weapon_flamethrower_left");
	}
	
	public void update(){
		super.update();
		
		// update fire
		for(Particle p : fireList){
			p.update();
				
			for(Enemy_Basic e : handler.enemyList){
				if(e.getBounds().intersects(p.getBounds())){
					p.die();
					fireList.remove(p);
					e.setHp(e.getHp()-1);
				}
			}
			
			if(isBehindPlayer(p)){
				p.die();
				fireList.remove(p);
			}

			if(calcDistance(p.getX(), p.getY(),this.x, this.y) > rand.nextInt(50)+flamethrowerRange){
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
			drawQuadImageRotLeft(weaponRight, x - 12, y - 2, width, height, angle);
		}else{
			drawQuadImageRotRight(weaponLeft, x, y - 2, width, height, angle - 180);
		}
	}
	
	public void shoot(){
		
		addFireParticle();
		addFireParticle();
		addFireParticle();
		addFireParticle();
		addFireParticle();
	}
	
	private void addFireParticle(){
		float velX = 0;
		float velY = 0;
		float angleOffset = 0;
		
		if(player.getDirection().equals("right")){
			angleOffset = 0;
			if(angle >= 310){
				angleOffset = 360 - angle; 
			}else{
				angleOffset = -angle;
			}
			angleOffset /= 20;
			velX = rand.nextFloat() * 4 + 1;
			velY = rand.nextFloat() * 1.0f - 0.5f - angleOffset;
			fireList.add(new Particle((int)x + 12, (int)y + 12 + (int)angleOffset*-10, rand.nextInt(4) + 4, rand.nextInt(4) + 4, velX + player.getVelX(), velY, rand.nextInt(3)+4, "orange", rand.nextInt(100)));
		}else{
			angleOffset = 0;
			if(angle <= 230 && angle >= 180){
				angleOffset = angle - 180; 
			}else if(angle < 180){
				angleOffset = -(180 - angle);
			}	
			angleOffset /= 20;
			velX = -(rand.nextFloat() * 4 + 1);
			velY = rand.nextFloat() * 1.0f - 0.5f - angleOffset;
			
			fireList.add(new Particle((int)x , (int)y + 12 + (int)angleOffset*-10, rand.nextInt(4) + 4, rand.nextInt(4) + 4, velX + player.getVelX(), velY, rand.nextInt(3)+4, "orange", rand.nextInt( 100)));
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
