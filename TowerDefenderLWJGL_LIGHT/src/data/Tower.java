package data;

import static helpers.Artist.*;
import static helpers.Clock.delta;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import object.enemy.Enemy;
import shader.Light;

public abstract class Tower implements Entity{

	private float x, y, timeSinceLastShot, firingSpeed, angle;
	private int width, height, range, cost;
	private boolean isPlaced;
	public Enemy target;
	private Texture[] texture;
	private CopyOnWriteArrayList<Enemy> enemies;
	public ArrayList<Projectile> projectiles;
	private boolean targeted;
	public TowerType type;
	protected Light light;
	
	public Tower(TowerType type, Tile startTile, CopyOnWriteArrayList<Enemy> enemies)
	{
		this.type = type;
		this.texture = type.texture;
		this.range = type.range;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = startTile.getWidth();
		this.height = startTile.getHeight();
		this.enemies = enemies;
		this.isPlaced = false;
		this.targeted = false;
		this.timeSinceLastShot = 0f;
		this.projectiles = new ArrayList<>();
		this.firingSpeed = type.firingSpeed;
		this.angle = 0f;
		this.cost = type.cost;
	}	
		
	private Enemy acquireTarget()
	{
		Enemy closest = null;
		// Arbitrary distance (larger than map), to help with sorting enemies
		float closestDistance = 10000;
		// Go through each enemy and find the closest to the tower
		for(Enemy e : enemies)
		{
			if(isInRange(e) && findeDistance(e) < closestDistance && e.getHiddenHealth() > 0)
			{
				closestDistance = findeDistance(e);
				closest = e;
			}
		}
		// If an enemy exists and is returned, targeted == true;
		if(closest != null)
			targeted = true;
		return closest;
	}
	
	private boolean isInRange(Enemy e)
	{
		float xDistance = Math.abs(e.getX() - x);
		float yDistance = Math.abs(e.getY() - y);
		
		if(xDistance < range && yDistance < range)
			return true;
		
		return false;
	}
	
	private float findeDistance(Enemy e)
	{
		float xDistance = Math.abs(e.getX() - x);
		float yDistance = Math.abs(e.getY() - y);
		
		return xDistance+yDistance;
	}
	
	private float calculateAngle()
	{	
		double angleTmp = Math.atan2(target.getY() - y, target.getX() - x);
		return (float) Math.toDegrees(angleTmp) - 90;
	}
	
	// Abstract method
	public abstract void shoot(Enemy target);
	
	public void updateEnemyList(CopyOnWriteArrayList<Enemy> newList)
	{
		enemies = newList;
	}
	
	public void update()
	{
		if(!targeted || target.getHiddenHealth() < 0)
		{
			target = acquireTarget();
		}else{
			angle = calculateAngle();
			if(timeSinceLastShot > firingSpeed)
			{
				shoot(target);
				timeSinceLastShot = 0;
			}
		}
		
		if(target == null || target.isAlive() == false)
			targeted = false;
		
		timeSinceLastShot += delta();
		
		for(Projectile p : projectiles)
			p.update();
		
		if(light != null)
			light.setLocation(new Vector2f(x + width/2, y + height/2));
		if(isPlaced && light != null)
		{
			lights.add(light);
			isPlaced = false;
		}
			
		draw();
	}

	public void draw() {
		drawQuadTex(texture[0], x, y, width, height);

		if(texture.length > 1)
		{
			for(int i = 1; i < texture.length; i++)
			{
				drawQuadTexRot(texture[i], x, y, width, height, angle);
				//drawQuadTex(texture[0], x, y, width, height);
			}
				
		}	
	}
	
	public Vector2f[] getVertices() 
	{
		return new Vector2f[] {
				new Vector2f(x + 10, y + 13), // left top
				new Vector2f(x + 10, y + 53), // left bottom
				new Vector2f(x + 55, y + 53), // right bottom
				new Vector2f(x + 55, y + 13) // right top
		};
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
	
	public Enemy getTarget()
	{
		return target;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isPlaced() {
		return isPlaced;
	}

	public void setPlaced(boolean isPlaced) {
		this.isPlaced = isPlaced;
	}
}
