package object.enemy;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.*;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.PLAYER_HP;
import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.*;
import static framework.helper.Graphics.quickLoaderImage;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.shader.Light;
import object.weapon.Laser_Basic;

public abstract class Enemy_Basic implements GameEntity{

	protected float x, y;
	protected float speed, velX, velY, max_speed;
	protected float spawnX, spawnY;
	protected int width, height;
	protected float hp;
	protected float angle;
	protected String direction;
	
	protected int hpFactor;
	protected int playerRange;
	
	protected Handler handler;
	protected Image image;
	protected Image hpBar;
	
	protected Animation moveLeft;
	protected Animation moveRight;
	protected Light light;

	public Enemy_Basic(int x, int y, int width, int height, Handler handler){
		this.x = x;
		this.y = y;
		this.spawnX = x;
		this.spawnY = y;
		
		this.width = width;
		this.height = height;
		this.image = quickLoaderImage("enemy/Enemy_tmp");
		this.hpBar = quickLoaderImage("enemy/healthForeground");
		
		this.max_speed = 4;
		this.speed = 0;
		this.velX = 0;
		this.velY = 0;
		this.hp = 32;
		
		this.handler = handler;
		this.direction = "right";

		this.playerRange = HEIGHT;
	}

	public void update() {
		velX = 0;
		velY = 0;
		
		if(!handler.getPlayer().getBounds().intersects(getBounds())) {
			calcDirectPathToPlayer(handler.getPlayer().getX() + handler.getPlayer().getWidth()/2, handler.getPlayer().getY() + handler.getPlayer().getHeight()/2);
		}

		// set direction
		if(velX == -1)
			direction = "left";
		if(velX == 1)
			direction = "right";

		x += (velX * speed);
		y += (velY * speed);
		
		damagePlayer();
		mapCollision();
		isPlayerInRange(playerRange);
		calcNoseAngle();
		
		if(light != null)
			light.setLocation(new Vector2f(x + width/2 + MOVEMENT_X, y + height/2 + MOVEMENT_Y));
	}
	
	protected void calcNoseAngle() {
		angle = (float) Math.toDegrees(Math.atan2(handler.getPlayer().getY() + handler.getPlayer().getHeight()/2 - (y), handler.getPlayer().getX() + handler.getPlayer().getWidth()/2 - (x)));
	    angle += 90;
	    angle %= 360;
		if(angle < 0){
	        angle += 360;
	    }
	}

	public void draw() {
		drawQuadImageRotCenter(image, x, y, width, height, angle);
	}
	
	private void calcDirectPathToPlayer(float playerX, float playerY) {
		
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(playerX - x);
		float yDistanceFromTarget = Math.abs(playerY - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		velX = xPercentOfMovement;
		velY = totalAllowedMovement - xPercentOfMovement;

		if(playerY < y)
			velY *= -1;
		
		if(playerX < x)
			velX *= -1;	
	}
	
	public void isPlayerInRange(int borderOffset){
		if(x > handler.getPlayer().getX() - borderOffset && x < handler.getPlayer().getX() + borderOffset){
			if(y > handler.getPlayer().getY() - borderOffset && y < handler.getPlayer().getY() + borderOffset){
				speed = max_speed;
			}
		}else
			return;
	}
	
	protected void damagePlayer(){
		Rectangle rangePlayer = new Rectangle((int)handler.getPlayer().getX() - 8, (int)handler.getPlayer().getY() - 8, (int)handler.getPlayer().getWidth() + 16, (int)handler.getPlayer().getHeight() + 16);
		if(rangePlayer.intersects(getBounds())){
			PLAYER_HP -= 1;
		}
	}
	
	protected void mapCollision() {
		
		// other enemy collision
		for(Enemy_Basic ge : handler.getEnemyList()){
			if(ge.getSpeed() != 0){
				int boxingOffet = 2;
				// top
				if(ge.getBottomBounds().intersects(getTopBounds()))
				{
					velY = 0;
					y = (ge.getY() + ge.getHeight() + boxingOffet);
				}
				// bottom
				if(ge.getTopBounds().intersects(getBottomBounds()))
				{	
					velY = 0;
					y = (ge.getY() - TILE_SIZE - boxingOffet);
				}		
				// left
				if(ge.getRightBounds().intersects(getLeftBounds()))
				{
					velX = 0;
					x = (ge.getX() + ge.getWidth() + boxingOffet);
				}
				// right
				if(ge.getLeftBounds().intersects(getRightBounds()))
				{
					velX = 0;
					x = (ge.getX() - TILE_SIZE - boxingOffet);
				}	
			}
		}
		bulletCollision();
	}
	
	private void bulletCollision() {
		for(Laser_Basic laser : handler.getLaserList()) {
			if(laser.getBounds().intersects(this.getBounds())) {
				laser.die();
				handler.getLaserList().remove(laser);
				
				// damage to enemy
				if(light != null)lights.remove(light);
				hp = 0;
			}
		}
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
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Vector2f[] getVertices() {
		float radius = (width / 2);
		float t = (float) Math.toRadians(angle);
		
		float yT = (float) (Math.cos(t) * radius) * -1;
		float xT = (float) (Math.sin(t) * radius);
		
		t = (float) Math.toRadians(angle - 120);
		
		float yL = (float) (Math.cos(t) * radius) * -1;
		float xL = (float) (Math.sin(t) * radius);
		
		t = (float) Math.toRadians(angle - 240);
		
		float yR = (float) (Math.cos(t) * radius) * -1;
		float xR = (float) (Math.sin(t) * radius);
		
		
		return new Vector2f[] {
				new Vector2f(x + MOVEMENT_X + width / 2 + xT, y + MOVEMENT_Y + height / 2 + yT), // center top
				new Vector2f(x + MOVEMENT_X + width / 2 + xL, y + MOVEMENT_Y + height / 2 + yL), // left bottom
				new Vector2f(x + MOVEMENT_X + width / 2 + xR, y + MOVEMENT_Y + height / 2 + yR), // right bottom
		};
	}
	
	public Rectangle getTopBounds(){
		return new Rectangle((int)x + 4, (int)y, width - 8, 4);
	}
	
	public Rectangle getBottomBounds(){
		return new Rectangle((int)x + 4, (int)y + height-4, width - 8, 4);
	}
	
	public Rectangle getLeftBounds(){
		return new Rectangle((int)x, (int)y + 4, 4, height - 8);
	}
	
	public Rectangle getRightBounds(){
		return new Rectangle((int)x + width-4, (int)y + 4, 4, height - 8);
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(float max_speed) {
		this.max_speed = max_speed;
	}
}