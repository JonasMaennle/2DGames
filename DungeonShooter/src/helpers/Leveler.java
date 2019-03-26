package helpers;

import static helpers.Setup.*;
import static helpers.Graphics.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import Enity.TileType;
import data.Handler;
import data.TileGrid;
import object.Lamp;
import object.enemy.EnemyBasic;
import object.entity.Player;
import object.weapon.MapCollectable;
import path.Graph;
import path.Node;

public class Leveler {
	
	public static int TILES_WIDTH, TILES_HEIGHT;
	
	public static TileGrid loadMap(Handler handler, String path, Graph graph)
	{
		Image image = quickLoaderImage(path);
		//System.out.println(image.getResourceReference());
		int w = image.getWidth();
		int h = image.getHeight();
		
		TILES_WIDTH = w;
		TILES_HEIGHT = h;
		
		TileGrid grid = new TileGrid(handler);

		//System.out.println("w: " + w + " h: " + h);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				Color c = image.getColor(x, y);
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				
		// Default Wall
				// Black -> Grass_Flat
				if(red == 0 && green == 0 && blue == 0)
				{
					grid.setTile(x, y, TileType.Default);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}

		// Player Tile
				// Red -> Player
				if(red == 255 && green == 0 && blue == 0)
				{
					handler.player = new Player(x * TILE_SIZE, y * TILE_SIZE, handler);
					graph.addNode(new Node(x, y));
				}
		// Enemy Tile
				// Yellow -> Player
				if(red == 255 && green == 201 && blue == 0)
				{
					handler.enemyList.add(new EnemyBasic(x * TILE_SIZE, y * TILE_SIZE, 64, 64));
					graph.addNode(new Node(x, y));
				}
		// Collectable
				// Shotgun
				if(red == 255 && green == 200 && blue == 0)
				{
					handler.collectableList.add(new MapCollectable(x * TILE_SIZE, y * TILE_SIZE, 70, 35, "Shotgun",quickLoaderImage("player/weapon_shotgun_left")));
				}
				// Minigun
				if(red == 255 && green == 160 && blue == 0)
				{
					handler.collectableList.add(new MapCollectable(x * TILE_SIZE, y * TILE_SIZE, 70, 35, "Minigun",quickLoaderImage("player/weapon_minigun_left")));
				}
				// Jetpack
				if(red == 255 && green == 175 && blue == 0)
				{
					handler.collectableList.add(new MapCollectable(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE*2, "Jetpack",quickLoaderImage("player/jetpack_left_full")));
				}
		// Lava 
				if(red == 200 && green == 70 && blue == 0)
				{
					grid.setTile(x, y, TileType.Lava);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				if(red == 255 && green == 70 && blue == 0)
				{
					grid.setTile(x, y, TileType.Lava_Light);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Lamp
				// Yellow
				if(red == 255 && green == 233 && blue == 127)
				{
					Lamp tmpLamp = new Lamp(x * TILE_SIZE, y * TILE_SIZE, 10, 10, 0, 1f);
					handler.lampList.add(tmpLamp);
					shadowObstacleList.add(tmpLamp);
				}
				// Red
				if(red == 255 && green == 127 && blue == 127)
				{
					Lamp tmpLamp = new Lamp(x * TILE_SIZE, y * TILE_SIZE, 10, 0, 0, 1f);
					handler.lampList.add(tmpLamp);
					shadowObstacleList.add(tmpLamp);
				}
				// Blue
				if(red == 127 && green == 146 && blue == 255)
				{
					Lamp tmpLamp = new Lamp(x * TILE_SIZE, y * TILE_SIZE, 0, 0, 10, 1f);
					handler.lampList.add(tmpLamp);
					shadowObstacleList.add(tmpLamp);
				}
				// Green
				if(red == 165 && green == 255 && blue == 127)
				{
					Lamp tmpLamp = new Lamp(x * TILE_SIZE, y * TILE_SIZE, 0, 10, 0, 1f);
					handler.lampList.add(tmpLamp);
					shadowObstacleList.add(tmpLamp);
				}
				// EMPTY -> add Node
				if(red == 255 && green == 255 && blue == 255)
				{
					graph.addNode(new Node(x, y));
				}
			}
		}
		System.out.println(graph.countNodes());
		graph.createMatrix();
		return grid;
	}
}
