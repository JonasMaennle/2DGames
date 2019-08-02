package object.player;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.lights;
import static framework.helper.Graphics.*;

import framework.entity.GameEntity;
import framework.shader.Light;

public class Shield implements GameEntity{
	
	private int x, y, width, height;
	private float engeryLeft;
	private Image image, shield_bar;
	
	private Light light;
	private Player player;
	
	public Shield(int x, int y, Player player) {
		this.x = x;
		this.y = y;
		this.width = 64;
		this.height = 64;
		
		this.player = player;
		this.engeryLeft = 64;
		
		this.shield_bar = quickLoaderImage("player/shield_bar");
		this.image = quickLoaderImage("player/shield");
		this.light = new Light(new Vector2f(x, y), 0, 100, 95, 30);
		
		lights.add(light);
	}

	@Override
	public void update() {
		light.setLocation(new Vector2f(player.getX() + 16 + MOVEMENT_X, player.getY() + 16 + MOVEMENT_Y));
	}

	@Override
	public void draw() {
		drawQuadImage(image, player.getX() - 16, player.getY() - 16, width, height);
		if(engeryLeft >= 0)
			drawQuadImage(shield_bar, player.getX() - 16,  player.getY() - 26, (int)engeryLeft, 6);
	}
	
	public void die() {
		lights.remove(light);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x - 16, y - 16, 64, 64);
	}

	@Override
	public Rectangle getTopBounds() {
		return new Rectangle(x - 16, y - 16, 64, 8);
	}

	@Override
	public Rectangle getBottomBounds() {
		return new Rectangle(x - 16, y + 40, 64, 8);
	}

	@Override
	public Rectangle getLeftBounds() {
		return new Rectangle(x - 16, y - 16, 8, 64);
	}

	@Override
	public Rectangle getRightBounds() {
		return new Rectangle(x + 40, y - 16, 8, 64);
	}

	public float getEngeryLeft() {
		return engeryLeft;
	}

	public void setEngeryLeft(float engeryLeft) {
		this.engeryLeft = engeryLeft;
	}
}
