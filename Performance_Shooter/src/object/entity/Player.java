package object.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static helpers.Leveler.*;
import static helpers.Setup.*;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager;
import Gamestate.StateManager.GameState;
import UI.HeadUpDisplay;
import data.Camera;
import data.Handler;
import object.Tile;
import object.weapon.MapWeapon;
import object.weapon.Minigun;
import object.weapon.Shotgun;
import object.weapon.Weapon;

import static helpers.Graphics.*;

import java.awt.Rectangle;

public class Player implements Entity{
	
	private Weapon weapon;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom, rectRampSensor;
	private Handler handler;
	
	protected float x, y, velX, velY, snowVelX;
	private float speed, gravity;
	private boolean falling, jumping;
	private final float MAX_SPEED = 30;
	private int frameCount;
	private String currentAnimation;
	private String direction;
	private boolean shooting, onIce;
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
		this.speed = 5f;
		this.velX = 0;
		this.velY = 0;
		this.x = x; 
		this.y = y;
		this.direction = "right";
		this.jumping = false;
		this.snowVelX = 1;
		this.falling = true;
		this.frameCount = 100;

		this.gravity = 4;
		this.idleStop = 9;
		this.shooting = false;
		this.onIce = false;
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectRight = new Rectangle((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4);
		this.rectRampSensor = new Rectangle(1, 1, 1, 1);
		this.weapon = new Weapon(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_left"), quickLoaderImage("player/weapon_right"));
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
		if(onIce)
		{
			if(velX > 0)
				velX = snowVelX;
			if(velX < 0)
				velX = -snowVelX;
		}else{
			velX = 0;
		}

		velY = gravity;
		//System.out.println(y + "      " + (HEIGHT - Mouse.getY() - MOVEMENT_Y)); // top 767 - bottom 0
		weapon.calcAngle(Mouse.getX() - MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y);
		updateDirection(Mouse.getX() - MOVEMENT_X, Mouse.getY() - MOVEMENT_Y);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			snowVelX = 1;
			velX += 1;
			currentAnimation = "anim_walkRight";
			direction = "right";
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			snowVelX = 1;
			velX -= 1;
			currentAnimation = "anim_walkLeft";
			direction = "left";
		}		
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			if(snowVelX > 0)snowVelX -= 0.01f;
			if(direction.equals("right"))
				currentAnimation = "anim_idleRight";
			else
				currentAnimation = "anim_idleLeft";
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !jumping && falling)
		{
			jumping = true;
			falling = false;
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			falling = true;
		}
		
		// Shoot
		if(Mouse.isButtonDown(0) && !shooting && Mouse.getY() < HEIGHT - 100)
		{
			if(!(weapon instanceof Minigun))shooting = true;
			//System.out.println(Mouse.getY());
			if(HeadUpDisplay.shotsLeft > 0 || HeadUpDisplay.shotsLeft == -1)weapon.shoot();
			
			if(HeadUpDisplay.shotsLeft == 0)
			{
				HeadUpDisplay.shotsLeft = -1;
				weapon.addToProjectileList();
				setWeapon(new Weapon(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_left"), quickLoaderImage("player/weapon_right")));
				HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_right");
			}
		
				
			
			anim_idleRight.restart();
			anim_idleLeft.restart();
			idleStop = 0;
		}
			
		if(!Mouse.isButtonDown(0))
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
		
		// switch direction -> mouse movement

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
		drawBounds();
	}
	
	public void mapCollision()
	{
		updateBounds();

		for(Tile t : handler.obstacleList)
		{
			Rectangle r = new Rectangle((int)t.getX(), (int)t.getY(), t.getWidth(), t.getHeight());
			
			if(r.intersects(rectRampSensor))
			{
				if(t.getType() == TileType.Ramp_Start)
				{
					//System.out.println(y + " " + ((x+TILE_SIZE - t.getX())/2));
					y = (t.getY() - TILE_SIZE) - ((x+(TILE_SIZE/2) - t.getX())/2);
					return;
				}else if(t.getType() == TileType.Ramp_End)
				{
					//System.out.println(y + " " + ((x+TILE_SIZE - t.getX())));
					y = (t.getY() - TILE_SIZE) - ((x+TILE_SIZE - t.getX()));
					return;
				}
			}
			// bottom
			if(r.intersects(rectBottom))
			{
				if(t.getType() == TileType.Ramp_End)
				{
					if(rectRampSensor.getX() > t.getX()+32)
					{
						y = (float) (r.getY() - TILE_SIZE * 2);
						return;
					}
				}
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					onIce = false;
					velY = 0;
					y = (float) (r.getY() - TILE_SIZE * 2);
				}
				if(t.getType() == TileType.Ice_Basic)
				{
					onIce = true;
					velY = 0;
					y = (float) (r.getY() - TILE_SIZE * 2);
				}
				
				if(t.getType() == TileType.Lava_Light)
				{
					damage(5);
				}

				jumping = false;
				if(t.getType() == TileType.Grass_Round_Half || t.getType() == TileType.Rock_Half)
				{
					//x = t.getX();//
					velX = t.getVelX();
					x += velX;
				}
				
				updateBounds();
			}
			
			// top
			if(r.intersects(rectTop))
			{
				velY = gravity;
				y = (float) (r.getY() + t.getHeight());
				jumping = false;
				updateBounds();
				return;
			}
			
			// left
			if(r.intersects(rectLeft))
			{
				System.out.println("left");
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					velX = 0;
					x = (float) (r.getX() + r.getWidth() - 11);
				}
				updateBounds();
			}
			
			// right
			if(r.intersects(rectRight))
			{
				if(t.getType() != TileType.Ramp_Start && t.getType() != TileType.Ramp_End)
				{
					velX = 0;
					x = (float) (r.getX() - TILE_SIZE) + 11;
				}
				updateBounds();
			}
		}
		
		// AT_ST Walker
		if(handler.at_st_walker != null && !handler.at_st_walker.isEnabled() && Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			if(checkCollision(x, y, TILE_SIZE, TILE_SIZE * 2, handler.at_st_walker.getX(), handler.at_st_walker.getY(), handler.at_st_walker.getWidth(), handler.at_st_walker.getHeight()))
			{
				if(handler.at_st_walker.isAlive())
					enterATST();
			}
		}
		// Speeder
		if(handler.speeder != null && !handler.speeder.isEnabled() && Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			if(checkCollision(x, y, TILE_SIZE, TILE_SIZE * 2, handler.speeder.getX(), handler.speeder.getY(), handler.speeder.getWidth(), handler.speeder.getHeight()))
			{
				if(handler.speeder.isAlive())
					enterSpeeder();
			}
		}
		
		// weapon
		for(MapWeapon w : handler.weaponList)
		{
			if(w.getBounds().intersects(getBounds()))
			{
				// Shotgun
				if(w.getName().equals("Shotgun"))
				{
					setWeapon(new Shotgun(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_shotgun_left"), quickLoaderImage("player/weapon_shotgun_right")));
					handler.weaponList.remove(w);
					HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_shotgun_right");
					
					// set max. ammo
					HeadUpDisplay.shotsLeft = 30;
				}
				// Minigun
				if(w.getName().equals("Minigun"))
				{
					setWeapon(new Minigun(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_minigun_left"), quickLoaderImage("player/weapon_minigun_right")));
					handler.weaponList.remove(w);
					HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_minigun_right");
					
					// set max. ammo
					HeadUpDisplay.shotsLeft = 250;
				}
			}
		}
	}
	
	private void enterATST()
	{
		velX = 0;
		velY = 0;
		handler.at_st_walker.setEnabled(true);
		//System.out.println("AT_ST start...");
		handler.setCurrentEntity(handler.at_st_walker);
		handler.getStatemanager().getGame().setCamera(new Camera(handler.getCurrentEntity()));
		handler.getStatemanager().getGame().getBackgroundHandler().setEntity(handler.getCurrentEntity());
	}
	
	private void enterSpeeder()
	{
		velX = 0;
		velY = 0;
		handler.speeder.setEnabled(true);
		//System.out.println("Speeder start...");
		handler.setCurrentEntity(handler.speeder);
		handler.getStatemanager().getGame().setCamera(new Camera(handler.getCurrentEntity()));
		handler.getStatemanager().getGame().getBackgroundHandler().setEntity(handler.getCurrentEntity());
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad(x + 10, y + 4, 4, (TILE_SIZE * 2) - 8); // left
		drawQuad(x + TILE_SIZE - 14, y + 4, 4, (TILE_SIZE * 2) - 8); // right
		drawQuad(x + 14, y, TILE_SIZE - 28, 4); // top
		drawQuad(x + 14, y + (TILE_SIZE * 2)-4, TILE_SIZE - 28, 4); // bottom
		
		drawQuad((int)x+(TILE_SIZE/2)-2, (int)y + (TILE_SIZE*2) - 16, 4, 4);
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x + 10, (int)y + 4, 4, (TILE_SIZE * 2) - 8); // left
		this.rectRight.setBounds((int)x + TILE_SIZE - 14,(int) y + 4, 4, (TILE_SIZE * 2) - 8); // right
		this.rectTop.setBounds((int)x + 14,(int) y, TILE_SIZE - 28, 4); // top
		this.rectBottom.setBounds((int)x + 14,(int) y + (TILE_SIZE * 2)-4, TILE_SIZE - 28, 4); // bottom
		
		this.rectRampSensor.setBounds((int)x+(TILE_SIZE/2)-2, (int)y + (TILE_SIZE*2) - 16, 4, 4);
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
			frameCount = 115;
		}
	}

	public void damage(float amount) 
	{
		HeadUpDisplay.playerHealth -= amount;
		if(HeadUpDisplay.playerHealth <= 0)
		{
			HeadUpDisplay.playerHealth = 0;
			StateManager.setState(GameState.DEAD);
		}
	}
	
	private void updateDirection(float mouseX, float mouseY)
	{
		if(mouseX > x)
			direction = "right";
		if(mouseX < x)
			direction = "left";
	}
	
	public boolean isOutOfMap()
	{
		if((TILES_HEIGHT * TILE_SIZE) < y)
			return true;
		return false;
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
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, TILE_SIZE, TILE_SIZE*2);
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

	public boolean isShooting() {
		return shooting;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public float getVelY() {
		return velY;
	}

}
