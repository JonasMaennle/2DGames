package framework.core;

import static framework.helper.Collection.*;

import framework.helper.Collection;

public class Camera {

	
	public Camera(){
		reset();
	}
	
	public void update(){
		MOVEMENT_X += Collection.MOVEMENT_OFFSET * 0.75f; // * x.x -> smoothnes factor
		MOVEMENT_Y += 0;
	}

	public void reset(){
		MOVEMENT_X = 0;
		MOVEMENT_Y = 0;
	}
}
