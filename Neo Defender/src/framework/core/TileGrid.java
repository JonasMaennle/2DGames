package framework.core;

import static framework.helper.Collection.*;

import org.newdawn.slick.Image;

import object.other.Chunk;
import object.other.Tile;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	private Handler handler;
	private int setTileCounter;
	public Chunk[][] chunkList;
	
	public int chunkWidth, chunkHeight;
	private int renderBorderOffset;
	
	public TileGrid(Handler handler){
		this.tilesWide = TILES_WIDTH;//WIDTH / TILE_SIZE;
		this.tilesHigh = TILES_HEIGHT;//HEIGHT / TILE_SIZE; 
		map = new Tile[tilesWide][tilesHigh];
		this.handler = handler;
		this.setTileCounter = 0;
		this.renderBorderOffset = TILE_SIZE * (CHUNK_SIZE/2);
	}
	
	public void setTile(int xCoord, int yCoord, Image image){
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, image, handler);	
		setTileCounter++;
	}
	
	public Tile getTile(int xPlace, int yPlace){
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
		{
			if(map[xPlace][yPlace] == null)
				return new Tile(0, 0, null, handler);
			else
				return map[xPlace][yPlace];
		}
		else
			return null; //new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void draw(){
		/*
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){	
				if(!(map[i][j] == null)){
					map[i][j].draw();			
				}
			}
		}
		*/
		// draw local chunks

		
		for(int x = 0; x < chunkList.length; x++) {
			for(int y = 0; y < chunkList[0].length; y++) {
				
				if(chunkList[x][y].getCenterX() + renderBorderOffset > getLeftBorder() 
						&& chunkList[x][y].getCenterX() - renderBorderOffset < getRightBorder() 
						&& chunkList[x][y].getCenterY() + renderBorderOffset > getTopBorder() 
						&& chunkList[x][y].getCenterY() - renderBorderOffset < getBottomBorder()) {
					chunkList[x][y].draw();
				}
			}
		}
		
		//System.out.println(getLeftBorder() + "                     " + chunkList[0][1].getCenterY());
		
	}
	
	public void defineCluster() {
		//System.out.println(map.length);
		//System.out.println();
		
		double tmpWidth = map.length/(float)CHUNK_SIZE;
		double tmpHeight = map[0].length/(float)CHUNK_SIZE;
		//System.out.println(Math.ceil(tmpWidth));
		chunkWidth = (int)Math.ceil(tmpWidth);
		chunkHeight = (int)Math.ceil(tmpHeight);
		
		chunkList = new Chunk[chunkWidth][chunkHeight]; // add as much chunks as needed
		// fill with empty chunks
		for(int x = 0; x < chunkWidth; x++) {
			for(int y = 0; y < chunkHeight; y++) {
				chunkList[x][y] = new Chunk();
			}
		}
		
		int cX = -1;
		int cY = -1;
		
		for(int y = 0; y < map[0].length; y++) {
			if(y % CHUNK_SIZE == 0)
				cY++;

			for(int x = 0; x < map.length; x++) {
				if(x % CHUNK_SIZE == 0)
					cX++;

				chunkList[cX][cY].addTileToChunk(map[x][y], x % CHUNK_SIZE, y % CHUNK_SIZE);
			}
			cX = -1;
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
