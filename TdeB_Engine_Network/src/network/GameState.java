package network;

import java.io.Serializable;
import java.util.ArrayList;

import object.player.Player;

public class GameState implements Serializable{
	
	private static final long serialVersionUID = 5206546342118019413L;
	public ArrayList<Player> list;
	public int sessionID;
	
	public GameState() {
		this.list = new ArrayList<>();
		this.sessionID = -1;
	}
}
