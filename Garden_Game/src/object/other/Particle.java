package object.other;

import java.awt.Rectangle;

import org.newdawn.slick.Image;
import static framework.helper.Graphics.*;
import static framework.helper.Collection.*;

public class Particle {
	private Image image;
	private float x, y;
	private int  width, height;
	private float velX, velY, speed, angle;
	private boolean toggleFadeout;
	private float alphaValue;
	private boolean dead;
	
	public Particle(int x, int y, int width, int height, float velX, float velY, float speed, Image image, float angle){
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.angle = angle;
		this.image = image;
		this.toggleFadeout = false;
		this.dead = false;
		this.alphaValue = 1;

		// if(color.equals("orange"))particles = quickLoaderImage("particles/Lava_" + rand.nextInt(5));
	}
	
	public void update(){
		speed *= 0.98f;
		if(speed < 1f)
			toggleFadeout = true;
		
		if(alphaValue < 0.1)
			dead = true;
		
		x += velX * speed;
		y += velY * speed;
	}
	
	public void draw(){
		if(toggleFadeout) {
			drawQuadImageRotCenter(image, x, y, width, height, angle, alphaValue);
			alphaValue *= 0.95f;
		}else {
			drawQuadImageRotCenter(image, x, y, width, height, angle);
		}

	}
	
	public boolean isOutOfMap(){
		if(x < getLeftBorder() || x > getRightBorder() || y < getTopBorder() || y > getBottomBorder())
			return true;
		
		return false;
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, width, height);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public boolean isDead() {
		return dead;
	}
}
