package object;

import framework.entity.GameEntity;
import object.player.playertask.WalkTo;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import java.awt.*;

import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class TestShot implements GameEntity {
	
	private float x, y, width, height, destX, destY, speed;
	private float velX, velY;
	private Image image;
	private WalkTo walkTo;
	
	public TestShot(WalkTo walkTo, float x, float y, float destX, float destY, float width, float height, int speed) {
		this.walkTo = walkTo;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.destX = destX;
		this.destY = destY;
		this.image = quickLoaderImage("tiles/test");

		calculateDirection();
	}
	
	public void update() {
		y += (velY * speed);
		x += velX * speed;;

		if(getBounds().getBounds2D().intersects(getTargetBounds())){
			walkTo.setTestShot(null);
		}
	}
	
	public void draw() {
		drawQuadImage(image, x, y, width, height);
	}
	
	private void calculateDirection() {
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

	private Rectangle getTargetBounds(){
		return new Rectangle((int)(destX - (width / 2)), (int)(destY - (height/2)), (int)width, (int)height);
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
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polygon getBounds() {
		Polygon p = new Polygon();
		p.addPoint((int)x,(int)y);
		p.addPoint((int)(x + width),(int)y);
		p.addPoint((int)(x + width),(int)(y+height));
		p.addPoint((int)x,(int)(y + height));
		return p;
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
