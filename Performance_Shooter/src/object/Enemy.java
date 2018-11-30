package object;

import static helpers.Graphics.drawQuad;
import static helpers.Leveler.TILES_HEIGHT;
import static helpers.Setup.TILE_SIZE;

import java.awt.Rectangle;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import Enity.Entity;
import data.TileGrid;
import shader.Light;


public abstract class Enemy implements Entity{

	protected int width, height;
	protected float speed, x, y, health, velX, velY, angle, destX, destY;
	protected boolean alive;
	protected float gravity;
	protected TileGrid grid;
	protected Light light;
	protected Rectangle rectLeft, rectRight, rectTop, rectBottom;
	protected float tX, tY, tVelX, tVelY;
	protected CopyOnWriteArrayList<Laser> laserList;
	protected Entity entity;
	protected Random rand;
	protected TestShot testShot;
	protected long lastShot;
	
	protected String direction;
	
	protected Image idleLeft, idleRight;
	
	protected Animation anim_walkRight;
	protected Animation anim_walkLeft;
	
	// Default constructor
	public Enemy(float x, float y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = 50;
		this.grid = grid;
		this.health = 100;
		this.alive = true;
		this.angle = 0;
		this.destX = 0;
		this.destY = 0;
		this.laserList = new CopyOnWriteArrayList<>();
		this.gravity = 0;
		
		this.tX = x;
		this.tY = y;
		//this.testShot = new TestShot(x + (width/2), y + 45, 0, 0, 4, 4, 5);
		
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (height) - 16);
		this.rectRight = new Rectangle((int)x + width - 4, (int)y + 4, 4, (height) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, width - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (height) - 4, width - 8, 4);
		
		this.rand = new Random();
	}
	
	public void update()
	{

	}
	
	public void updateBounds()
	{
		rectLeft.setBounds((int)x, (int)y + 4, 4, (height) - 16);
		rectRight.setBounds((int)x + width - 4, (int)y + 4, 4, (height) - 16);
		rectTop.setBounds((int)x + 4, (int)y, width - 8, 4);
		rectBottom.setBounds((int)x + 4, (int)y + (height) - 4, width - 8, 4);
	}
	
	public void drawBounds()
	{
		drawQuad(x, y + 4, 4, (height) - 16); // left
		drawQuad(x + width - 4, y + 4, 4, (height) - 16); // right
		drawQuad(x + 4, y, width - 8, 4); // top
		drawQuad(x + 4, y + (height) - 4, width - 8, 4); // bottom
	}
	
	// Take damage from external source
	public void damage(int amount)
	{
			
	}
	
	public void draw()
	{

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public TileGrid getTileGrid()
	{
		return grid;
	}

	public boolean isAlive() {
		return alive;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isOutOfMap()
	{
		if((TILES_HEIGHT * TILE_SIZE) < y)
			return true;
		return false;
	}
	
	
	public float getVelX() {
		return velX;
	}

	public float getVelY() {
		return velY;
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, width, height);
	}
}
