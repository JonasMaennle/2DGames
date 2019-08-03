package helpers;

import java.io.BufferedReader;
import static helpers.Artist.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Tile;
import data.TileGrid;
import data.TileType;
import object.obstacle.Obstacle;

public class Leveler {
	
	public static ArrayList<Obstacle> obstacleList = new ArrayList<>();
	
	public static void saveMap(String mapName, TileGrid grid)
	{
		String mapData = "";
		for(int i = 0; i < grid.getTilesWide(); i++)
		{
			for(int j = 0; j < grid.getTilesHigh(); j++)
			{
				mapData += getTileID(grid.getTile(i, j));
			}
		}
		
		try {
			File file = new File(mapName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(mapData);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("saved");
	}
	
	public static TileGrid loadMap(String mapName)
	{
		TileGrid grid = new TileGrid();
		try {
			BufferedReader br = new BufferedReader(new FileReader(mapName));
			String mapData = br.readLine();
			
			// this is where the magic happens
			for(int i = 0; i < grid.getTilesWide(); i++)
			{
				for(int j = 0; j < grid.getTilesHigh(); j++)
				{
					grid.setTile(i, j, getTileType(mapData.substring(i * grid.getTilesHigh() + j, i * grid.getTilesHigh() + j + 1)));
					// get type
					if(grid.getTile(i, j).getType().name().equals("Rock"))
					{
						obstacleList.add(new Obstacle(quickLoad("Rock"), i * 64, j * 64, 64, 64));
					}					
				}
			}	
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return grid;
	}
	
	public static TileType getTileType(String ID)
	{
		TileType tile = TileType.NULL;
		
		switch (ID) {
		case "0":
			tile = TileType.Grass;
			break;
		case "1":
			tile = TileType.Dirt;
			break;
		case "2":
			tile = TileType.Water;
			break;
		case "3":
			tile = TileType.Rock;
			break;
		case "4":
			tile = TileType.NULL;
			break;
		default:
			break;
		}
		return tile;
	}
	
	public static String getTileID(Tile t)
	{
		String ID = "E";
		switch (t.getType()) {
		case Dirt:
			ID = "1";
			break;
		case Grass:
			ID = "0";
			break;
		case Water:
			ID = "2";
			break;
		case Rock:
			ID = "3";
			break;
		case NULL:
			ID = "4";
			break;
		default:
			break;
		}
		return ID;
	}
}
