package object.weapon;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.entity.GameEntity;
import framework.shader.Light;

public class Bullet_Basic implements GameEntity, Serializable{

	private static final long serialVersionUID = 4811538936833511096L;
	protected transient Image image;
	protected float x, y, angle, destX, destY, speed;
	protected float velX, velY;
	protected int width, height;
	protected transient Light light;
	protected long spawnTime;
	
	public Bullet_Basic(float x, float y, int width, int height, float destX, float destY, String direction, int speed, float angle){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.angle = angle;
		this.destX = destX;
		this.destY = destY;
		
		this.spawnTime = System.currentTimeMillis();
		
		this.image = quickLoaderImage("player/bullet_basic");
		
		if(direction.equals("right")){
			this.velX = 1;
		}else{
			this.velX = -1;
		}
		calculateDirection();
	}
	
	@Override
	public void update() {
		y += velY * speed;
		x += velX * speed;	
	}

	@Override
	public void draw() {
		drawQuadImageRotCenter(image, x, y, width, height, angle);	
	}
	
	private void calculateDirection(){
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(destX - x);
		float yDistanceFromTarget = Math.abs(destY - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		velX = xPercentOfMovement;
		velY = totalAllowedMovement - xPercentOfMovement;
		
		// set direction based on position of target relative to tower
		if(destY < y)
			velY *= -1;
		
		if(destX < x)
			velX *= -1;	
	}
	
	public boolean isOutOfMap(){
		if(x - width < getLeftBorder()-100 || x > getRightBorder()+100 || y - height< getTopBorder() || y > getBottomBorder()){
			return true;
		}
		return false;
	}
	
	public void die(){
		if(light != null)
			lights.remove(light);
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

	public long getSpawnTime() {
		return spawnTime;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
