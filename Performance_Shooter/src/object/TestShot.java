package object;

import org.lwjgl.util.vector.Vector2f;

import Enity.Entity;

import static helpers.Graphics.*;
import static helpers.Setup.*;

import java.awt.Rectangle;

public class TestShot implements Entity{
	
	private float x, y, width, height, destX, destY, speed;
	private float velX, velY;
	
	public TestShot(float x, float y, float destX, float destY, float width, float height, int speed)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.destX = destX;
		this.destY = destY;

		calculateDirection();
	}
	
	public void update()
	{
		y += velY * speed;
		x += velX * speed;;
	}
	
	public void draw()
	{
		drawQuad(x, y, width, height);
	}
	
	public boolean isOutOfMap()
	{
		if(x < getLeftBorder() || x > getRightBorder() || y > getBottomBorder() || y < getTopBorder())
			return true;
		return false;
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

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return (int) width;
	}

	@Override
	public int getHeight() 
	{
		return (int) height;
	}

	@Override
	public void setWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
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
	public void damage(float amount) {
		// TODO Auto-generated method stub
		
	}

	public float getDestX() {
		return destX;
	}

	public void setDestX(float destX) {
		this.destX = destX;
	}

	public float getDestY() {
		return destY;
	}

	public void setDestY(float destY) {
		this.destY = destY;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
}
