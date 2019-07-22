package network;

import java.util.Random;

import org.newdawn.slick.Animation;

import framework.core.Handler;
import object.player.Player;

import static framework.helper.Collection.TILE_SIZE;
import static framework.helper.Graphics.*;

public class NetworkPlayer extends Player{
	
	private static final long serialVersionUID = -6415163331442886573L;
	private Random rand;
	private int playerUniqueID;
	private long timeout;

	public NetworkPlayer(Player player, Handler handler) {
		super((int)player.getX(), (int)player.getY(), handler);
		rand = new Random();
		playerUniqueID = rand.nextInt(100000000);
		timeout = System.currentTimeMillis();
	}
	
	public void draw() {
		
		if(idle_left == null || idle_right == null || walkRight == null || walkLeft == null) {
			this.idle_left = quickLoaderImage("player/player_idle_left");
			this.idle_right = quickLoaderImage("player/player_idle_right");
			this.walkLeft = new Animation(loadSpriteSheet("player/player_walk_left", TILE_SIZE, TILE_SIZE), 200);
			this.walkRight = new Animation(loadSpriteSheet("player/player_walk_right", TILE_SIZE, TILE_SIZE), 200);	
		}
		
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
		
		weapon.draw();
	}

	public int getPlayerUniqueID() {
		return playerUniqueID;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
}
