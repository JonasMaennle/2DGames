package object.player;

import java.awt.*;

import framework.core.Handler;
import org.lwjgl.util.vector.Vector2f;

import static framework.helper.Collection.*;
import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.*;
import framework.entity.GameEntity;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class Player implements GameEntity {

	private float x, y;
	private int width, height;
	private Handler handler;

	private Animation walk_right_front;
	private Animation walk_left_front;
	private Animation walk_right_back;
	private Animation walk_left_back;
	private Animation currentAnimation;

	private PlayerTask currentTask;
	private Image collisionBox, left_front_idle, left_back_idle, right_back_idle, right_front_idle, idle_image, player_shadow;
	private String movingDirection;
	private boolean showIdle;

	public Player(float x, float y, int width, int height, Handler handler){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.handler = handler;
		this.showIdle = true;
		this.walk_right_front = new Animation(loadSpriteSheet("player/right_front", 64, 128), 100);
		this.walk_left_front = new Animation(loadSpriteSheet("player/left_front", 64, 128), 100);
		this.walk_right_back = new Animation(loadSpriteSheet("player/right_back", 64, 128), 100);
		this.walk_left_back = new Animation(loadSpriteSheet("player/left_back", 64, 128), 100);

		this.collisionBox = quickLoaderImage("tiles/player_box");
		this.left_front_idle = quickLoaderImage("player/left_front_idle");
		this.left_back_idle = quickLoaderImage("player/left_back_idle");
		this.right_front_idle = quickLoaderImage("player/right_front_idle");
		this.right_back_idle = quickLoaderImage("player/right_back_idle");
		this.player_shadow = quickLoaderImage("player/player_shadow");

		this.currentAnimation = walk_right_front;
		this.movingDirection = "right_front";
		this.idle_image = right_front_idle;
	}

	@Override
	public void update() {
		showIdle = true;
		if(currentTask != null) currentTask.performTask();

		switch (movingDirection){
			case "right_front":
				currentAnimation = walk_right_front;
				idle_image = right_front_idle;
				break;
			case "left_front":
				currentAnimation = walk_left_front;
				idle_image = left_front_idle;
				break;
			case "right_back":
				currentAnimation = walk_right_back;
				idle_image = right_back_idle;
				break;
			case "left_back":
				currentAnimation = walk_left_back;
				idle_image = left_back_idle;
				break;
		}

		// System.out.println(currentTask);
	}

	@Override
	public void draw() {
		drawQuadImage(player_shadow, x - 10, y + height - 12, width + 8, 18);

		if(showIdle)
			drawQuadImage(idle_image, x, y, width, height);
		else
			drawAnimation(currentAnimation, x, y, width, height);

		if(currentTask != null)currentTask.renderTask();

		// drawQuadImage(collisionBox, x + 4, y + height - 8, 20, 8);
	}

	@Override
	public float getX() { return x; }
	@Override
	public float getY() { return y; }
	@Override
	public int getWidth() { return width; }
	@Override
	public int getHeight() { return height; }
	@Override
	public Vector2f[] getVertices() { return null; }
	@Override
	public Polygon getBounds() {
		Polygon p = new Polygon();
		p.addPoint((int)x + 12, (int)y + height - 8); // top center
		p.addPoint((int)x + 12, (int)y + height); // bottom center
		p.addPoint((int)x + 4, (int)y + height - 4); // left center
		p.addPoint((int)x + 20, (int) y + height - 4); // right center
		return p;
	}
	@Override
	public Rectangle getTopBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Rectangle getBottomBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Rectangle getLeftBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Rectangle getRightBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public PlayerTask getCurrentTask() { return currentTask; }

	public void setCurrentTask(PlayerTask currentTask) { this.currentTask = currentTask; }

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public void setMovingDirection(String movingDirection) {
		this.movingDirection = movingDirection;
	}

	public boolean isShowIdle() {
		return showIdle;
	}

	public void setShowIdle(boolean showIdle) {
		this.showIdle = showIdle;
	}
}
