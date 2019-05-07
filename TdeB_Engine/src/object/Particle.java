package object;

import java.awt.Rectangle;
import java.util.Random;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import framework.shader.Light;

public class Particle {
	
	private Image particles;
	private Random rand;
	private int x, y, width, height;
	private float velX, velY, speed, angle;
	private Light light;
	
	public Particle(int x, int y, int width, int height, float velX, float velY, float speed, String color, int lightProbability){
		this.x = x;
		this.y = y;
		this.rand = new Random();
		this.velX = velX;
		this.velY = velY;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.angle = 0;
		if(lightProbability > 80){ // 85 for laptop to run smooth
			light = new Light(new Vector2f(0, 0), 20, 6, 0, 4);
			lights.add(light);
		}
		if(color.equals("orange"))particles = quickLoaderImage("tiles/Lava_" + rand.nextInt(5));
	}
	
	public void update(){
		x += velX * speed;
		y += velY * speed;

		if(light != null)
			light.setLocation(new Vector2f(x + MOVEMENT_X + width/2, y + MOVEMENT_Y + height/2));
	}
	
	public void mapCollision(Handler handler){
		if(speed == 0)
			return;
		
		for(GameEntity ge: handler.obstacleList){
			if(ge.getBounds().intersects(getBounds())){
				speed = 0;
			}
		}
	}
	
	public void die(){
		lights.remove(light);
	}
	
	public void stop(){
		speed = 0;
	}
	
	public void draw(){
		drawQuadImageRotCenter(particles, x, y, width, height, angle);
	}
	
	public boolean isOutOfMap(){
		if(x < getLeftBorder() || x > getRightBorder() || y < getTopBorder() || y > getBottomBorder())
			return true;
		
		return false;
	}
	
	public boolean isOutOfMapBottom(){
		if(y > getBottomBorder())
			return true;
		return false;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, width, height);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}
}
