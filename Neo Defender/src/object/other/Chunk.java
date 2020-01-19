package object.other;

import static framework.helper.Collection.*;

public class Chunk{
	
	public Tile[][] chunk;
	
	public Chunk() {
		this.chunk = new Tile[CHUNK_SIZE][CHUNK_SIZE];
	}
	
	public void addTileToChunk(Tile t, int x, int y) {
		chunk[x][y] = t;
	}

	public void update() {
		for (int y = 0; y < CHUNK_SIZE; y++) {
			for (int x = 0; x < CHUNK_SIZE; x++) {
				if(chunk[x][y] != null) chunk[x][y].update();
			}
		}
	}

	public void draw() {
		for (int y = 0; y < CHUNK_SIZE; y++) {
			for (int x = 0; x < CHUNK_SIZE; x++) {
				if(chunk[x][y] != null) chunk[x][y].draw();
			}
		}
	}
	
	public int getCenterX() {
		if(chunk[CHUNK_SIZE/2][CHUNK_SIZE/2] != null)
			return (int) chunk[CHUNK_SIZE/2][CHUNK_SIZE/2].getX();
		return -1000000;
	}
	public int getCenterY() {
		if(chunk[CHUNK_SIZE/2][CHUNK_SIZE/2] != null)
			return (int) chunk[CHUNK_SIZE/2][CHUNK_SIZE/2].getY();
		return -1000000;
	}
}
