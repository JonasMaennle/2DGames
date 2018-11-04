package object;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import Enity.Entity;
import data.Handler;
import data.Tile;

import static helpers.Artist.*;

import java.awt.Rectangle;

public class Player implements Entity{
	
	private Weapon weapon;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom;
	private Handler handler;
	
	private float x, y, velX, velY;
	private float speed, gravity;
	private boolean falling, jumping;
	private final float MAX_SPEED = 30;
	private int frameCount;
	private String currentAnimation;
	private String direction;
	private boolean shooting;
	private long timer1, timer2;
	private int idleStop;
	
	// Animations
	private Animation anim_idleRight;
	private Animation anim_walkRight;
	private Animation anim_jumpRight;
	
	private Animation anim_idleLeft;
	private Animation anim_walkLeft;
	private Animation anim_jumpLeft;
	
	public Player(float x, float y, Handler handler)
	{
		this.handler = handler;
		this.speed = 3;
		this.velX = 0;
		this.velY = 0;
		this.x = x; 
		this.y = y;
		this.direction = "right";
		this.jumping = false;
		this.falling = true;
		this.frameCount = 110;
		this.gravity = 4;
		this.idleStop = 9;
		this.shooting = false;
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectRight = new Rectangle((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4);
		this.weapon = new Weapon(x, y, 70, 35, this, handler);
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		
		// Animation stuff
		this.anim_walkRight = new Animation(loadSpriteSheet("player/player_walkRight", TILE_SIZE, TILE_SIZE * 2), 50);
		this.anim_idleRight = new Animation(loadSpriteSheet("player/player_idleRight", TILE_SIZE, TILE_SIZE * 2), 25);
		this.anim_jumpRight = new Animation(loadSpriteSheet("player/player_jumpRight", TILE_SIZE, TILE_SIZE * 2), 20);
		
		this.anim_walkLeft = new Animation(loadSpriteSheet("player/player_walkLeft", TILE_SIZE, TILE_SIZE * 2), 50);
		this.anim_idleLeft = new Animation(loadSpriteSheet("player/player_idleLeft", TILE_SIZE, TILE_SIZE * 2), 25);
		this.anim_jumpLeft = new Animation(loadSpriteSheet("player/player_jumpLeft", TILE_SIZE, TILE_SIZE * 2), 20);
		
		this.currentAnimation = "anim_idleRight";
	}
	
	public void update()
	{	
		velX = 0;
		velY = gravity;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			velX += 1;
			currentAnimation = "anim_walkRight";
			direction = "right";
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			velX -= 1;
			currentAnimation = "anim_walkLeft";
			direction = "left";
		}		
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			if(direction.equals("right"))
				currentAnimation = "anim_idleRight";
			else
				currentAnimation = "anim_idleLeft";
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !jumping && falling)
		{
			jumping = true;
			falling = false;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			falling = true;
		}
		

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !shooting)
		{
			weapon.shoot();
			shooting = true;
			anim_idleRight.restart();
			anim_idleLeft.restart();
			idleStop = 0;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			shooting = false;
		}
		
		// every three sec
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 3000)
		{
			timer2 = timer1;
			if(anim_idleRight.getFrame() == 0 && currentAnimation.equals("anim_idleRight"))
			{
				anim_idleRight.restart();
				idleStop = 9;
			}
			if(anim_idleLeft.getFrame() == 0 && currentAnimation.equals("anim_idleLeft"))
			{
				anim_idleLeft.restart();
				idleStop = 9;
			}
		}
		

		jump();
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
		weapon.update();
		//System.out.println("x :  " + x + " y: " + y);
	}
	
	public void draw()
	{	
		switch (currentAnimation) {
		case "anim_idleRight":
			anim_idleRight.stopAt(idleStop);
			drawAnimation(anim_idleRight, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_walkRight.restart();
			anim_jumpRight.restart();
			anim_walkLeft.restart();
			anim_idleLeft.restart();
			anim_jumpLeft.restart();
			break;
		case "anim_walkRight":
			drawAnimation(anim_walkRight, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_idleRight.restart();
			anim_jumpRight.restart();
			anim_walkLeft.restart();
			anim_idleLeft.restart();
			anim_jumpLeft.restart();
			break;
		case "anim_jumpRight":
			anim_jumpRight.stopAt(4);
			drawAnimation(anim_jumpRight, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_idleRight.restart();
			anim_walkRight.restart();
			anim_walkLeft.restart();
			anim_idleLeft.restart();
			anim_jumpLeft.restart();
			break;
		case "anim_walkLeft":
			drawAnimation(anim_walkLeft, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_idleRight.restart();
			anim_jumpRight.restart();
			anim_walkRight.restart();
			anim_idleLeft.restart();
			anim_jumpLeft.restart();
			break;
		case "anim_idleLeft":
			anim_idleLeft.stopAt(idleStop);
			drawAnimation(anim_idleLeft, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_walkRight.restart();
			anim_jumpRight.restart();
			anim_walkLeft.restart();
			anim_idleRight.restart();
			anim_jumpLeft.restart();
			break;
		case "anim_jumpLeft":
			anim_jumpLeft.stopAt(4);
			drawAnimation(anim_jumpLeft, x, y, TILE_SIZE, TILE_SIZE * 2);
			anim_idleRight.restart();
			anim_walkRight.restart();
			anim_walkLeft.restart();
			anim_idleLeft.restart();
			anim_jumpRight.restart();
			break;
		default:
			break;
		}
		
		weapon.draw();
		//drawBounds();
	}
	
	private void mapCollision()
	{
		updateBounds();

		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), TILE_SIZE, TILE_SIZE);
			
			if(r.intersects(rectTop))
			{
				velY = gravity;
				y = (float) (r.getY() + TILE_SIZE);
				jumping = false;
				return;
			}
			if(r.intersects(rectLeft))
			{
				velX = 0;
				x = (float) (r.getX() + r.getWidth());
			}
			if(r.intersects(rectRight))
			{
				velX = 0;
				x = (float) (r.getX() - TILE_SIZE);
			}
			if(r.intersects(rectBottom))
			{
				velY = 0;
				y = (float) (r.getY() - TILE_SIZE * 2);
				jumping = false;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad(x, y + 4, 4, (TILE_SIZE * 2) - 16); // left
		drawQuad(x + TILE_SIZE - 4, y + 4, 4, (TILE_SIZE * 2) - 16); // right
		drawQuad(x + 4, y, TILE_SIZE - 8, 4); // top
		drawQuad(x + 4, y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4); // bottom
	}
	
	private void jump()
	{
		if(jumping)
		{
			if(direction.equals("right"))
				currentAnimation = "anim_jumpRight";
			else
				currentAnimation = "anim_jumpLeft";
			
			if(jumping && frameCount >= 0)
			{
				
				velY -= frameCount * 0.1;
				frameCount -= 4;
				if(velY > MAX_SPEED)
				{
					velY = MAX_SPEED;
				}
			}
		}else{
			frameCount = 135;
		}
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x, (int)y + 4 , 4, (TILE_SIZE * 2) - 16);
		this.rectRight.setBounds((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop.setBounds((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom.setBounds((int)x + 4, (int)y + (TILE_SIZE * 2)- 4, TILE_SIZE - 8, 4);
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
		return TILE_SIZE;
	}

	@Override
	public int getHeight() 
	{
		return TILE_SIZE * 2;
	}

	@Override
	public void setWidth(int width) 
	{
		
	}

	@Override
	public void setHeight(int height) 
	{
		
	}

	@Override
	public void setX(float x) 
	{
		this.x = x;
	}

	@Override
	public void setY(float y) 
	{
		this.y = y;
	}

	@Override
	public Vector2f[] getVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}
	
	public String getCurrentAnimation()
	{
		return currentAnimation;
	}

	public Animation getAnim_idleRight() {
		return anim_idleRight;
	}

	public void setAnim_idleRight(Animation anim_idleRight) {
		this.anim_idleRight = anim_idleRight;
	}

	public Animation getAnim_walkRight() {
		return anim_walkRight;
	}

	public void setAnim_walkRight(Animation anim_walkRight) {
		this.anim_walkRight = anim_walkRight;
	}

	public Animation getAnim_jumpRight() {
		return anim_jumpRight;
	}

	public void setAnim_jumpRight(Animation anim_jumpRight) {
		this.anim_jumpRight = anim_jumpRight;
	}

	public Animation getAnim_idleLeft() {
		return anim_idleLeft;
	}

	public void setAnim_idleLeft(Animation anim_idleLeft) {
		this.anim_idleLeft = anim_idleLeft;
	}

	public Animation getAnim_walkLeft() {
		return anim_walkLeft;
	}

	public void setAnim_walkLeft(Animation anim_walkLeft) {
		this.anim_walkLeft = anim_walkLeft;
	}

	public Animation getAnim_jumpLeft() {
		return anim_jumpLeft;
	}

	public void setAnim_jumpLeft(Animation anim_jumpLeft) {
		this.anim_jumpLeft = anim_jumpLeft;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Rectangle getRectLeft() {
		return rectLeft;
	}

	public void setRectLeft(Rectangle rectLeft) {
		this.rectLeft = rectLeft;
	}

	public Rectangle getRectRight() {
		return rectRight;
	}

	public void setRectRight(Rectangle rectRight) {
		this.rectRight = rectRight;
	}

	public Rectangle getRectTop() {
		return rectTop;
	}

	public void setRectTop(Rectangle rectTop) {
		this.rectTop = rectTop;
	}

	public Rectangle getRectBottom() {
		return rectBottom;
	}

	public void setRectBottom(Rectangle rectBottom) {
		this.rectBottom = rectBottom;
	}
	
	
}
