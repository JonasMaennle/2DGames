package data;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import helpers.StateManager;
import object.GunganEnemy;
import object.Player;
import static helpers.StateManager.*;

public class Handler {
	
	public ArrayList<Tile> obstacleList = new ArrayList<>();
	public CopyOnWriteArrayList<GunganEnemy> gunganList = new CopyOnWriteArrayList<>();
	public Player player;
	private long timer1, timer2;
	
	public Handler()
	{
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
	}
	
	public void update()
	{
		// update Player
		player.update();
		
		// update gunganEnemy
		for(GunganEnemy g : gunganList)
		{
			g.update();
		}
		
		objectInfo();
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
	
	private void objectInfo()
	{
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 2000)
		{
			timer2 = timer1;
			
			// Data output
			System.out.println("Anzahl Tiles: " + obstacleList.size() + "\tAnzahl Enemies: " + gunganList.size() + "\tFPS: " + StateManager.framesInLastSecond);
		}
	}
}
