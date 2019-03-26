package object.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static helpers.Leveler.*;
import static helpers.Setup.*;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import Enity.Entity;
import Gamestate.StateManager;
import Gamestate.StateManager.GameState;
import UI.HeadUpDisplay;
import data.Handler;
import object.Tile;
import object.weapon.MapCollectable;
import object.weapon.Minigun;
import object.weapon.Shotgun;
import object.weapon.Weapon;
import shader.Light;

import static helpers.Graphics.*;

import java.awt.Rectangle;

public class Player implements Entity{
	
	private Weapon weapon;
	private Rectangle rectLeft, rectRight, rectTop, rectBottom;
	private Handler handler;
	private Image imgPlayer;
	private Light playerLight;
	
	protected float x, y, velX, velY, snowVelX;
	private float speed;

	private boolean shooting;
	
	public Player(float x, float y, Handler handler)
	{
		this.handler = handler;
		this.speed = 4f;
		this.velX = 0;
		this.velY = 0;
		this.x = x; 
		this.y = y;
		

		this.playerLight = new Light(new Vector2f(0, 0), 240, 80, 50, 10);
		this.shooting = false;
		
		this.imgPlayer = quickLoaderImage("player/player_tmp");
		this.rectLeft = new Rectangle((int)x, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectRight = new Rectangle((int)x + TILE_SIZE - 4, (int)y + 4, 4, (TILE_SIZE * 2) - 16);
		this.rectTop = new Rectangle((int)x + 4, (int)y, TILE_SIZE - 8, 4);
		this.rectBottom = new Rectangle((int)x + 4, (int)y + (TILE_SIZE * 2) - 4, TILE_SIZE - 8, 4);
		this.weapon = new Weapon(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_left"), quickLoaderImage("player/weapon_right"));
	}
	
	public void update()
	{	
		velX = 0;
		velY = 0;

		//System.out.println(y + "      " + (HEIGHT - Mouse.getY() - MOVEMENT_Y)); // top 767 - bottom 0
		weapon.calcAngle(Mouse.getX() - MOVEMENT_X, HEIGHT - Mouse.getY() - MOVEMENT_Y);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			velX += 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			velX -= 1;
		}		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			velY = 1;
		}	
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			velY = -1;
		}	
		
		// Shoot
		if(Mouse.isButtonDown(0) && !shooting && Mouse.getY() < HEIGHT - 100)
		{
			shooting = true;
			weapon.shoot();
			
//			if(HeadUpDisplay.shotsLeft == 0)
//			{
//				HeadUpDisplay.shotsLeft = -1;
//				weapon.addToProjectileList();
//				setWeapon(new Weapon(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_left"), quickLoaderImage("player/weapon_right")));
//				HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_right");
//			}
		}
			
		if(!Mouse.isButtonDown(0))
		{
			shooting = false;
		}
		
		// switch direction -> mouse movement

		x += velX * speed;
		y += velY * speed;
		
		// several collision detections
		mapCollision();
		collectableCollision();

		weapon.update();
		//System.out.println("x :  " + x + " y: " + y);
	}
	
	public void draw()
	{	
		weapon.draw();

		//drawBounds();
		

		// move and draw light
		playerLight.setLocation(new Vector2f(x + MOVEMENT_X + 32, y + MOVEMENT_Y + 32));	
		renderSingleLightMouse(shadowObstacleList, playerLight);
		
		drawQuadImage(imgPlayer, x, y, 64, 64);
		
	}
	
	public void mapCollision()
	{
		for(Tile t : handler.obstacleList)
		{
			updateBounds();
			
			// top
			if(t.getBottomBounds().intersects(rectTop))
			{
				velY = 0;
				y = (float) (t.getY() + t.getHeight());
				updateBounds();
			}
			// bottom
			if(t.getTopBounds().intersects(rectBottom))
			{	
				velY = 0;
				y = (float) (t.getY() - TILE_SIZE);
				updateBounds();
			}		
			// left
			if(t.getRightBounds().intersects(rectLeft))
			{
				velX = 0;
				x = (float) (t.getX() + t.getWidth());
				updateBounds();
			}
			// right
			if(t.getLeftBounds().intersects(rectRight))
			{
				velX = 0;
				x = (float) (t.getX() - TILE_SIZE);
				updateBounds();
			}
		}
	}
	
	private void collectableCollision()
	{
		// collectables
		for(MapCollectable w : handler.collectableList)
		{
			if(w.getBounds().intersects(getBounds()))
			{
				// Shotgun
				if(w.getName().equals("Shotgun"))
				{
					setWeapon(new Shotgun(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_shotgun_left"), quickLoaderImage("player/weapon_shotgun_right")));
					handler.collectableList.remove(w);
					HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_shotgun_right");
					
					// set max. ammo
					HeadUpDisplay.shotsLeft = 30;
				}
				// Minigun
				if(w.getName().equals("Minigun"))
				{
					setWeapon(new Minigun(x, y, 70, 35, this, handler, quickLoaderImage("player/weapon_minigun_left"), quickLoaderImage("player/weapon_minigun_right")));
					handler.collectableList.remove(w);
					HeadUpDisplay.hud_weapon = quickLoaderImage("player/weapon_minigun_right");
					
					// set max. ammo
					HeadUpDisplay.shotsLeft = 250;
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void drawBounds()
	{
		drawQuad((int)x, (int)y, 16, TILE_SIZE); // left
		drawQuad((int)x + TILE_SIZE - 16,(int) y, 16, TILE_SIZE); // right
		
		drawQuad((int)x,(int) y, TILE_SIZE, 16); // top
		drawQuad((int)x,(int) y + TILE_SIZE - 16, TILE_SIZE, 16); // bottom
	}
	
	private void updateBounds()
	{
		this.rectLeft.setBounds((int)x, (int)y + 8, 16, TILE_SIZE - 8); // left
		this.rectRight.setBounds((int)x + TILE_SIZE - 16,(int) y + 8, 16, TILE_SIZE - 8); // right
		
		this.rectTop.setBounds((int)x + 8,(int) y, TILE_SIZE - 8, 16); // top
		this.rectBottom.setBounds((int)x + 8,(int) y + TILE_SIZE - 16, TILE_SIZE - 8, 16); // bottom
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
	public void setVelY(float velY) {
		this.velY = velY;
	}
}
