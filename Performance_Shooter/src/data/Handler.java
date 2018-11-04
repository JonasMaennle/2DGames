package data;

import java.util.ArrayList;

import object.GunganEnemy;
import object.Player;
import static helpers.StateManager.*;

public class Handler {
	
	public ArrayList<Tile> obstacleList = new ArrayList<>();
	public ArrayList<GunganEnemy> gunganList = new ArrayList<>();
	public Player player;
	
	public void update()
	{
		// update Player
		player.update();
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}
	}
	
	public void draw()
	{
		// draw tile map
		map.draw();
		
		// draw player
		player.draw();
		
		// draw gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.draw();
		}
	}
}
