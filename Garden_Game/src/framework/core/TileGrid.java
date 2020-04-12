package framework.core;

import static framework.helper.Collection.*;

import object.player.Player;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import framework.core.service.WaveManager;
import object.other.Tile;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	private Handler handler;
	private int setTileCounter;
	private WaveManager waveManager;
	
	public TileGrid(Handler handler){
		this.tilesWide = TILES_WIDTH;//WIDTH / TILE_SIZE;
		this.tilesHigh = TILES_HEIGHT;//HEIGHT / TILE_SIZE; 
		this.map = new Tile[tilesWide][tilesHigh];
		this.handler = handler;
		this.setTileCounter = 0;
		this.waveManager = new WaveManager(map);
	}
	
	public void setTile(int x, int y, int xCoord, int yCoord, Image image, int ID){
		map[x][y] = new Tile(xCoord, yCoord, image, handler, ID);
		if(ID != 34)
			setTileCounter++;
	}
	
	public Tile getTile(int xPlace, int yPlace){
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
		{
			if(map[xPlace][yPlace] == null)
				return new Tile(0, 0, null, handler, 0);
			else
				return map[xPlace][yPlace];
		}
		else
			return null; //new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void draw(){
		waveManager.update();
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){	
				if(!(map[i][j] == null)){
					map[i][j].draw();			
				}
			}
		}
	}

	public int getTilesWide() {
		return tilesWide;
	}

	public void setTilesWide(int tilesWide) {
		this.tilesWide = tilesWide;
	}

	public int getTilesHigh() {
		return tilesHigh;
	}

	public void setTilesHigh(int tilesHigh) {
		this.tilesHigh = tilesHigh;
	}

	public int getSetTileCounter() {
		return setTileCounter;
	}

	public void setSetTileCounter(int setTileCounter) {
		this.setTileCounter = setTileCounter;
	}
}
