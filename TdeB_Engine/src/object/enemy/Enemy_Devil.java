package object.enemy;

import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.loadSpriteSheet;

import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.EnemyType;
import framework.entity.GameEntity;
import framework.shader.Light;
import object.player.Player;

import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

public class Enemy_Devil extends Enemy_Basic{
	
	private CopyOnWriteArrayList<Slime> slimeList;
	private int attackRange;
	private long t1, t2;
	private Light eyeLight;
	private float eyeX, eyeY;

	public Enemy_Devil(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		
		moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Devil_left", TILE_SIZE, TILE_SIZE), 200);
		moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Devil_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.hpFactor = 6;
		this.hp *= hpFactor;	

		this.type = EnemyType.DEVIL;
		this.attackRange = 200;
		
		this.slimeList = new CopyOnWriteArrayList<>();
		this.eyeLight = new Light(new Vector2f(-1000, -1000), 0, 20, 0, 8);
		lights.add(eyeLight);
	}
	
	@Override
	public void update(){
		super.update();
		
		for(Slime s : slimeList) {
			s.update();
			
			// Player collision
			if(s.getBounds().intersects(handler.getPlayer().getBounds())) {
				if(s.isTestShot()) {
					
					t1 = System.currentTimeMillis();
					if(t1 - t2 > 1000) {
						t2 = t1;
						slimeList.add(new Slime(false, x + width/2, y + height/2 , 6, handler.getPlayer().getX()+handler.getPlayer().getWidth()/2, handler.getPlayer().getY()+handler.getPlayer().getHeight()/2, 8f));
					}		
					slimeList.remove(s);
				}else {
					// Slime hit player
					PLAYER_HP -= 4;
					if(handler.getPlayer().getSpeed() >= 3) {
						handler.getPlayer().setSpeed(2f);
					}
					handler.getPlayer().setSlowTimer(System.currentTimeMillis());
					s.die();
					slimeList.remove(s);
				}
			}
			
			// Map collision
			for(GameEntity entity : handler.obstacleList){
				if(entity.getBounds().intersects(s.getBounds())) {
					s.die();
					slimeList.remove(s);
				}
			}
		}
		isPlyerNear(handler.getPlayer());
		
		// calc eyelight position
		if(direction.equals("left")){
			if(moveLeft.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 8);
				eyeY = (int) (y + MOVEMENT_Y + 12);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 8);
				eyeY = (int) (y + MOVEMENT_Y + 16);
			}
			eyeLight.setLocation(new Vector2f(eyeX, eyeY));
		}else{
			if(moveRight.getFrame() == 0){
				eyeX = (int) (x + MOVEMENT_X + 24);
				eyeY = (int) (y + MOVEMENT_Y + 12);
			}else{
				eyeX = (int) (x + MOVEMENT_X + 24);
				eyeY = (int) (y + MOVEMENT_Y + 18);
			}	
			eyeLight.setLocation(new Vector2f(eyeX, eyeY));
		}
	}
	
	@Override
	public void draw(){

		if(direction.equals("right")){
			drawAnimation(moveRight, x, y, width, height);
		}else{
			drawAnimation(moveLeft, x, y, width, height);
		}
		// hp bar
		drawQuadImage(hpBar, x, y - 6, hp / hpFactor, 4);
		
		for(Slime s : slimeList) {
			s.draw();
		}
	}
	
	private boolean isPlyerNear(Player p) {
		
		if((p.getX()+p.getWidth()/2) - attackRange < x && (p.getX()+p.getWidth()/2) + attackRange > x) {
			if((p.getY()+p.getHeight()/2) - attackRange < y && (p.getY()+p.getHeight()/2) + attackRange > y) {
				slimeList.add(new Slime(true, x + width/2, y + height/2 , 6, p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, 32));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void die() {
		for(Slime s : slimeList) {
			s.die();
			slimeList.remove(s);
		}
		lights.remove(eyeLight);
	}
	
	// throw object
	private class Slime {
		
		private boolean testShot;
		private float x, y, velX, velY, speed;
		private Image image;
		private int size;
		private Light light;
		
		private float destX, destY;
		
		public Slime(boolean testShot, float x, float y, int size, float destX, float destY, float speed) {
			this.testShot = testShot;
			this.x = x;
			this.y = y;
			this.size = size;
			
			this.velX = 0;
			this.velY = 0;
			this.speed = speed;
			
			if(!testShot)this.light = new Light(new Vector2f(0, 0), 0, 20, 0, 10);
			if(!testShot)lights.add(light);
			
			this.destX = destX;
			this.destY = destY;
			
			if(!testShot)image = quickLoaderImage("enemy/slime");
			
			calculateDirection();
		}
		
		public void update() {
			if(!testShot)light.setLocation(new Vector2f(x + 2 + MOVEMENT_X, y + 2 + MOVEMENT_Y));
			y += velY * speed;
			x += velX * speed;
		}
		
		public void draw() {
			if(image != null)drawQuadImage(image, x, y, size, size);
		}
		
		private void calculateDirection()
		{
			float totalAllowedMovement = 1.0f;
			float xDistanceFromTarget = Math.abs(destX - x);
			float yDistanceFromTarget = Math.abs(destY - y);
			float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
			float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
			
			velX = xPercentOfMovement;
			velY = totalAllowedMovement - xPercentOfMovement;

			if(destY < y)
				velY *= -1;	
			
			if(destX < x)
				velX *= -1;	
		}
		
		public void die() {
			if(!testShot)lights.remove(light);
		}
		
		public Rectangle getBounds() {
			return new Rectangle((int)x, (int)y, size, size);
		}

		public boolean isTestShot() {
			return testShot;
		}
		
		
	}
}
