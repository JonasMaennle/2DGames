package framework.multiplayer;


import static framework.helper.Graphics.drawQuadImage;
import static framework.helper.Graphics.quickLoaderImage;

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
		if(idle_right == null)
			this.idle_right = quickLoaderImage("player/player_idle_right");
		
		drawQuadImage(idle_right, x, y, width, height);
	}


	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}
