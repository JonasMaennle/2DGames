package framework.core;

import static framework.helper.Collection.*;

import framework.helper.Collection;

public class Camera {
	
	public Camera(){
		reset();
		MOVEMENT_X = 0;
	}
	
	public void update(){
		
	}

	public void reset(){
		MOVEMENT_X = 0 + (TILES_WIDTH * TILE_SIZE) / 4;
		MOVEMENT_Y = 0 - (TILES_HEIGHT * TILE_SIZE) / 16;
	}
}
