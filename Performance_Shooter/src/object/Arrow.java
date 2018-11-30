package object;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;
import static helpers.Graphics.*;
import static helpers.Setup.getLeftBorder;
import static helpers.Setup.getRightBorder;

import java.awt.Rectangle;

public class Arrow implements Entity{

	private float x, y, velX, velY, speed, gravity, angle, destX, destY, distanceX;
	private int width, height;
	private Image image; 
	private boolean dynamic, dead, stopped;
	private Rectangle bounds;
	private long despawnTimer;
	
	public Arrow(float x, float y, float distanceX, float distanceY, Entity entity)
	{
		this.x = x;
		this.y = y;
		this.distanceX = distanceX;
		this.speed = 3.0f;
		this.width = 32;
		this.height = 16;
		this.dynamic = true;
		this.dead = false;
		this.stopped = false;
		
		
		this.angle = 310;
		
		if(distanceX > 0)
		{
			this.angle = 310;
			image = quickLoaderImage("enemy/Ewok_Arrow_right");
		}
		else{
			image = quickLoaderImage("enemy/Ewok_Arrow_right");
			this.angle = 210;
		}

		
		this.destX = entity.getX();
		this.destY = entity.getY();
		this.bounds = new Rectangle((int)x, (int)y, width, height);
		this.despawnTimer = System.currentTimeMillis();
		
		this.velX = distanceX * 0.004f;
		this.velY = -2;
		this.gravity = 0.04f;
	}
	
	public void update() 
	{
		velY += gravity;
		
		x += velX * speed;
		y += velY * speed;
		
		if(dynamic)calcAngle(destX, destY);
		
		if(stopped)
		{
			if(System.currentTimeMillis() - despawnTimer > 3000)
			{
				dead = true;
			}
		}
	}

	public void draw() 
	{
		drawQuadImageRot(image, x, y, width, height, angle);
	}
	public void calcAngle(float destX, float destY)
	{
		if(distanceX > 0)
		{
			angle += 1f;
			
		    if(angle < 0)
		        angle += 360;
		    
			if(angle > 360)
				angle -= 360;
			
			if(angle == 60)
				dynamic = false;
		}else{
			angle -= 1f;
			
		    if(angle < 0)
		        angle += 360;
		    
			if(angle > 360)
				angle -= 360;
			
			if(angle == 120)
				dynamic = false;
		}

		//System.out.println("Angle: " + angle);
	}
	
	public void stop()
	{
		velX = 0;
		velY = 0;
		gravity = 0;
		speed = 0;
		dynamic = false;
		stopped = true;
		despawnTimer = System.currentTimeMillis();
	}
	
	public Rectangle getBounds()
	{
		bounds.setBounds((int)x, (int)y, width, height);
		return bounds;
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
	public float getVelX() {
		return velX;
	}

	@Override
	public float getVelY() {
		return velY;
	}

	@Override
	public void damage(int amount) {
		// TODO Auto-generated method stub
		
	}

	public boolean isOutOfMap()
	{
		if(x - width < getLeftBorder()-100 || x > getRightBorder()+100)
			return true;
		return false;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}