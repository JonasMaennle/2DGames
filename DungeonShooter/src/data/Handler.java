package data;

import static Gamestate.StateManager.*;
import static helpers.Graphics.drawQuadImageStatic;
import static helpers.Graphics.quickLoaderImage;
import static helpers.Setup.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;

import Enity.Entity;
import Enity.TileType;
import Gamestate.StateManager;
import UI.UI;
import object.Lamp;
import object.Tile;
import object.enemy.Enemy;
import object.entity.Player;
import object.weapon.Laser;
import object.weapon.MapCollectable;

public class Handler {
	
	private StateManager statemanager;
	public CopyOnWriteArrayList<Tile> obstacleList;
	private CopyOnWriteArrayList<ParticleEvent> eventList;
	public CopyOnWriteArrayList<Enemy> enemyList;
	public CopyOnWriteArrayList<MapCollectable> collectableList;
	public CopyOnWriteArrayList<Lamp> lampList;
	
	public Player player;
	
	private long timer1, timer2;
	private TileGrid map;
	private UI gameUI;
	private Image filter;
	
	public Handler(StateManager statemanager)
	{
		this.statemanager = statemanager;
		this.obstacleList = new CopyOnWriteArrayList<>();
		this.eventList = new CopyOnWriteArrayList<>();
		this.enemyList = new CopyOnWriteArrayList<>();
		this.collectableList = new CopyOnWriteArrayList<>();
		this.lampList = new CopyOnWriteArrayList<>();
		
		this.player = null;
		this.filter = quickLoaderImage("background/filter");
		
		this.timer1 = System.currentTimeMillis();
		this.timer2 = timer1;
		this.gameUI = new UI();
		this.gameUI.addButton("Return", "intro/Return", (int)(getLeftBorder() + MOVEMENT_X) + 5, (int)(getTopBorder() + MOVEMENT_Y) + 5, 128, 64);
	}
	
	public void update()
	{
		// update current entity (player || at_st || speeder)
		if(player != null)
		{
			player.update();
		}
		
		// update enemies
		for(Enemy e : enemyList)
		{
			if(e.getX() > getLeftBorder() - 256 && e.getX() < getRightBorder() + 256 && e.getY() > getTopBorder() - 512 && e.getY() < getBottomBorder() + 512)
			{
				e.update();
			}
		}
		
		// update map if player is near
		for(Tile t : obstacleList)
		{
			if(t.getX() > getLeftBorder() - 256 && t.getX() < getRightBorder() + 256 && t.getY() > getTopBorder() - 640 && t.getY() < getBottomBorder() + 640)
			{
				if(t.getType() == TileType.Lava_Light)
				{
					t.update();
				}
			}
			if(t.getType() == TileType.Grass_Round_Half || t.getType() == TileType.Rock_Half)
				t.update();
		}

		// update particle if tile is dead
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				if(event.getX() > getLeftBorder() - 100 && event.getX() < getRightBorder() + 100 && event.getY() > getTopBorder() - 256 && event.getY() < getBottomBorder() + 256)
				{
					event.update(this);
				}
			}
		}
		
		// update "dead" projectiles
		for(Entity e : projectileList)
		{
			e.update();
			
			if(e.getVelX() == 0)
				projectileList.remove(e);
			
			if(e.isOutOfMap())
				projectileList.remove(e);
			
			if(e.getBounds().intersects(player.getBounds()))
			{
				player.damage(10);
				projectileList.remove(e);
				if(e.getClass().getSimpleName().equals("Laser"))
				{
					Laser l = (Laser)e;
					l.removeLight();
				}
			}
		}
		
		// Check if "Return" Button was clicked
		if(gameUI.getButton("Return") != null)
		{
			while(Mouse.next())
			{
				if(gameUI.isButtonClicked("Return"))
				{

				}
			}
		}
		
		// Game information output in console
		objectInfo();
	}
	
	public void draw()
	{		
		// round movemnt x, y // test
		MOVEMENT_X = (int)MOVEMENT_X;
		MOVEMENT_Y = (int)MOVEMENT_Y;
		
		// draw tile map
		map.draw();

		// Draw alpha FILTER
		GL11.glColor4f(0, 0, 0, 0.2f);
		drawQuadImageStatic(filter, 0, 0, 2048, 2048);
		GL11.glColor4f(1, 1, 1, 1);
		
		// draw player
		if(gameState != GameState.DEAD && player != null)
			player.draw();
		
		// draw enemies
		for(Enemy e : enemyList)
		{
			if(e.getX() > getLeftBorder() - 256 && e.getX() < getRightBorder() + 256 && e.getY() > getTopBorder() - 256 && e.getY() < getBottomBorder() + 256)
			{
				e.draw();
			}
		}
		
		// draw "dead" projectiles
		for(Entity e : projectileList)
		{
			e.draw();
		}
		
		// draw weapons
		for(MapCollectable w : collectableList)
		{
			w.draw();
		}

		//draw particles if tile dies
		for(ParticleEvent event : eventList)
		{
			if(event.isListEmpty())
			{
				eventList.remove(event);
			}else{
				if(event.getX() > getLeftBorder() - WIDTH && event.getX() < getRightBorder() + WIDTH && event.getY() > getTopBorder() - HEIGHT && event.getY() < getBottomBorder() + HEIGHT)
				{
					event.draw();
				}
			}
		}
		
		//draw lamp
		for(Lamp lamp : lampList)
		{
			if(lamp.getX() > getLeftBorder() - WIDTH && lamp.getX() < getRightBorder() + WIDTH && lamp.getY() > getTopBorder() - 512 && lamp.getY() < getBottomBorder() + 512)
			{
				lamp.draw();
			}
		}
		
		// draw Buttons
		gameUI.draw();
	}
	
	//@SuppressWarnings("unused")
	private void objectInfo()
	{
		timer1 = System.currentTimeMillis();
		if(timer1 - timer2 > 2000)
		{
			timer2 = timer1;
			// Data output
			System.out.println("Anzahl Tiles: " + obstacleList.size() + "\tAnzahl Enemies: " + enemyList.size() + "\tFPS: " + StateManager.framesInLastSecond + "\t\tLight: " + lights.size() + "\tMovementX: " + MOVEMENT_X);
		}
	}
	
	public void wipe()
	{
		player = null;
		enemyList.clear();
		obstacleList.clear();
		eventList.clear();
		collectableList.clear();
		lampList.clear();
		
		// clean lights
		lights.clear();
		
		// clear old projectiles
		projectileList.clear();
		
		shadowObstacleList.clear();
	}

	public TileGrid getMap() {
		return map;
	}

	public void setMap(TileGrid map) {
		this.map = map;
	}

	public StateManager getStatemanager() {
		return statemanager;
	}

	public void setStatemanager(StateManager statemanager) {
		this.statemanager = statemanager;
	}
	
	public void addParticleEvent(ParticleEvent event){
		eventList.add(event);
	}

	public UI getGameUI() {
		return gameUI;
	}

	public void setGameUI(UI gameUI) {
		this.gameUI = gameUI;
	}
}
