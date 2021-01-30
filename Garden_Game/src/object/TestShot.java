package object;

import framework.core.pathfinding.Graph;
import framework.entity.GameEntity;
import object.player.playertask.WalkTo;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import java.awt.*;

import static framework.helper.Collection.convertObjectCoordinatesToIsometricGrid;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

public class TestShot {
	
	private float x, y, width, height, destX, destY, speed;
	private float velX, velY;
	private Image image;
	private WalkTo walkTo;
	private Graph graph;
	
	public TestShot(WalkTo walkTo, float x, float y, float destX, float destY, float width, float height, int speed) {
		this.walkTo = walkTo;
		this.graph = walkTo.getPlayer().getHandler().getGraph();
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
		// test if testShot is over empty node -> doComplexPathfinding
		Vector2f point = convertObjectCoordinatesToIsometricGrid((int)x, (int)y);
		if(graph.getNode((int)point.x, (int)point.y) == null){
			walkTo.setDoComplexPathfinding(true);
			walkTo.setTestShot(null);
			return;
		}

		y += (velY * speed);
		x += velX * speed;;

		// mouse target hit? -> doSimplePathfinding
		if(getBounds().getBounds2D().intersects(getTargetBounds())){
			walkTo.setDoSimplePathfinding(true);
			walkTo.setTestShot(null);
			return;
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

	public float getX() 
	{
		return x;
	}

	public float getY() 
	{
		return y;
	}

	public int getWidth()
	{
		return (int) width;
	}

	public int getHeight() 
	{
		return (int) height;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,(int)width,(int)height);
	}
}
