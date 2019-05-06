package object.player;

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

public class Weapon_Basic implements GameEntity{

	protected float x, y, angle, destX, destY;
	protected int width, height;
	protected Player player;
	protected Handler handler;
	protected Image default_weapon;
	protected int bulletSpeed;
	protected int bulletDamage;
	
	protected CopyOnWriteArrayList<Bullet_Basic> bulletList;
	
	public Weapon_Basic(int width, int height, Player player, Handler handler) {
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
		this.width = width;
		this.height = height;
		
		this.angle = 0;
		this.destX = 0;
		this.destY = 0;
		
		this.bulletSpeed = 5;
		this.bulletDamage = 16;
		
		this.handler = handler;
		this.default_weapon = quickLoaderImage("player/weapon");
		this.bulletList = new CopyOnWriteArrayList<Bullet_Basic>();
	}
	
	@Override
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
	
	protected void shoot(){
		// walk right
		if(player.getDirection().equals("right") && destX > (player.getX()+player.getWidth()/2)){
			if(destX < x + (width/2)){
				destX = getRightBorder() - destX;
			}
			bulletList.add(new Bullet_Basic(x + 2, y + (height/2) - 4, 6, 6, destX, destY, "right", bulletSpeed, angle));
		}
		
		// walk left
		if(player.getDirection().equals("left") && destX < (player.getX()+player.getWidth()/2)){
			if(destX > x + (width/2)){
				destX = getLeftBorder() + destX;
			}
			bulletList.add(new Bullet_Basic(x + width - 6, y + (height/2) - 4, 6, 6, destX, destY, "left", bulletSpeed, angle));
		}
	}
	
	// Calc Angle in degree between x,y and destX,destY <- nice
	private void calcAngle(float destX, float destY){
		angle = (float) Math.toDegrees(Math.atan2(destY - (y), destX - (x)));

	    if(angle < 0){
	        angle += 360;
	    }
	    // block angle 320 - 360 && 0 - 30
	    if(player.getDirection().equals("right")){
	    	if(angle < 310 && angle > 180){
	    		angle = 310;
	    	}else if(angle > 40 && angle < 180){
	    		angle = 40;
	    	}else{
	    		this.destX = destX;
	    		this.destY = destY;
	    	}
	    }
	    // block angle if 220 - 180 && 180 - 150
	    if(player.getDirection().equals("left")){
	    	if(angle > 230 && angle < 360){
	    		angle = 230;
	    	}else if(angle < 140 && angle > 0){
	    		angle = 140;
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
}
