package object.player;

import static framework.helper.Collection.*;
import static framework.helper.Graphics.*;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import framework.core.Handler;
import framework.entity.GameEntity;
import shader.Light;

public class Player implements GameEntity{
	
	private int width, height;
	private float x, y, velX, velY, speed;
	private String direction;
	private Image placeholder;
	private Handler handler;
	private Light playerLight;
	
	private Weapon_Basic weapon;
	private boolean isShooting;
	
	public Player(int x, int y, Handler handler)
	{
		this.handler = handler;
		
		this.x = x;
		this.y = y;
		this.width = 32;
		this.height = 32;
		this.speed = 4f;
		this.direction = "right";
		this.isShooting = false;
		
		this.placeholder = quickLoaderImage("player/Player_tmp");
		this.playerLight = new Light(new Vector2f(0, 0), 255, 128, 0, 15);
		
		this.weapon = new Weapon_Pistol(16, 8, this, handler);
		lights.add(playerLight);
	}

	public void update() {
		velX = 0;
		velY = 0;
		
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
		if(Mouse.isButtonDown(0) && !isShooting){
			if(weapon instanceof Weapon_Pistol){
				isShooting = true;
				weapon.shoot();
			}
		}	
		if(!Mouse.isButtonDown(0)){
			isShooting = false;
		}
		
		x += velX * speed;
		y += velY * speed;
		
		mapCollision();
		
		updateDirection();
		weapon.update();
	}

	private void mapCollision() {
		for(GameEntity ge : handler.obstacleList)
		{
			// top
			if(ge.getBottomBounds().intersects(getTopBounds()))
			{
				velY = 0;
				y = (float) (ge.getY() + ge.getHeight());
			}
			// bottom
			if(ge.getTopBounds().intersects(getBottomBounds()))
			{	
				velY = 0;
				y = (float) (ge.getY() - TILE_SIZE);
			}		
			// left
			if(ge.getRightBounds().intersects(getLeftBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() + ge.getWidth());
			}
			// right
			if(ge.getLeftBounds().intersects(getRightBounds()))
			{
				velX = 0;
				x = (float) (ge.getX() - TILE_SIZE);
			}	
		}
	}

	public void draw() {
		playerLight.setLocation(new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 16));
		drawQuadImage(placeholder, x, y, width, height);
		
		weapon.draw();
	}
	
	@SuppressWarnings("unused")
	private void drawBounds(){
		drawQuad((int)x, (int)y, 16, TILE_SIZE); // left
		drawQuad((int)x + TILE_SIZE - 16,(int) y, 16, TILE_SIZE); // right
		
		drawQuad((int)x,(int) y, TILE_SIZE, 16); // top
		drawQuad((int)x,(int) y + TILE_SIZE - 16, TILE_SIZE, 16); // bottom
	}
	
	private void updateDirection()
	{
		float mouseX = Mouse.getX() - MOVEMENT_X;
		
		if(mouseX > x)
			direction = "right";
		if(mouseX < x)
			direction = "left";
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
		this.x = (int)x;
	}

	@Override
	public void setY(float y) {
		this.y = (int)y;
	}

	@Override
	public Vector2f[] getVertices() {
		return null;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}

	@Override
	public Rectangle getTopBounds() {
		return new Rectangle((int)x + 8,(int) y, TILE_SIZE - 8, 16);
	}

	@Override
	public Rectangle getBottomBounds() {
		return new Rectangle((int)x + 8,(int) y + TILE_SIZE - 16, TILE_SIZE - 8, 16);
	}

	@Override
	public Rectangle getLeftBounds() {
		return new Rectangle((int)x, (int)y + 8, 16, TILE_SIZE - 8);
	}

	@Override
	public Rectangle getRightBounds() {
		return new Rectangle((int)x + TILE_SIZE - 16,(int) y + 8, 16, TILE_SIZE - 8);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public Weapon_Basic getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon_Basic weapon) {
		this.weapon = weapon;
	}
}
