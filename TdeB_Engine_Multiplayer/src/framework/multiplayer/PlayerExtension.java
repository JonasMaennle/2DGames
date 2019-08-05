package framework.multiplayer;


import static framework.helper.Collection.TILE_SIZE;
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
			drawQuadImage(idle_right, x, y, width, height);
			break;
		case "left":
			drawQuadImage(idle_left, x, y, width, height);
			break;

		default:
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
