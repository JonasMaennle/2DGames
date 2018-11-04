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
		player.update();
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}
	}
	
	public void draw()
	{
		map.draw();
		player.draw();
		
		for(GunganEnemy g : gunganList)
		{
			g.draw();
		}
	}
}
