package helpers;

import static helpers.Setup.*;
import static helpers.Graphics.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import Enity.TileType;
import data.Handler;
import data.TileGrid;
import object.Goal;
import object.Lamp;
import object.LaserPortal;
import object.enemy.EwokArcherEnemy;
import object.enemy.EwokArcherEnemyRed;
import object.enemy.EwokSoldierEnemy;
import object.enemy.GunganEnemy;
import object.entity.AT_ST_Walker;
import object.entity.Player;
import object.entity.Speeder;
import object.weapon.MapCollectable;

public class Leveler {
	
	public static int TILES_WIDTH, TILES_HEIGHT;
	
	public static TileGrid loadMap(Handler handler, String path)
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
				
		// Grass Tiles
				// Black -> Grass_Flat
				if(red == 0 && green == 0 && blue == 0)
				{
					grid.setTile(x, y, TileType.Grass_Flat);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Left
				if(red == 64 && green == 64 && blue == 64)
				{
					grid.setTile(x, y, TileType.Grass_Left);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Right
				if(red == 48 && green == 48 && blue == 48)
				{
					grid.setTile(x, y, TileType.Grass_Right);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Grass_Round
				if(red == 128 && green == 128 && blue == 128)
				{
					grid.setTile(x, y, TileType.Grass_Round);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Round_Half -> velX = 1
				if(red == 100 && green == 50 && blue == 50)
				{
					grid.setTile(x, y, TileType.Grass_Round_Half);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Round_Half -> velX = -1
				if(red == 100 && green == 50 && blue == 100)
				{
					grid.setTile(x, y, TileType.Grass_Round_Half);
					grid.getTile(x, y).setVelX(-1);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Left_Half
				if(red == 150 && green == 50 && blue == 50)
				{
					grid.setTile(x, y, TileType.Grass_Left_Half);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Right_Half
				if(red == 150 && green == 50 && blue == 70)
				{
					grid.setTile(x, y, TileType.Grass_Right_Half);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Brown -> Grass_Flat_Half
				if(red == 150 && green == 70 && blue == 70)
				{
					grid.setTile(x, y, TileType.Grass_Flat_Half);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Dirt Tile
				// brown -> Dirt_Basic
				if(red == 127 && green == 50 && blue == 0)
				{
					grid.setTile(x, y, TileType.Dirt_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Ramp Start
				if(red == 80 && green == 50 && blue == 0)
				{
					grid.setTile(x, y, TileType.Ramp_Start);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Ramp End
				if(red == 70 && green == 50 && blue == 0)
				{
					grid.setTile(x, y, TileType.Ramp_End);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Rock Tile
				// Grey -> Rock_Basic
				if(red == 196 && green == 196 && blue == 196)
				{
					grid.setTile(x, y, TileType.Rock_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Grey -> Rock_Basic
				if(red == 225 && green == 225 && blue == 225)
				{
					grid.setTile(x, y, TileType.Rock_Decoration);
					shadowObstacleList.add(grid.getTile(x, y));
				}
				// Movable half block
				if(red == 80 && green == 80 && blue == 80)
				{
					grid.setTile(x, y, TileType.Rock_Half);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Metal Tile
				// Grey -> Rock_Basic
				if(red == 42 && green == 42 && blue == 42)
				{
					grid.setTile(x, y, TileType.Metal_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Player Tile
				// Red -> Player
				if(red == 255 && green == 0 && blue == 0)
				{
					handler.player = new Player(x * TILE_SIZE, y * TILE_SIZE, handler);
				}
				// Blue -> Player
				if(red == 0 && green == 0 && blue == 255)
				{
					handler.at_st_walker = new AT_ST_Walker(x * TILE_SIZE, y * TILE_SIZE, handler);
				}
				// Blue -> Player
				if(red == 0 && green == 255 && blue == 255)
				{
					handler.speeder = new Speeder(x * TILE_SIZE, y * TILE_SIZE, handler);
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
		// Enemy Tile
				// Green -> GunganEnemy
				if(red == 0 && green == 255 && blue == 0)
				{
					handler.enemyList.add(new GunganEnemy(x * TILE_SIZE, y * TILE_SIZE, 64, 128, handler));
				}
				if(red == 0 && green == 255 && blue == 120)
				{
					handler.enemyList.add(new GunganEnemy(x * TILE_SIZE, y * TILE_SIZE, 64, 128, handler, 2));
				}
				// Ewok Archer
				if(red == 0 && green == 127 && blue == 0)
				{
					handler.enemyList.add(new EwokArcherEnemy(x * TILE_SIZE, y * TILE_SIZE, 48, 80, true, handler));
				}
				// Ewok FireArcher
				if(red == 0 && green == 100 && blue == 0)
				{
					handler.enemyList.add(new EwokArcherEnemyRed(x * TILE_SIZE, y * TILE_SIZE, 48, 80, true, handler));
				}
				// Ewok Soldier
				if(red == 0 && green == 70 && blue == 0)
				{
					handler.enemyList.add(new EwokSoldierEnemy(x * TILE_SIZE, y * TILE_SIZE, 48, 80, handler));
				}
		// Goal Tile
				// Pink -> Level Goal
				if(red == 255 && green == 0 && blue == 255)
				{
					handler.levelGoal = new Goal(x * TILE_SIZE, y * TILE_SIZE);
				}
		// Tree stump
				if(red == 80 && green == 0 && blue == 127) // Stump Left
				{
					grid.setTile(x, y, TileType.TreeStump_Left);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				if(red == 85 && green == 0 && blue == 127) // Stump Center
				{
					grid.setTile(x, y, TileType.TreeStump_Center);
					handler.obstacleList.add(grid.getTile(x, y));
				}
				if(red == 90 && green == 0 && blue == 127) // Stump Right
				{
					grid.setTile(x, y, TileType.TreeStump_Right);
					handler.obstacleList.add(grid.getTile(x, y));
				}
		// Decoration Tile -> Tree
				// Redwood
				if(red == 180 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_01);
				}
				if(red == 170 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_02);
				}
				if(red == 160 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_03);
				}
				if(red == 150 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_04);
				}
				if(red == 140 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_05);
				}
				if(red == 130 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_06);
				}
				if(red == 120 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_07);
				}
				if(red == 110 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_08);
				}
				if(red == 100 && green == 55 && blue == 0)
				{
					grid.setTile(x, y, TileType.RedWood_09);
				}
				// Platform
				if(red == 70 && green == 0 && blue == 255)
				{
					grid.setTile(x, y, TileType.Platform);
					handler.enemyList.add(new EwokArcherEnemy(x * TILE_SIZE + 40, y * TILE_SIZE - 48, 48, 80, false, handler));
				}
		// Ice
				if(red == 0 && green == 148 && blue == 255)
				{
					grid.setTile(x, y, TileType.Ice_Basic);
					handler.obstacleList.add(grid.getTile(x, y));
					shadowObstacleList.add(grid.getTile(x, y));
				}
		// Laser Portal
				if(red == 190 && green == 75 && blue == 75)
				{
					LaserPortal l = new LaserPortal( x * TILE_SIZE, y * TILE_SIZE);
					handler.portalList.add(l);
					shadowObstacleList.add(l);
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
			}
		}
		handler.setCurrentEntity(handler.player);
		return grid;
	}
}
