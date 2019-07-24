package network;

import java.util.Random;
import static framework.helper.Collection.*;


import framework.core.Handler;
import object.player.Bomb;
import object.player.Player;

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
		
		lights.remove(playerLight);
	}
	
	public void update() {
		for(Bomb b : bombList)
			b.update();
	}
	
	public void draw() {
		
		if(placeholder == null) {
			this.placeholder = quickLoaderImage("player/Player_tmp");
		}
		
		super.draw();
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
