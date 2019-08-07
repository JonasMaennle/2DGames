package object.enemy;

import static framework.helper.Collection.MOVEMENT_X;
import static framework.helper.Collection.MOVEMENT_Y;
import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.loadSpriteSheet;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Animation;

import framework.core.Handler;
import framework.core.StateManager;
import framework.core.StateManager.GameState;
import framework.entity.EnemyType;

public class Enemy_Fly extends Enemy_Basic{

	private static final long serialVersionUID = -8136620707204498750L;

	public Enemy_Fly(float x, float y, int width, int height, Handler handler) {
		super(x, y, width, height, handler);
		
		this.hpFactor = 4;
		this.hp *= hpFactor;
		
		
		if(StateManager.gameMode == GameState.GAME)
			this.playerRange = 500;
		else
			this.playerRange = 1000;
		
		this.moveLeft = new Animation(loadSpriteSheet("enemy/Enemy_Fly_left", TILE_SIZE, TILE_SIZE), 200);
		this.moveRight = new Animation(loadSpriteSheet("enemy/Enemy_Fly_right", TILE_SIZE, TILE_SIZE), 200);
		
		this.type = EnemyType.FLY;
	}
	
	public void update() {
		velX = 0;
		velY = 0;
		
		calcDirectPathToPlayer(handler.getPlayer().getX(), handler.getPlayer().getY());
		
		// set direction
		if(velX < 0)
			direction = "left";
		if(velX > 0)
			direction = "right";

		x += (velX * speed);
		y += (velY * speed);
		
		damagePlayer();
		isPlayerInRange(playerRange);
	}
	
	public void draw() {
		if(direction.equals("right")){
			drawAnimation(moveRight, x, y, width, height);
		}else{
			drawAnimation(moveLeft, x, y, width, height);
		}
		// hp bar
		drawQuadImage(hpBar, x, y - 6, hp / hpFactor, 4);
	}
	
	private void calcDirectPathToPlayer(float playerX, float playerY) {
		
		float totalAllowedMovement = 1.0f;
		float xDistanceFromTarget = Math.abs(playerX - x);
		float yDistanceFromTarget = Math.abs(playerY - y);
		float totalDistanceFromTarget = xDistanceFromTarget + yDistanceFromTarget;
		float xPercentOfMovement = xDistanceFromTarget / totalDistanceFromTarget;
		
		velX = xPercentOfMovement;
		velY = totalAllowedMovement - xPercentOfMovement;

		if(playerY < y)
			velY *= -1;
		
		if(playerX < x)
			velX *= -1;	
	}
	
	@Override
	public Vector2f[] getVertices() {
		
		if(direction.equals("left")){
			
			if(moveLeft.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 15, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 15, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 20, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 20, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 12)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 15, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 15, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 20, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 20, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 23, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 12, y + MOVEMENT_Y + 12)
				};
			}
		}else{
			if(moveRight.getFrame() == 0) {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 11, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 11, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 8)
				};
			}else {
				return new Vector2f[] {
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 8),
						new Vector2f(x + MOVEMENT_X + 8, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 16),
						new Vector2f(x + MOVEMENT_X + 4, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 11, y + MOVEMENT_Y + 23),
						new Vector2f(x + MOVEMENT_X + 11, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 19),
						new Vector2f(x + MOVEMENT_X + 16, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 27),
						new Vector2f(x + MOVEMENT_X + 27, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 12),
						new Vector2f(x + MOVEMENT_X + 19, y + MOVEMENT_Y + 8)
				};
			}
		}
	}
}
