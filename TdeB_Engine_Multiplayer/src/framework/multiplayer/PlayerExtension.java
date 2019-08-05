package framework.multiplayer;

import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.drawAnimation;
import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.loadSpriteSheet;
import static framework.helper.Graphics.quickLoaderImage;

import org.newdawn.slick.Animation;

import framework.core.Handler;
import object.player.Player;

public class PlayerExtension extends Player{

	private static final long serialVersionUID = -287827503604601538L;
	private int playerID;

	public PlayerExtension(int x, int y, Handler handler, int playerID) {
		super(x, y, handler);
		
		this.playerID = playerID;
	}
	
	@Override
	public void draw() {
		// init images and animations
		if(idle_right == null)
			this.idle_right = quickLoaderImage("player/player_idle_right");
		if(idle_left == null)
			this.idle_left = quickLoaderImage("player/player_idle_left");
		if(walkLeft == null)
			this.walkLeft = new Animation(loadSpriteSheet("player/player_walk_left", TILE_SIZE, TILE_SIZE), 200);
		if(walkRight == null)
			this.walkRight = new Animation(loadSpriteSheet("player/player_walk_right", TILE_SIZE, TILE_SIZE), 200);	
		
		switch (direction) {
		case "right":
			if(velX == 0 && velY == 0)
				drawQuadImage(idle_right, x, y, width, height);
			else
				drawAnimation(walkRight, x, y, width, height);
			break;
		case "left":
			if(velX == 0 && velY == 0)
				drawQuadImage(idle_left, x, y, width, height);
			else
				drawAnimation(walkLeft, x, y, width, height);
			break;
		default:
			System.out.println("Player velocityX out of range!");
			break;
		}
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}
